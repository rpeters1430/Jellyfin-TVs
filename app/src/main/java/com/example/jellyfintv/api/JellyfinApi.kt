package com.example.jellyfintv.api

import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.api.operations.SystemApi
import org.jellyfin.sdk.api.operations.UserLibraryApi
import org.jellyfin.sdk.api.operations.UsersApi
import org.jellyfin.sdk.model.*
import org.jellyfin.sdk.model.api.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

interface JellyfinApi {
    suspend fun connectToServer(serverUrl: String, username: String, password: String): Result<ConnectionResult>
    suspend fun getLibraries(): Result<List<BaseItemDto>>
    suspend fun getLatestMedia(): Result<List<BaseItemDto>>
    suspend fun getContinueWatching(): Result<List<BaseItemDto>>
    suspend fun getItem(itemId: String): Result<BaseItemDto>
    suspend fun getChildren(itemId: String): Result<List<BaseItemDto>>
    suspend fun getStreamUrl(itemId: String): String
    
    data class ConnectionResult(
        val serverInfo: ServerInfo,
        val userDto: UserDto,
        val accessToken: String
    )
}

class JellyfinApiImpl : JellyfinApi, KoinComponent {
    private val preferencesManager: PreferencesManager by inject()
    
    private val client by lazy {
        Jellyfin {
            clientInfo = ClientInfo(name = "JellyfinTV", version = "1.0.0")
            context = AndroidContext(androidContext())
            serverUrl = preferencesManager.serverUrl
            accessToken = preferencesManager.accessToken
            userId = preferencesManager.userId?.let { UUID.fromString(it) }
        }.createKtor()
    }
    
    private val systemApi by lazy { SystemApi(client) }
    private val usersApi by lazy { UsersApi(client) }
    private val userLibraryApi by lazy { UserLibraryApi(client) }
    
    override suspend fun connectToServer(serverUrl: String, username: String, password: String): Result<JellyfinApi.ConnectionResult> {
        return try {
            // Update client with new server URL
            client.serverUrl = serverUrl
            
            // Get server info
            val serverInfo = systemApi.getPublicSystemInfo()
            
            // Authenticate user
            val authResult = usersApi.authenticateUserByName(
                authenticateUserByName = AuthenticateUserByName(
                    username = username,
                    pw = password
                )
            )
            
            // Update client with auth token
            client.accessToken = authResult.accessToken
            client.userId = authResult.user?.id
            
            // Save to preferences
            preferencesManager.apply {
                this.serverUrl = serverUrl
                accessToken = authResult.accessToken
                userId = authResult.user?.id?.toString()
                username = username
            }
            
            Result.success(
                JellyfinApi.ConnectionResult(
                    serverInfo = serverInfo,
                    userDto = authResult.user!!,
                    accessToken = authResult.accessToken!!
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getLibraries(): Result<List<BaseItemDto>> {
        return try {
            val userId = UUID.fromString(preferencesManager.userId)
            val views = userViewsApi.getUserViews(userId = userId).items ?: emptyList()
            Result.success(views)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getLatestMedia(): Result<List<BaseItemDto>> {
        return try {
            val userId = UUID.fromString(preferencesManager.userId)
            val latest = userLibraryApi.getLatestMedia(
                userId = userId,
                parentId = null,
                fields = listOf(
                    ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
                    ItemFields.BASIC_SYNOPSIS
                )
            )
            Result.success(latest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getContinueWatching(): Result<List<BaseItemDto>> {
        return try {
            val userId = UUID.fromString(preferencesManager.userId)
            val result = userLibraryApi.getResumeItems(
                userId = userId,
                limit = 20,
                fields = listOf(
                    ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
                    ItemFields.BASIC_SYNOPSIS
                )
            )
            Result.success(result.items ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getItem(itemId: String): Result<BaseItemDto> {
        return try {
            val userId = preferencesManager.userId?.let { UUID.fromString(it) }
            val item = userLibraryApi.getItem(
                userId = userId,
                itemId = UUID.fromString(itemId),
                fields = listOf(
                    ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
                    ItemFields.BASIC_SYNOPSIS,
                    ItemFields.MEDIA_SOURCES
                )
            )
            Result.success(item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getChildren(itemId: String): Result<List<BaseItemDto>> {
        return try {
            val userId = preferencesManager.userId?.let { UUID.fromString(it) }
            val result = userLibraryApi.getItems(
                userId = userId,
                parentId = UUID.fromString(itemId),
                fields = listOf(
                    ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
                    ItemFields.BASIC_SYNOPSIS
                )
            )
            Result.success(result.items ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getStreamUrl(itemId: String): String {
        val baseUrl = preferencesManager.serverUrl?.removeSuffix("/") ?: ""
        return "$baseUrl/Videos/$itemId/stream?static=true&api_key=${preferencesManager.accessToken}"
    }
}
