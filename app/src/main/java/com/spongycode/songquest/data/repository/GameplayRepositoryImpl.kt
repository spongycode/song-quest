package com.spongycode.songquest.data.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.PlayingModel
import com.spongycode.songquest.domain.repository.GameplayRepository
import com.spongycode.songquest.util.Constants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class GameplayRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : GameplayRepository {
    override suspend fun createGame(createGameModel: CreateGameModel): ApiResponse<PlayingModel>? {
        return try {
            val res = client.post {
                url("${BASE_URL}api/mobile/gameplay/create")
                contentType(ContentType.Application.Json)
                setBody(createGameModel)
            }
            res.body<ApiResponse<PlayingModel>>()
        } catch (err: Exception) {
            null
        }
    }

    override suspend fun getQuestions() {
        TODO("Not yet implemented")
    }

    override suspend fun checkAnswer(checkAnswerModel: CheckAnswerModel): ApiResponse<CheckAnswerModel>? {
        return try {
            val res = client.post {
                url("${BASE_URL}api/mobile/gameplay/check")
                contentType(ContentType.Application.Json)
                setBody(checkAnswerModel)
            }
            res.body<ApiResponse<CheckAnswerModel>>()
        } catch (err: Exception) {
            null
        }
    }

    override suspend fun saveGame() {
        TODO("Not yet implemented")
    }
}