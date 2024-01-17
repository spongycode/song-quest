package com.spongycode.songquest.domain.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.HistoryModel
import com.spongycode.songquest.data.model.gameplay.PlayingModel

interface GameplayRepository {
    suspend fun createGame(createGameModel: CreateGameModel): ApiResponse<PlayingModel>?

    suspend fun getQuestions()

    suspend fun checkAnswer(checkAnswerModel: CheckAnswerModel): ApiResponse<CheckAnswerModel>?

    suspend fun saveGame(checkAnswerModel: CheckAnswerModel): ApiResponse<PlayingModel>?
    suspend fun history(authModel: AuthModel): ApiResponse<HistoryModel>?
    suspend fun leaderboard(authModel: AuthModel): ApiResponse<HistoryModel>?
}