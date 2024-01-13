package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class CreateGameModel(
    val accessToken: String,
    val count: Int,
    val category: String
)
