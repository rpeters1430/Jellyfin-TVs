package com.example.jellyfintv.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItemDataDto(
    val isFavorite: Boolean? = null,
    val played: Boolean? = null,
    val playCount: Int? = null,
    val playbackPositionTicks: Long? = null,
    val lastPlayedDate: String? = null,
    val playedPercentage: Double? = null,
    val unplayedItemCount: Int? = null,
    val playableMediaTypes: List<String>? = null
) : Parcelable
