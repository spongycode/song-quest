package com.spongycode.songquest.screen.gameplay.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.screen.gameplay.history.components.CustomList
import com.spongycode.songquest.screen.gameplay.profile.components.Topbar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val games = viewModel.games
    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Your games")
    }) {
        when (viewModel.historyState.value) {
            HistoryState.Error -> PlaceholderMessageText("Oops, some error occurred.")
            HistoryState.Loading -> PlaceholderMessageText("Loading your latest games..")
            HistoryState.Success -> CustomList(games = games, it.calculateTopPadding())
        }
    }
}