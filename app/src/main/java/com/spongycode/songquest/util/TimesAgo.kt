package com.spongycode.songquest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimesAgo {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeAgo(time: String): String {
        val parts = time.split('Z')
        val serverTime = LocalDateTime.parse(parts[0])
        val serverZone = ZoneId.of("UTC")
        val localZone = ZoneId.systemDefault()
        val dateTime =
            serverTime.atZone(serverZone).withZoneSameInstant(localZone).toLocalDateTime()
        val duration = Duration.between(dateTime, LocalDateTime.now())

        return when {
            duration.toDays() > 2 -> {
                val pattern = "MMM d 'at' HH:mm"
                val formatter = DateTimeFormatter.ofPattern(pattern)
                dateTime.format(formatter)
            }

            duration.toDays() >= 1 -> if (duration.toDays() > 1) "${duration.toDays()} days" else "yesterday"
            duration.toHours() >= 1 -> "${duration.toHours()} hour${if (duration.toHours() > 1) "s" else ""} ago"
            duration.toMinutes() >= 1 -> "${duration.toMinutes()} min${if (duration.toMinutes() > 1) "s" else ""} ago"
            else -> "${duration.seconds} sec${if (duration.seconds > 1) "s" else ""} ago"
        }
    }
}