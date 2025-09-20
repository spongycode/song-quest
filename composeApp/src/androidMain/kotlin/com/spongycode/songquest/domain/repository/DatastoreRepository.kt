package com.spongycode.songquest.domain.repository

import androidx.datastore.preferences.core.Preferences

interface DatastoreRepository {
    suspend fun getString(key: Preferences.Key<String>): String?
    suspend fun storeString(key: Preferences.Key<String>, value: String)
    suspend fun storeListString(list: List<Pair<Preferences.Key<String>, String>>)
}