package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class GameModel(
    val _id: String? = null,
    val player: String? = null,
    val score: Float? = null,
    val questionsId: List<String> = emptyList(),
    val category: String? = null,
    val isGameSaved: Boolean? = null,
    val accurate: Int? = null,
    val createdAt: String? = null
)