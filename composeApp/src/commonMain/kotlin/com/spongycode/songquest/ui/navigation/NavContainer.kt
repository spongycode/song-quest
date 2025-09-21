package com.spongycode.songquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import com.spongycode.songquest.di.AppContainer
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordScreenRoot
import com.spongycode.songquest.ui.screen.auth.login.LoginScreenRoot
import com.spongycode.songquest.ui.screen.auth.register.RegisterScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.gameover.GameOverScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.history.HistoryScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.home.HomeScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.LeaderboardScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.profile.ProfileScreenRoot
import com.spongycode.songquest.ui.screen.starter.SplashScreenRoot
import com.spongycode.songquest.util.Constants.BOLLYWOOD_CODE
import com.spongycode.songquest.util.Constants.CATEGORY
import com.spongycode.songquest.util.Constants.FORGOT_PASSWORD_SCREEN
import com.spongycode.songquest.util.Constants.GAME_ID
import com.spongycode.songquest.util.Constants.GAME_OVER_SCREEN
import com.spongycode.songquest.util.Constants.HISTORY_SCREEN
import com.spongycode.songquest.util.Constants.HOME_SCREEN
import com.spongycode.songquest.util.Constants.LEADERBOARD_SCREEN
import com.spongycode.songquest.util.Constants.LOGIN_SCREEN
import com.spongycode.songquest.util.Constants.PLAYING_SCREEN
import com.spongycode.songquest.util.Constants.PROFILE_SCREEN
import com.spongycode.songquest.util.Constants.REGISTER_SCREEN
import com.spongycode.songquest.util.Constants.SPLASH_SCREEN
import io.ktor.websocket.FrameType.Companion.get

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }

@Composable
fun NavContainer(
    startDestination: String,
    appContainer: AppContainer
) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController, startDestination) {
            composable(SPLASH_SCREEN) { SplashScreenRoot(appContainer.splashViewModel) }
            composable(REGISTER_SCREEN) { RegisterScreenRoot(viewModel = appContainer.registerViewModel) }
            composable(LOGIN_SCREEN) { LoginScreenRoot(viewModel = appContainer.loginViewModel) }
            composable(FORGOT_PASSWORD_SCREEN) { ForgotPasswordScreenRoot(viewModel = appContainer.forgotPasswordViewModel) }
            composable(HOME_SCREEN) { HomeScreenRoot(viewModel = appContainer.homeViewModel) }
            composable("$PLAYING_SCREEN/{$CATEGORY}") {
                val category = it.arguments?.read { getStringOrNull(CATEGORY) }
                PlayingScreenRoot(
                    category = category ?: BOLLYWOOD_CODE,
                    viewModel = appContainer.playingViewModel
                )
            }
            composable("$GAME_OVER_SCREEN/{$GAME_ID}") {
                GameOverScreenRoot(
                    gameId = it.arguments?.read { getStringOrNull(GAME_ID) }.toString(),
                    viewModel = appContainer.gameOverViewModel
                )
            }
            composable(PROFILE_SCREEN) { ProfileScreenRoot(viewModel = appContainer.profileViewModel) }
            composable(HISTORY_SCREEN) { HistoryScreenRoot(viewModel = appContainer.historyViewModel) }
            composable(LEADERBOARD_SCREEN) { LeaderboardScreenRoot(viewModel = appContainer.leaderboardViewModel) }
        }
    }
}