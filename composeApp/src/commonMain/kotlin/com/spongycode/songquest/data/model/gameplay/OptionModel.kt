package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class OptionModel(
    val _id: String? = null,
    val optionid: Int? = null,
    val value: String? = null,
)
