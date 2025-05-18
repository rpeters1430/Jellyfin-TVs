package com.example.jellyfintv.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.jellyfintv.JellyfinTVApplication

/**
 * A utility class for managing shared preferences in a type-safe way.
 */
class Prefs private constructor() {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = JellyfinTVApplication.appContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    companion object {
        private const val PREFS_NAME = "jellyfin_tv_prefs"
        private const val KEY_SERVER_URL = "server_url"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_LAST_SERVER = "last_server"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_QUALITY_SELECTION = "quality_selection"
        private const val KEY_SUBTITLE_LANGUAGE = "subtitle_language"
        private const val KEY_AUDIO_LANGUAGE = "audio_language"
        private const val KEY_THEME = "app_theme"
        private const val KEY_PLAYBACK_SPEED = "playback_speed"
        private const val KEY_AUTO_PLAY_NEXT = "auto_play_next"
        private const val KEY_SUBTITLE_SIZE = "subtitle_size"
        private const val KEY_SUBTITLE_STYLE = "subtitle_style"
        private const val KEY_HARDWARE_DECODING = "hardware_decoding"
        private const val KEY_VIDEO_PLAYER = "video_player"
        private const val KEY_OFFLINE_MODE = "offline_mode"
        private const val KEY_LAST_UPDATE_CHECK = "last_update_check"
        private const val KEY_ANALYTICS_ENABLED = "analytics_enabled"
        private const val KEY_CRASH_REPORTING_ENABLED = "crash_reporting_enabled"

        @Volatile
        private var instance: Prefs? = null

        fun getInstance(): Prefs {
            return instance ?: synchronized(this) {
                instance ?: Prefs().also { instance = it }
            }
        }
    }

    // Server
    var serverUrl: String?
        get() = sharedPreferences.getString(KEY_SERVER_URL, null)
        set(value) = sharedPreferences.edit { putString(KEY_SERVER_URL, value) }

    // User
    var userId: String?
        get() = sharedPreferences.getString(KEY_USER_ID, null)
        set(value) = sharedPreferences.edit { putString(KEY_USER_ID, value) }

    var accessToken: String?
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        set(value) = sharedPreferences.edit { putString(KEY_ACCESS_TOKEN, value) }

    var refreshToken: String?
        get() = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        set(value) = sharedPreferences.edit { putString(KEY_REFRESH_TOKEN, value) }

    var userName: String?
        get() = sharedPreferences.getString(KEY_USER_NAME, null)
        set(value) = sharedPreferences.edit { putString(KEY_USER_NAME, value) }

    var userEmail: String?
        get() = sharedPreferences.getString(KEY_USER_EMAIL, null)
        set(value) = sharedPreferences.edit { putString(KEY_USER_EMAIL, value) }

    var lastServer: String?
        get() = sharedPreferences.getString(KEY_LAST_SERVER, null)
        set(value) = sharedPreferences.edit { putString(KEY_LAST_SERVER, value) }

    var rememberMe: Boolean
        get() = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_REMEMBER_ME, value) }

    // Playback
    var qualitySelection: String?
        get() = sharedPreferences.getString(KEY_QUALITY_SELECTION, "Auto")
        set(value) = sharedPreferences.edit { putString(KEY_QUALITY_SELECTION, value) }

    var subtitleLanguage: String?
        get() = sharedPreferences.getString(KEY_SUBTITLE_LANGUAGE, "English")
        set(value) = sharedPreferences.edit { putString(KEY_SUBTITLE_LANGUAGE, value) }

    var audioLanguage: String?
        get() = sharedPreferences.getString(KEY_AUDIO_LANGUAGE, "English")
        set(value) = sharedPreferences.edit { putString(KEY_AUDIO_LANGUAGE, value) }

    var theme: String?
        get() = sharedPreferences.getString(KEY_THEME, "System")
        set(value) = sharedPreferences.edit { putString(KEY_THEME, value) }

    var playbackSpeed: Float
        get() = sharedPreferences.getFloat(KEY_PLAYBACK_SPEED, 1.0f)
        set(value) = sharedPreferences.edit { putFloat(KEY_PLAYBACK_SPEED, value) }

    var autoPlayNext: Boolean
        get() = sharedPreferences.getBoolean(KEY_AUTO_PLAY_NEXT, true)
        set(value) = sharedPreferences.edit { putBoolean(KEY_AUTO_PLAY_NEXT, value) }

    var subtitleSize: Int
        get() = sharedPreferences.getInt(KEY_SUBTITLE_SIZE, 16)
        set(value) = sharedPreferences.edit { putInt(KEY_SUBTITLE_SIZE, value) }

    var subtitleStyle: String?
        get() = sharedPreferences.getString(KEY_SUBTITLE_STYLE, "Default")
        set(value) = sharedPreferences.edit { putString(KEY_SUBTITLE_STYLE, value) }

    var hardwareDecoding: Boolean
        get() = sharedPreferences.getBoolean(KEY_HARDWARE_DECODING, true)
        set(value) = sharedPreferences.edit { putBoolean(KEY_HARDWARE_DECODING, value) }

    var videoPlayer: String?
        get() = sharedPreferences.getString(KEY_VIDEO_PLAYER, "ExoPlayer")
        set(value) = sharedPreferences.edit { putString(KEY_VIDEO_PLAYER, value) }

    var offlineMode: Boolean
        get() = sharedPreferences.getBoolean(KEY_OFFLINE_MODE, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_OFFLINE_MODE, value) }

    // App
    var lastUpdateCheck: Long
        get() = sharedPreferences.getLong(KEY_LAST_UPDATE_CHECK, 0)
        set(value) = sharedPreferences.edit { putLong(KEY_LAST_UPDATE_CHECK, value) }

    var analyticsEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_ANALYTICS_ENABLED, true)
        set(value) = sharedPreferences.edit { putBoolean(KEY_ANALYTICS_ENABLED, value) }

    var crashReportingEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_CRASH_REPORTING_ENABLED, true)
        set(value) = sharedPreferences.edit { putBoolean(KEY_CRASH_REPORTING_ENABLED, value) }

    /**
     * Clears all user-related preferences (keeps app settings).
     */
    fun clearUserData() {
        sharedPreferences.edit {
            remove(KEY_USER_ID)
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_LAST_SERVER)
            remove(KEY_REMEMBER_ME)
        }
    }

    /**
     * Clears all preferences.
     */
    fun clearAll() {
        sharedPreferences.edit {
            clear()
        }
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise.
     */
    fun isLoggedIn(): Boolean {
        return !accessToken.isNullOrEmpty() && !userId.isNullOrEmpty()
    }

    /**
     * Saves user session data.
     *
     * @param userId The user's ID.
     * @param accessToken The access token.
     * @param refreshToken The refresh token.
     * @param userName The user's display name.
     * @param userEmail The user's email.
     */
    fun saveUserSession(
        userId: String,
        accessToken: String,
        refreshToken: String,
        userName: String?,
        userEmail: String?
    ) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, userId)
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putBoolean(KEY_REMEMBER_ME, true)
        }
    }
}
