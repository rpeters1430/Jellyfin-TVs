package com.example.jellyfintv.data.model

/**
 * Enum representing different types of images that can be associated with a media item.
 */
enum class ImageType {
    PRIMARY,
    BACKDROP,
    THUMB,
    LOGO,
    ART,
    BANNER,
    DISC,
    BOX,
    SCREENSHOT,
    MENU,
    CHAPTER,
    BOX_REAR,
    PROFILE,
    
    companion object {
        /**
         * Converts a string representation of an image type to the corresponding enum value.
         * @param type The string representation of the image type (case-insensitive).
         * @return The corresponding ImageType enum value, or null if no match is found.
         */
        fun fromString(type: String?): ImageType? {
            return when (type?.uppercase()) {
                "PRIMARY" -> PRIMARY
                "BACKDROP" -> BACKDROP
                "THUMB" -> THUMB
                "LOGO" -> LOGO
                "ART" -> ART
                "BANNER" -> BANNER
                "DISC" -> DISC
                "BOX" -> BOX
                "SCREENSHOT" -> SCREENSHOT
                "MENU" -> MENU
                "CHAPTER" -> CHAPTER
                "BOX_REAR" -> BOX_REAR
                "PROFILE" -> PROFILE
                else -> null
            }
        }
    }
}
