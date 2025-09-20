package com.spongycode.songquest.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object TimesAgo {
    fun getTimeAgo(time: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val serverDate = inputFormat.parse(time)

            val now = Calendar.getInstance()
            val serverTime = Calendar.getInstance()
            serverTime.time = serverDate!!

            val diffInMillis = now.timeInMillis - serverTime.timeInMillis
            val seconds = diffInMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                days > 2 -> {
                    val outputFormat = SimpleDateFormat("MMM d 'at' HH:mm", Locale.getDefault())
                    outputFormat.timeZone = TimeZone.getDefault()
                    outputFormat.format(serverDate)
                }

                days >= 1 -> if (days > 1) "$days days ago" else "yesterday"
                hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
                minutes >= 1 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
                else -> "$seconds sec${if (seconds > 1) "s" else ""} ago"
            }
        } catch (e: Exception) {
            return "just now"
        }
    }
}