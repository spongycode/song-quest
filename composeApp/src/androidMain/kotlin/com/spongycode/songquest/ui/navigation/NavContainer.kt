package com.spongycode.songquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }
@Composable
fun NavContainer(
    startDestination: String,
    container: AppContainer
) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController, startDestination) {
            composable(SPLASH_SCREEN) { SplashScreenRoot(viewModel = container.splashViewModel) }
            composable(REGISTER_SCREEN) { RegisterScreenRoot(viewModel = container.registerViewModel) }
            composable(LOGIN_SCREEN) { LoginScreenRoot(viewModel = container.loginViewModel) }
            composable(FORGOT_PASSWORD_SCREEN) { ForgotPasswordScreenRoot(viewModel = container.forgotPasswordViewModel) }
            composable(HOME_SCREEN) { HomeScreenRoot(viewModel = container.homeViewModel) }
            composable("$PLAYING_SCREEN/{$CATEGORY}") { PlayingScreenRoot(category = it.arguments?.getString(CATEGORY)!!, viewModel = container.playingViewModel) }
            composable("$GAME_OVER_SCREEN/{$GAME_ID}") { GameOverScreenRoot(gameId = it.arguments?.getString(GAME_ID)!!, viewModel = container.gameOverViewModel) }
            composable(PROFILE_SCREEN) { ProfileScreenRoot(viewModel = container.profileViewModel) }
            composable(HISTORY_SCREEN) { HistoryScreenRoot(viewModel = container.historyViewModel) }
            composable(LEADERBOARD_SCREEN) { LeaderboardScreenRoot(viewModel = container.leaderboardViewModel) }
        }
    }
}