package com.spongycode.songquest.data.repository

import android.util.Log
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
            val res1 =  res.body<ApiResponse<UserModel>>()
            Log.d("MAINVIEWMIDEL", res1.toString())
            return res1
        } catch (err: Exception) {
            Log.d("MAINVIEWMIDEL", err.message.toString())
            null
        }
    }
}


