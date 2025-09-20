package com.spongycode.songquest.di

import android.content.Context
import android.util.Log
import com.spongycode.songquest.data.repository.AuthRepositoryImpl
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.data.repository.GameplayRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordViewModel
import com.spongycode.songquest.ui.screen.auth.login.LoginViewModel
import com.spongycode.songquest.ui.screen.auth.register.RegisterViewModel
import com.spongycode.songquest.ui.screen.gameplay.gameover.GameOverViewModel
import com.spongycode.songquest.ui.screen.gameplay.history.HistoryViewModel
import com.spongycode.songquest.ui.screen.gameplay.home.HomeViewModel
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.LeaderboardViewModel
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingViewModel
import com.spongycode.songquest.ui.screen.gameplay.profile.ProfileViewModel
import com.spongycode.songquest.ui.screen.starter.SplashViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.getValue


object AppContainer {
    val client: HttpClient by lazy { provideClient() }

    lateinit var datastoreRepository: DatastoreRepository

    val splashViewModel by lazy { SplashViewModel(authRepository, datastoreRepository) }
    val registerViewModel by lazy { RegisterViewModel(authRepository, datastoreRepository) }
    val loginViewModel by lazy { LoginViewModel(authRepository, datastoreRepository) }
    val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(authRepository) }
    val homeViewModel by lazy { HomeViewModel(datastoreRepository) }
    val playingViewModel by lazy { PlayingViewModel(gameplayRepository, datastoreRepository) }
    val gameOverViewModel by lazy { GameOverViewModel(datastoreRepository, gameplayRepository) }
    val profileViewModel by lazy { ProfileViewModel(authRepository, datastoreRepository) }
    val historyViewModel by lazy { HistoryViewModel(gameplayRepository, datastoreRepository) }
    val leaderboardViewModel by lazy {
        LeaderboardViewModel(
            datastoreRepository,
            gameplayRepository
        )
    }
    private val gameplayRepository by lazy { GameplayRepositoryImpl(client) }
    private val authRepository by lazy { AuthRepositoryImpl(client) }

    fun init(app: Context) {
        datastoreRepository = DatastoreRepositoryImpl(app)
    }

    private fun provideClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("http", message)
                }
            }
            level = LogLevel.ALL
        }
    }
}
