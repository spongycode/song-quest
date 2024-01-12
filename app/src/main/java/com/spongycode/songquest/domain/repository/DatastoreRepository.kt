package com.spongycode.songquest.domain.repository

import androidx.datastore.preferences.core.Preferences

interface DatastoreRepository {
    suspend fun getToken(key: Preferences.Key<String>): String?

    suspend fun storeToken(key: Preferences.Key<String>, value: String)
}