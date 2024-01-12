package com.spongycode.songquest.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.PREFERENCES_NAME
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DatastoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DatastoreRepository {

    override suspend fun getToken(key: Preferences.Key<String>): String? {
        val preferences = context.preferencesDataStore.data.first()
        val tokenExists = preferences[key] != null
        return if (tokenExists) {
            preferences[key] ?: ""
        } else {
            null
        }
    }

    override suspend fun storeToken(key: Preferences.Key<String>, value: String) {
        context.preferencesDataStore.edit { setting ->
            setting[key] = value
        }
    }

    companion object {
        private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
            name = PREFERENCES_NAME
        )
        val accessTokenSession = stringPreferencesKey(Constants.ACCESS_TOKEN_SESSION)
        val refreshTokenSession = stringPreferencesKey(Constants.REFRESH_TOKEN_SESSION)
    }
}