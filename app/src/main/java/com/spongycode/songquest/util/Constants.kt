package com.spongycode.songquest.util

import androidx.compose.ui.unit.dp

object Constants {

    const val BASE_URL = "https://songquest.vercel.app/"

    const val PREFERENCES_NAME = "authentication"

    const val ACCESS_TOKEN_SESSION = "ACCESS_TOKEN_SESSION"
    const val REFRESH_TOKEN_SESSION = "REFRESH_TOKEN_SESSION"
    const val USERNAME_SESSION = "USERNAME_SESSION"
    const val EMAIL_SESSION = "EMAIL_SESSION"
    const val GAMES_PLAYED_SESSION = "GAMES_PLAYED_SESSION"

    val VERY_SMALL_HEIGHT = 8.dp
    val SMALL_HEIGHT = 15.dp
    val MEDIUM_HEIGHT = 40.dp
    val LARGE_HEIGHT = 60.dp
    val VERY_LARGE_HEIGHT = 100.dp

    const val CORNER_RADIUS_PERCENTAGE = 20

    const val BOLLYWOOD_DISPLAY_TEXT = "Bollywood"
    const val HOLLYWOOD_DISPLAY_TEXT = "Hollywood"
    const val DESI_HIP_HOP_DISPLAY_TEXT = "Desi Hip Hop"
    const val HIP_HOP_DISPLAY_TEXT = "Hip Hop"

    const val BOLLYWOOD_CODE = "BOLLYWOOD"
    const val HOLLYWOOD_CODE = "HOLLYWOOD"
    const val DESI_HIP_HOP_CODE = "DESI_HIP_HOP"
    const val HIP_HOP_CODE = "HIP_HOP"

    const val TOTAL_CHANCE = 3
    const val TIME_PER_QUESTION = 20

    const val CIRCULAR_TIMER_RADIUS = 100

    const val USERNAME_MIN_LENGTH = 4
    const val USERNAME_MAX_LENGTH = 15

    // screen route names
    const val SPLASH_SCREEN = "splash"
    const val REGISTER_SCREEN = "register"
    const val LOGIN_SCREEN = "login"
    const val HOME_SCREEN = "home"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password"
    const val LEADERBOARD_SCREEN = "leaderboard"
    const val PLAYING_SCREEN = "playing"
    const val PROFILE_SCREEN = "profile"
    const val HISTORY_SCREEN = "history"
    const val GAME_OVER_SCREEN = "game_over"
    const val CATEGORY = "category"
    const val GAME_ID = "game_id"
}