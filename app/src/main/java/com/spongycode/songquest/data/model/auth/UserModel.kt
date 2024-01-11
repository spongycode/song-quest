package com.spongycode.songquest.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val _id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val password: String? = null
)