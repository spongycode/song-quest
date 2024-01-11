package com.spongycode.songquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.songquest.screen.HomeScreen
import com.spongycode.songquest.screen.auth.login.LoginScreen
import com.spongycode.songquest.screen.auth.register.RegisterScreen

@Composable
fun NavContainer(startDestination: String) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "register") {
            RegisterScreen(navController = navController)
        }
        composable(route = "login") {
            LoginScreen(navController = navController)
        }
        composable(route = "home") {
            HomeScreen(navController = navController)
        }
    }
}