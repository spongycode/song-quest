package com.spongycode.songquest.data.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : AuthRepository {
    private val BASE_URL = "https://songquest.vercel.app/"

    override suspend fun register(
        fullName: String, username: String,
        email: String, password: String
    ): ApiResponse<UserModel>? {
        return try {
            val res = client.post {
                url("${BASE_URL}api/mobile/users/register")
                contentType(ContentType.Application.Json)
                setBody(
                    UserModel(
                        fullName = fullName,
                        username = username,
                        email = email,
                        password = password
                    )
                )
            }
            res.body<ApiResponse<UserModel>>()
        } catch (err: Exception) {
            null
        }
    }

    override suspend fun login(
        emailOrUsername: String,
        password: String
    ): ApiResponse<UserModel>? {
        return try {
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
            res.body<ApiResponse<UserModel>>()
        } catch (err: Exception) {
            null
        }
    }

    override suspend fun forgotPasswordEmail(email: String): ApiResponse<UserModel>? {
        return try {
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
        } catch (err: Exception) {
            null
        }
    }
}


