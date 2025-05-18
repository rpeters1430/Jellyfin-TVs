package com.example.jellyfintv.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseItemDto(
    val id: String? = null,
    val name: String? = null,
    val overview: String? = null,
    val type: ItemType? = null,
    val imageTags: Map<ImageType, String>? = null,
    val backdropImageTags: List<String>? = null,
    val productionYear: Int? = null,
    val runTimeTicks: Long? = null,
    val userData: UserItemDataDto? = null
) : Parcelable

@Parcelize
enum class ItemType {
    Movie,
    Series,
    Season,
    Episode,
    MusicAlbum,
    MusicArtist,
    MusicVideo,
    Audio,
    Photo,
    PhotoAlbum,
    Playlist,
    BoxSet,
    Book,
    Person,
    Genre,
    Studio,
    UserView,
    CollectionFolder,
    Folder,
    Program,
    Channel,
    TvChannel,
    TvProgram,
    LiveTvChannel,
    PlaylistFolder,
    UserRootFolder,
    UserViewFolder,
    CollectionFolderItem,
    ProgramVideo,
    ChannelVideoItem,
    Trailer,
    Extra
}

@Parcelize
enum class ImageType {
    Primary,
    Art,
    Backdrop,
    Banner,
    Logo,
    Thumb,
    Disc,
    Box,
    Screenshot,
    Menu,
    Chapter,
    BoxRear,
    Profile
}
