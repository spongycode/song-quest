package com.spongycode.songquest.ui.screen.gameplay.leaderboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.components.CustomDropDownMenu
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.components.CustomTableList
import com.spongycode.songquest.ui.screen.gameplay.profile.components.Topbar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val selectedCategory = viewModel.selectedCategory
    val selectedLeaderboardList = viewModel.leaderboardDatabase[selectedCategory.value]

    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Leaderboard")
    }) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding() + 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomDropDownMenu()
            when (viewModel.leaderboardState.value) {
                LeaderboardState.Error -> PlaceholderMessageText("Oops, some error occurred.")
                LeaderboardState.Loading -> PlaceholderMessageText("Loading latest leaderboard..")
                LeaderboardState.Success -> {
                    selectedLeaderboardList?.let {
                        CustomTableList(listItems = selectedLeaderboardList, topPadding = 0.dp)
                    }
                }
            }
        }
    }
}