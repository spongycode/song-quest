package com.spongycode.songquest.di

import com.spongycode.songquest.repository.AuthRepositoryImpl
import com.spongycode.songquest.repository.SettingsRepository
import com.spongycode.songquest.repository.SettingsRepositoryImpl
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordViewModel
import com.spongycode.songquest.ui.screen.auth.login.LoginViewModel
import com.spongycode.songquest.ui.screen.auth.register.RegisterViewModel
import com.spongycode.songquest.ui.screen.gameplay.home.HomeViewModel
import com.spongycode.songquest.ui.screen.starter.SplashViewModel
import com.spongycode.songquest.util.BasePreferenceManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class AppPreferenceManager : BasePreferenceManager()

object AppContainer {
    val client: HttpClient by lazy { provideClient() }

    lateinit var settingsRepository: SettingsRepository

    val splashViewModel by lazy { SplashViewModel(authRepository, settingsRepository) }
    val registerViewModel by lazy { RegisterViewModel(authRepository, settingsRepository) }
    val loginViewModel by lazy { LoginViewModel(authRepository, settingsRepository) }
    val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(authRepository) }
    val homeViewModel by lazy { HomeViewModel(settingsRepository) }
//    val playingViewModel by lazy { PlayingViewModel(gameplayRepository, datastoreRepository) }
//    val gameOverViewModel by lazy { GameOverViewModel(datastoreRepository, gameplayRepository) }
//    val profileViewModel by lazy { ProfileViewModel(authRepository, datastoreRepository) }
//    val historyViewModel by lazy { HistoryViewModel(gameplayRepository, datastoreRepository) }
//    val leaderboardViewModel by lazy {
//        LeaderboardViewModel(
//            datastoreRepository,
//            gameplayRepository
//        )
//    }
//    private val gameplayRepository by lazy { GameplayRepositoryImpl(client) }
    private val authRepository by lazy { AuthRepositoryImpl(client) }

    fun init() {
        settingsRepository = SettingsRepositoryImpl(AppPreferenceManager())
    }

    private fun provideClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("http $message")
                }
            }
            level = LogLevel.ALL
        }
    }
}
