package com.spongycode.songquest.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordScreen
import com.spongycode.songquest.ui.screen.auth.login.LoginScreen
import com.spongycode.songquest.ui.screen.auth.register.RegisterScreen
import com.spongycode.songquest.ui.screen.gameplay.gameover.GameOverScreen
import com.spongycode.songquest.ui.screen.gameplay.history.HistoryScreen
import com.spongycode.songquest.ui.screen.gameplay.home.HomeScreen
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.LeaderboardScreen
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingScreen
import com.spongycode.songquest.ui.screen.gameplay.profile.ProfileScreen
import com.spongycode.songquest.ui.screen.starter.StarterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavContainer(startDestination: String) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "starter") {
            StarterScreen(navController = navController)
        }
        composable(route = "register") {
            RegisterScreen(navController = navController)
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
            LoginScreen(navController = navController)
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
            ForgotPasswordScreen(navController = navController)
        }
        composable(route = "home") {
            HomeScreen(navController = navController)
        }
        composable(route = "playing/{category}") {
            val category = it.arguments?.getString("category")
            PlayingScreen(category = category!!, navController = navController)
        }
        composable(route = "gameover/{gameId}") {
            val gameId = it.arguments?.getString("gameId")
            GameOverScreen(gameId = gameId!!, navController = navController)
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
            ProfileScreen(navController = navController)
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
            HistoryScreen(navController = navController)
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
            LeaderboardScreen(navController = navController)
        }
    }
}