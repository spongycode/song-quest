package com.spongycode.songquest.repository

interface SettingsRepository {
    suspend fun getString(key: String): String?
    suspend fun storeString(key: String, value: String)
    suspend fun storeListString(list: List<Pair<String, String>>)
}
