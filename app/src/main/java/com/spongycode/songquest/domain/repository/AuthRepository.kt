package com.spongycode.songquest.domain.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.auth.UserModel

interface AuthRepository {
    suspend fun register(
        fullName: String, username: String,
        email: String, password: String
    ): ApiResponse<AuthModel>?

    suspend fun login(
        emailOrUsername: String, password: String
    ): ApiResponse<AuthModel>?

    suspend fun forgotPasswordEmail(
        email: String
    ): ApiResponse<UserModel>?

    suspend fun refreshToken(
        refreshToken: String
    ): ApiResponse<AuthModel>?
}