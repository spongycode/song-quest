package com.spongycode.songquest.ui.screen.gameplay.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.ui.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.ui.screen.gameplay.history.components.CustomList
import com.spongycode.songquest.ui.screen.gameplay.profile.components.Topbar

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