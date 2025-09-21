package com.spongycode.songquest.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object TimesAgo {
    @OptIn(ExperimentalTime::class)
    fun getTimeAgo(time: String): String {
        return try {
            val serverInstant = Instant.parse(time)
            val nowInstant = Clock.System.now()
            val diff = nowInstant.epochSeconds - serverInstant.epochSeconds
            val seconds = diff
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                days > 2 -> {
                    val localDateTime =
                        serverInstant.toLocalDateTime(TimeZone.currentSystemDefault())
                    val hour = localDateTime.hour.toString().padStart(2, '0')
                    val minute = localDateTime.minute.toString().padStart(2, '0')
                    "${localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                            "${localDateTime.day} at $hour:$minute"
                }

                days >= 1 -> if (days > 1) "$days days ago" else "yesterday"
                hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
                minutes >= 1 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
                else -> "$seconds sec${if (seconds > 1) "s" else ""} ago"
            }
        } catch (e: Exception) {
            "just now"
        }
    }
}