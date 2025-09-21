package com.spongycode.songquest.repository

import com.spongycode.songquest.util.BasePreferenceManager

class SettingsRepositoryImpl(
    private val prefs: BasePreferenceManager
) : SettingsRepository {

    override suspend fun getString(key: String): String? {
        return prefs.getStringOrNull(key)
    }

    override suspend fun storeString(key: String, value: String) {
        prefs.putString(key, value)
    }

    override suspend fun storeListString(list: List<Pair<String, String>>) {
        list.forEach { (key, value) ->
            prefs.putString(key, value)
        }
    }

    companion object {
        const val ACCESS_TOKEN_SESSION = "ACCESS_TOKEN_SESSION"
        const val REFRESH_TOKEN_SESSION = "REFRESH_TOKEN_SESSION"
        const val USERNAME_SESSION = "USERNAME_SESSION"
        const val EMAIL_SESSION = "EMAIL_SESSION"
        const val GAMES_PLAYED_SESSION = "GAMES_PLAYED_SESSION"
    }
}
