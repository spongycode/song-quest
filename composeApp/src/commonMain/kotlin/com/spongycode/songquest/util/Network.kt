package com.spongycode.songquest.util

import kotlinx.coroutines.delay
import kotlinx.io.IOException
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
                println("http Retry attempt $attempt after IOException: ${err.message}")
                delay((500L * 2.0.pow(attempt)).toLong())
            } catch (err: Exception) {
                println("http Non-IOException error: ${err.message}")
                break
            }
        }

        println("http Request failed after $maxRetries attempts: ${lastException?.message}")
        return null
    }
}