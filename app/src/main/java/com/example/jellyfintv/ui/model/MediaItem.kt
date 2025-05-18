package com.example.jellyfintv.ui.model

import android.os.Parcel
import android.os.Parcelable
import com.example.jellyfintv.data.model.BaseItemDto
import com.example.jellyfintv.data.model.ImageType
import com.example.jellyfintv.data.model.MediaSourceInfo
import com.example.jellyfintv.data.model.UserItemDataDto
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

// Suppress ParcelCreator needed warning as we're using @Parcelize
@Suppress("ParcelCreator")

/**
 * Represents a media item in the Jellyfin TV app.
 * This can be a movie, TV show, season, episode, etc.
 */
@Parcelize
data class MediaItem(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val overview: String = "",
    val year: Int? = null,
    val runtimeTicks: Long? = null,
    val userData: UserItemDataDto? = null,
    val imageTags: Map<ImageType, String>? = null,
    val seriesId: String? = null,
    val seriesName: String? = null,
    val seasonId: String? = null,
    val seasonName: String? = null,
    val indexNumber: Int? = null,
    val parentIndexNumber: Int? = null,
    val runTimeTicks: Long? = null,
    val playAccess: String? = null,
    val mediaSources: List<MediaSourceInfo>? = null,
    val mediaType: String? = null,
    val isFolder: Boolean = false,
    val isPlayed: Boolean = false,
    val isFavorite: Boolean = false,
    val canDownload: Boolean = false,
    val canDelete: Boolean = false,
    val canPlay: Boolean = false,
    val canSeek: Boolean = false,
    val canScrobble: Boolean = false
) : Parcelable {
    
    val isMovie: Boolean 
        get() = type.equals("Movie", true)
        
    val isEpisode: Boolean 
        get() = type.equals("Episode", true)
        
    val isSeries: Boolean 
        get() = type.equals("Series", true)
        
    val isSeason: Boolean 
        get() = type.equals("Season", true)
    
    val primaryImageTag: String? 
        get() = imageTags?.get(ImageType.PRIMARY)
        
    val thumbImageTag: String? 
        get() = imageTags?.get(ImageType.THUMB)
        
    val backdropImageTag: String? 
        get() = imageTags?.get(ImageType.BACKDROP)
    
    val runtime: Long? 
        get() = runTimeTicks ?: runtimeTicks
    
    val hasSubtitles: Boolean 
        get() = mediaSources?.firstOrNull()?.mediaStreams?.any { stream -> 
            stream.type?.equals("Subtitle", true) ?: false
        } ?: false
    
    /**
     * Builds an image URL for this media item.
     * 
     * @param imageType The type of image (Primary, Backdrop, Thumb, etc.)
     * @param maxWidth The maximum width of the image (optional)
     * @return The complete image URL or empty string if not available
     */
    fun getImageUrl(imageType: ImageType, maxWidth: Int = 600): String {
        // TODO: Implement proper image URL building with your server URL
        val tag = when (imageType) {
            ImageType.PRIMARY -> primaryImageTag
            ImageType.BACKDROP -> backdropImageTag
            ImageType.THUMB -> thumbImageTag
            else -> null
        }
        
        return tag ?: ""
    }
    
    companion object {
        /**
         * Creates a [MediaItem] from a [BaseItemDto].
         */
        fun fromBaseItem(item: BaseItemDto, userData: UserItemDataDto? = null): MediaItem {
            // Convert string-based image tags to ImageType enum keys
            val imageTags = item.imageTags?.mapKeys { (key, _) ->
                ImageType.fromString(key) ?: ImageType.PRIMARY
            }
            
            return MediaItem(
                id = item.id,
                name = item.name,
                type = item.type ?: "",
                overview = item.overview ?: "",
                year = item.productionYear,
                runtimeTicks = item.runTimeTicks,
                userData = userData ?: item.userData,
                imageTags = imageTags,
                seriesId = item.seriesId,
                seriesName = item.seriesName,
                seasonId = item.seasonId,
                seasonName = item.seasonName,
                indexNumber = item.indexNumber,
                parentIndexNumber = item.parentIndexNumber,
                runTimeTicks = item.runTimeTicks,
                playAccess = item.playAccess,
                mediaSources = item.mediaSources,
                mediaType = item.mediaType,
                isFolder = item.isFolder ?: false,
                isPlayed = userData?.played ?: false,
                isFavorite = userData?.isFavorite ?: false,
                canDownload = item.canDownload ?: false,
                canDelete = item.canDelete ?: false,
                canPlay = item.canPlay ?: false,
                canSeek = item.canSeek ?: false,
                canScrobble = item.canScrobble ?: false
            )
        }
    }
}

@Parcelize
data class UserData(
    val isFavorite: Boolean,
    val played: Boolean,
    val playCount: Int,
    val playbackPositionTicks: Long
) : Parcelable {
    companion object {
        fun fromUserItemDataDto(dto: UserItemDataDto): UserData {
            return UserData(
                isFavorite = dto.isFavorite ?: false,
                played = dto.played ?: false,
                playCount = dto.playCount ?: 0,
                playbackPositionTicks = dto.playbackPositionTicks ?: 0L
            )
        }
    }
}
