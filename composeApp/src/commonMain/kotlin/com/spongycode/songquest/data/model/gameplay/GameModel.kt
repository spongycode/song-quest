package com.spongycode.songquest.data.model.gameplay

import com.spongycode.songquest.util.Constants.HOLLYWOOD_CODE
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
) {
    fun dummy(): GameModel = GameModel(
        _id = "123",
        player = "user 1",
        score = 12f,
        questionsId = listOf("123", "123"),
        category = HOLLYWOOD_CODE,
        isGameSaved = true,
        accurate = 12,
        createdAt = "2024-12-03T10:07:31.075Z"
    )
}