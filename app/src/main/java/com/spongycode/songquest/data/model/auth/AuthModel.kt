package com.spongycode.songquest.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthModel(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val user: UserModel? = null
)
