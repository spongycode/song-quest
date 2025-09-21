package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class PlayingModel(
    val game: GameModel? = null,
    val gamesPlayed: Int? = null,
    val isMoreQuestion: Boolean? = null,
    val questions: List<QuestionModel>? = null
)