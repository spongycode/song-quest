package com.spongycode.songquest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)