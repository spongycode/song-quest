package com.spongycode.songquest.data.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.util.Constants.BASE_URL
import com.spongycode.songquest.util.Network.executeWithRetry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): ApiResponse<AuthModel>? {
        return executeWithRetry {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/register")
                contentType(ContentType.Application.Json)
                setBody(
                    UserModel(
                        username = username,
                        email = email,
                        password = password
                    )
                )
            }
            res.body<ApiResponse<AuthModel>>()
        }
    }

    override suspend fun login(
        emailOrUsername: String,
        password: String
    ): ApiResponse<AuthModel>? {
        return executeWithRetry {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/login")
                contentType(ContentType.Application.Json)
                setBody(
                    UserModel(
                        username = emailOrUsername,
                        email = emailOrUsername,
                        password = password
                    )
                )
            }
            res.body<ApiResponse<AuthModel>>()
        }
    }

    override suspend fun forgotPasswordEmail(
        email: String
    ): ApiResponse<UserModel>? {
        return executeWithRetry {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/resetpassword")
                contentType(ContentType.Application.Json)
                setBody(
                    UserModel(
                        email = email
                    )
                )
            }
            res.body<ApiResponse<UserModel>>()
        }
    }

    override suspend fun changePassword(userModel: UserModel): ApiResponse<UserModel>? {
        return executeWithRetry {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/password")
                contentType(ContentType.Application.Json)
                setBody(userModel)
            }
            res.body<ApiResponse<UserModel>>()
        }
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): ApiResponse<AuthModel>? {
        return executeWithRetry {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/refreshtoken")
                contentType(ContentType.Application.Json)
                setBody(
                    UserModel(
                        refreshToken = refreshToken
                    )
                )
            }
            res.body<ApiResponse<AuthModel>>()
        }
    }

    override suspend fun updateProfile(userModel: UserModel): ApiResponse<AuthModel>? {
        return executeWithRetry {
            val res = client.patch {
                url("${BASE_URL}api/mobile/users/profile")
                contentType(ContentType.Application.Json)
                setBody(userModel)
            }
            res.body<ApiResponse<AuthModel>>()
        }
    }
}