package com.example.jellyfintv.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents user data for a media item.
 */
@Parcelize
data class UserItemDataDto(
    val played: Boolean = false,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val playbackPositionTicks: Long = 0,
    val lastPlayedDate: String? = null,
    val playedPercentage: Double? = null,
    val key: String? = null,
    val itemId: String? = null
) : Parcelable

/**
 * Represents media source information.
 */
@Parcelize
data class MediaSourceInfo(
    val id: String = "",
    val path: String? = null,
    val protocol: String? = null,
    val type: String? = null,
    val container: String? = null,
    val size: Long? = null,
    val name: String? = null,
    val isRemote: Boolean = false,
    val runTimeTicks: Long? = null,
    val containerStartTimeTicks: Long? = null,
    val supportsTranscoding: Boolean = false,
    val supportsDirectStream: Boolean = false,
    val supportsDirectPlay: Boolean = false,
    val isInfiniteStream: Boolean = false,
    val requiresOpening: Boolean = false,
    val openToken: String? = null,
    val requiresClosing: Boolean = false,
    val liveStreamId: String? = null,
    val bufferMs: Int? = null,
    val requiresLooping: Boolean = false,
    val supportsProbing: Boolean = true,
    val videoType: String? = null,
    val mediaStreams: List<MediaStream> = emptyList(),
    val mediaAttachments: List<MediaAttachment> = emptyList(),
    val formats: List<String> = emptyList(),
    val bitrate: Int? = null,
    val timestamp: String? = null,
    val requiredHttpHeaders: Map<String, String> = emptyMap(),
    val transcodingUrl: String? = null,
    val transcodingSubProtocol: String? = null,
    val transcodingContainer: String? = null,
    val analyzeDurationMs: Int? = null,
    val defaultAudioStreamIndex: Int? = null,
    val defaultSubtitleStreamIndex: Int? = null
) : Parcelable

/**
 * Represents a media stream.
 */
@Parcelize
data class MediaStream(
    val codec: String? = null,
    val codecTag: String? = null,
    val language: String? = null,
    val colorRange: String? = null,
    val colorSpace: String? = null,
    val colorTransfer: String? = null,
    val colorPrimaries: String? = null,
    val comment: String? = null,
    val timeBase: String? = null,
    val codecTimeBase: String? = null,
    val title: String? = null,
    val localUndefinedField: String? = null,
    val displayTitle: String? = null,
    val nalLengthSize: String? = null,
    val isInterlaced: Boolean = false,
    val isAVC: Boolean? = null,
    val channelLayout: String? = null,
    val bitRate: Int? = null,
    val bitDepth: Int? = null,
    val refFrames: Int? = null,
    val packetLength: Int? = null,
    val channels: Int? = null,
    val sampleRate: Int? = null,
    val isDefault: Boolean = false,
    val isForced: Boolean = false,
    val isHearingImpaired: Boolean = false,
    val height: Int? = null,
    val width: Int? = null,
    val averageFrameRate: Float? = null,
    val realFrameRate: Float? = null,
    val profile: String? = null,
    val type: String? = null,
    val aspectRatio: String? = null,
    val index: Int = 0,
    val isExternal: Boolean = false,
    val isTextSubtitleStream: Boolean = false,
    val supportsExternalStream: Boolean = false,
    val path: String? = null,
    val pixelFormat: String? = null,
    val level: Double? = null,
    val isAnamorphic: Boolean? = null
) : Parcelable

/**
 * Represents a media attachment.
 */
@Parcelize
data class MediaAttachment(
    val codec: String? = null,
    val codecTag: String? = null,
    val comment: String? = null,
    val index: Int = 0,
    val fileName: String? = null,
    val mimeType: String? = null,
    val deliveryUrl: String? = null
) : Parcelable

/**
 * Represents a base item DTO from the Jellyfin API.
 */
@Parcelize
data class BaseItemDto(
    val id: String = "",
    val name: String = "",
    val serverId: String? = null,
    val playlistItemId: String? = null,
    val type: String? = null,
    val mediaType: String? = null,
    val isFolder: Boolean? = null,
    val runTimeTicks: Long? = null,
    val container: String? = null,
    val productionYear: Int? = null,
    val indexNumber: Int? = null,
    val parentIndexNumber: Int? = null,
    val premiereDate: String? = null,
    val imageTags: Map<String, String>? = null,
    val backdropImageTags: List<String>? = null,
    val parentLogoImageTag: String? = null,
    val seriesId: String? = null,
    val seriesName: String? = null,
    val seasonId: String? = null,
    val seasonName: String? = null,
    val userData: UserItemDataDto? = null,
    val recursiveItemCount: Int? = null,
    val childCount: Int? = null,
    val seriesPrimaryImageTag: String? = null,
    val seasonUserData: UserItemDataDto? = null,
    val locationType: String? = null,
    val mediaSources: List<MediaSourceInfo>? = null,
    val cumulativeRunTimeTicks: Long? = null,
    val playAccess: String? = null,
    val remoteTrailers: List<MediaUrl>? = null,
    val providerIds: Map<String, String>? = null,
    val isHD: Boolean? = null,
    val isFolderItem: Boolean? = null,
    val isMovie: Boolean? = null,
    val isSeries: Boolean? = null,
    val isNews: Boolean? = null,
    val isKids: Boolean? = null,
    val isSports: Boolean? = null,
    val displayPreferencesId: String? = null,
    val tags: List<String>? = null,
    val status: String? = null,
    val airTime: String? = null,
    val officialRating: String? = null,
    val customRating: String? = null,
    val overview: String? = null,
    val taglines: List<String>? = null,
    val genres: List<String>? = null,
    val communityRating: Float? = null,
    val voteCount: Int? = null,
    val canDelete: Boolean? = null,
    val canDownload: Boolean? = null,
    val hasSubtitles: Boolean? = null,
    val preferredMetadataLanguage: String? = null,
    val preferredMetadataCountryCode: String? = null,
    val isPlaceHolder: Boolean? = null,
    val parentId: String? = null,
    val people: List<BaseItemPerson>? = null,
    val studios: List<NameIdPair>? = null,
    val genreItems: List<NameIdPair>? = null,
    val parentLogoItemId: String? = null,
    val parentBackdropItemId: String? = null,
    val parentBackdropImageTags: List<String>? = null,
    val localTrailerCount: Int? = null,
    val specialFeatureCount: Int? = null,
    val aspectRatio: String? = null,
    val number: String? = null,
    val channelNumber: String? = null,
    val indexNumberEnd: Int? = null
) : Parcelable

/**
 * Represents a media URL.
 */
@Parcelize
data class MediaUrl(
    val url: String,
    val name: String? = null
) : Parcelable

/**
 * Represents a name-ID pair.
 */
@Parcelize
data class NameIdPair(
    val name: String? = null,
    val id: String? = null
) : Parcelable

/**
 * Represents a person associated with a base item.
 */
@Parcelize
data class BaseItemPerson(
    val name: String,
    val id: String,
    val role: String? = null,
    val type: String? = null,
    val primaryImageTag: String? = null
) : Parcelable
