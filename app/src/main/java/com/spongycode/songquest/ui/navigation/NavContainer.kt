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
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordScreen
import com.spongycode.songquest.ui.screen.auth.login.LoginScreen
import com.spongycode.songquest.ui.screen.auth.register.RegisterScreenRoot
import com.spongycode.songquest.ui.screen.gameplay.gameover.GameOverScreen
import com.spongycode.songquest.ui.screen.gameplay.history.HistoryScreen
import com.spongycode.songquest.ui.screen.gameplay.home.HomeScreen
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.LeaderboardScreen
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingScreen
import com.spongycode.songquest.ui.screen.gameplay.profile.ProfileScreen
import com.spongycode.songquest.ui.screen.starter.StarterScreen

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavContainer(startDestination: String) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(route = "starter") {
                StarterScreen()
            }
            composable(route = "register") {
                RegisterScreenRoot()
            }
            composable(route = "login",
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
                LoginScreen()
            }
            composable(route = "forgotpassword",
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
                ForgotPasswordScreen()
            }
            composable(route = "home") {
                HomeScreen()
            }
            composable(route = "playing/{category}") {
                val category = it.arguments?.getString("category")
                PlayingScreen(category = category!!)
            }
            composable(route = "gameover/{gameId}") {
                val gameId = it.arguments?.getString("gameId")
                GameOverScreen(gameId = gameId!!)
            }
            composable(route = "profile",
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
            composable(route = "history",
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
                HistoryScreen()
            }
            composable(route = "leaderboard",
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