package com.spongycode.songquest.util

import android.util.Log
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.math.pow

object Network {
    suspend fun <T> executeWithRetry(
        maxRetries: Int = 3,
        block: suspend () -> T
    ): T? {
        var attempt = 0
        var lastException: IOException? = null

        while (attempt < maxRetries) {
            try {
                return block()
            } catch (err: IOException) {
                lastException = err
                attempt++
                Log.d("http", "Retry attempt $attempt after IOException: ${err.message}")
                delay((500L * 2.0.pow(attempt)).toLong())
            } catch (err: Exception) {
                Log.e("http", "Non-IOException error: ${err.message}")
                break
            }
        }

        Log.e("http", "Request failed after $maxRetries attempts: ${lastException?.message}")
        return null
    }
}