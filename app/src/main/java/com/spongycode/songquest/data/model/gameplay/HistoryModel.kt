package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class HistoryModel(
    val games: List<GameModel>? = null
)