package com.spongycode.songquest.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.HistoryModel
import com.spongycode.songquest.data.model.gameplay.LeaderboardModel
import com.spongycode.songquest.data.model.gameplay.PlayingModel
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Network
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GameplayRepositoryImpl(
    private val client: HttpClient
) : GameplayRepository {
    override suspend fun createGame(createGameModel: CreateGameModel): ApiResponse<PlayingModel>? {
        return Network.executeWithRetry {
            val res = client.post {
                url("${Constants.BASE_URL}api/mobile/gameplay/create")
                contentType(ContentType.Application.Json)
                setBody(createGameModel)
            }
            res.body<ApiResponse<PlayingModel>>()
        }
    }

    override suspend fun getQuestions() {
        TODO("Not yet implemented")
    }

    override suspend fun checkAnswer(checkAnswerModel: CheckAnswerModel): ApiResponse<CheckAnswerModel>? {
        return Network.executeWithRetry {
            val res = client.post {
                url("${Constants.BASE_URL}api/mobile/gameplay/check")
                contentType(ContentType.Application.Json)
                setBody(checkAnswerModel)
            }
            res.body<ApiResponse<CheckAnswerModel>>()
        }
    }

    override suspend fun saveGame(checkAnswerModel: CheckAnswerModel): ApiResponse<PlayingModel>? {
        return Network.executeWithRetry {
            val res = client.post {
                url("${Constants.BASE_URL}api/mobile/gameplay/save")
                contentType(ContentType.Application.Json)
                setBody(checkAnswerModel)
            }
            res.body<ApiResponse<PlayingModel>>()
        }
    }

    override suspend fun history(authModel: AuthModel): ApiResponse<HistoryModel>? {
        return Network.executeWithRetry {
            val res = client.post {
                url("${Constants.BASE_URL}api/mobile/gameplay/history")
                contentType(ContentType.Application.Json)
                setBody(authModel)
            }
            res.body<ApiResponse<HistoryModel>>()
        }
    }

    override suspend fun leaderboard(authModel: AuthModel): ApiResponse<List<LeaderboardModel>>? {
        return Network.executeWithRetry {
            val res = client.post {
                url("${Constants.BASE_URL}api/mobile/gameplay/highscore")
                contentType(ContentType.Application.Json)
                setBody(authModel)
            }
            res.body<ApiResponse<List<LeaderboardModel>>>()
        }
    }
}