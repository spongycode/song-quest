package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardModel(
    val category: String = "MIX",
    val users: List<LeaderboardUsersModel>
)

@Serializable
data class LeaderboardUsersModel(
    val username: String,
    val score: Float,
    val accurate: Int,
    val createdAt: String? = null
)
