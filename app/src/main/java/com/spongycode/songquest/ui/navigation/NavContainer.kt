package com.spongycode.songquest.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordScreenRoot
import com.spongycode.songquest.ui.screen.auth.login.LoginScreenRoot
import com.spongycode.songquest.ui.screen.auth.register.RegisterScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.gameover.GameOverScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.history.HistoryScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.home.HomeScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.LeaderboardScreen
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingScreen
import com.spongycode.songquest.ui.screen.gameplay.profile.ProfileScreen
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavContainer(startDestination: String) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(route = SPLASH_SCREEN) {
                SplashScreenRoot()
            }
            composable(route = REGISTER_SCREEN) {
                RegisterScreenRoot()
            }
            composable(route = LOGIN_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                    )
                }
            ) {
                LoginScreenRoot()
            }
            composable(route = FORGOT_PASSWORD_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                    )
                }
            ) {
                ForgotPasswordScreenRoot()
            }
            composable(route = HOME_SCREEN) {
                HomeScreenRoot()
            }
            composable(route = "$PLAYING_SCREEN/{$CATEGORY}") {
                val category = it.arguments?.getString(CATEGORY)
                PlayingScreen(category = category!!)
            }
            composable(route = "$GAME_OVER_SCREEN/{$GAME_ID}") {
                val gameId = it.arguments?.getString(GAME_ID)
                GameOverScreenRoot(gameId = gameId!!)
            }
            composable(route = PROFILE_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(100)
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(100)
                    )
                }) {
                ProfileScreen()
            }
            composable(route = HISTORY_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(100)
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(100)
                    )
                }) {
                HistoryScreenRoot()
            }
            composable(route = LEADERBOARD_SCREEN,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(100)
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(100)
                    )
                }) {
                LeaderboardScreen()
            }
        }
    }
}