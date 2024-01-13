package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class CheckAnswerModel(
    val gameId: String? = null,
    val questionId: String? = null,
    val optionId: Int? = null,
    val timeTaken: Int? = null,
    val isCorrect: Boolean? = null,
    val increment: Float? = null
)