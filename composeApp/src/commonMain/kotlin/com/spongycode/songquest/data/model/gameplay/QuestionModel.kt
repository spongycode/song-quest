package com.spongycode.songquest.data.model.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class QuestionModel(
    val _id: String? = null,
    val title: String? = null,
    val songUrl: String? = null,
    val options: List<OptionModel>,
    val category: String? = null
)