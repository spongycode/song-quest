package com.spongycode.songquest.domain.repository

import com.spongycode.songquest.data.model.ApiResponse
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.PlayingModel

interface GameplayRepository {
    suspend fun createGame(createGameModel: CreateGameModel): ApiResponse<PlayingModel>?

    suspend fun getQuestions()

    suspend fun checkAnswer()

    suspend fun saveGame()
}