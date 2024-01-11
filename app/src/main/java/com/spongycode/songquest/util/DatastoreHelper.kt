package com.spongycode.songquest.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.datastore by preferencesDataStore("authentication")
suspend fun DataStore<Preferences>.storeAccessToken(token: String) {
    val accessToken = stringPreferencesKey("ACCESS_TOKEN")
    edit { settings ->
        settings[accessToken] = token
    }
}

suspend fun DataStore<Preferences>.getAccessToken(): String {
    val accessToken = stringPreferencesKey("ACCESS_TOKEN")
    val preferences = data.first()
    val accessTokenExists = preferences[accessToken] != null
    return if (accessTokenExists) {
        preferences[accessToken] ?: ""
    } else {
        ""
    }
}