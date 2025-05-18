package com.example.jellyfintv.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utility class for handling date and time operations.
 */
object DateTimeUtils {

    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val DISPLAY_DATE_FORMAT = "MMM d, yyyy"
    private const val DISPLAY_TIME_FORMAT = "h:mm a"
    private const val DISPLAY_DATETIME_FORMAT = "MMM d, yyyy h:mm a"
    private const val DURATION_FORMAT = "%02d:%02d:%02d"

    /**
     * Parses a date string from the server and returns a [Date] object.
     *
     * @param dateString The date string to parse.
     * @return The parsed [Date] object, or null if the string is null or empty.
     */
    fun parseServerDate(dateString: String?): Date? {
        if (dateString.isNullOrEmpty()) return null
        return try {
            SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            Timber.e(e, "Error parsing date: $dateString")
            null
        }
    }

    /**
     * Formats a date as a display string.
     *
     * @param date The date to format.
     * @return The formatted date string.
     */
    fun formatDate(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).format(date)
    }

    /**
     * Formats a date and time as a display string.
     *
     * @param date The date to format.
     * @return The formatted date and time string.
     */
    fun formatDateTime(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat(DISPLAY_DATETIME_FORMAT, Locale.getDefault()).format(date)
    }

    /**
     * Formats a time as a display string.
     *
     * @param date The date containing the time to format.
     * @return The formatted time string.
     */
    fun formatTime(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat(DISPLAY_TIME_FORMAT, Locale.getDefault()).format(date)
    }

    /**
     * Formats a duration in milliseconds as a string in the format "HH:MM:SS".
     *
     * @param durationMs The duration in milliseconds.
     * @return The formatted duration string.
     */
    fun formatDuration(durationMs: Long): String {
        if (durationMs <= 0) return "00:00:00"

        val hours = TimeUnit.MILLISECONDS.toHours(durationMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1)

        return String.format(Locale.getDefault(), DURATION_FORMAT, hours, minutes, seconds)
    }

    /**
     * Formats a duration in milliseconds as a human-readable string.
     * Example: "2h 30m" or "45m 30s" or "1d 3h 15m"
     *
     * @param durationMs The duration in milliseconds.
     * @return The formatted duration string.
     */
    fun formatDurationHumanReadable(durationMs: Long): String {
        if (durationMs <= 0) return "0s"

        val days = TimeUnit.MILLISECONDS.toDays(durationMs)
        val hours = TimeUnit.MILLISECONDS.toHours(durationMs) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60

        return when {
            days > 0 -> "${days}d ${hours}h ${minutes}m"
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }


    /**
     * Gets a relative time span string for the given date.
     * Example: "2 hours ago", "yesterday", "3 days ago", etc.
     *
     * @param date The date to get the relative time span for.
     * @return The relative time span string.
     */
    fun getRelativeTimeSpanString(date: Date?): String {
        if (date == null) return ""

        val now = Date()
        val duration = now.time - date.time

        return when {
            duration < 0 -> "in the future" // Shouldn't happen, but just in case
            duration < MINUTE_MS -> "just now"
            duration < HOUR_MS -> {
                val minutes = (duration / MINUTE_MS).toInt()
                "$minutes ${if (minutes == 1) "minute" else "minutes"} ago"
            }
            duration < DAY_MS -> {
                val hours = (duration / HOUR_MS).toInt()
                if (hours < 24) {
                    "$hours ${if (hours == 1) "hour" else "hours"} ago"
                } else {
                    "yesterday"
                }
            }
            duration < WEEK_MS -> {
                val days = (duration / DAY_MS).toInt()
                if (days == 1) "yesterday" else "$days days ago"
            }
            duration < MONTH_MS -> {
                val weeks = (duration / WEEK_MS).toInt()
                "$weeks ${if (weeks == 1) "week" else "weeks"} ago"
            }
            duration < YEAR_MS -> {
                val months = (duration / MONTH_MS).toInt()
                "$months ${if (months == 1) "month" else "months"} ago"
            }
            else -> {
                val years = (duration / YEAR_MS).toInt()
                "$years ${if (years == 1) "year" else "years"} ago"
            }
        }
    }

    // Time constants in milliseconds
    private const val SECOND_MS = 1000L
    private const val MINUTE_MS = 60 * SECOND_MS
    private const val HOUR_MS = 60 * MINUTE_MS
    private const val DAY_MS = 24 * HOUR_MS
    private const val WEEK_MS = 7 * DAY_MS
    private const val MONTH_MS = 30 * DAY_MS
    private const val YEAR_MS = 365 * DAY_MS
}
