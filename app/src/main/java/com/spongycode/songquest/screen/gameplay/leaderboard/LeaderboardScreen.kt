package com.spongycode.songquest.screen.gameplay.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.gameplay.leaderboard.components.CustomDropDownMenu
import com.spongycode.songquest.screen.gameplay.profile.components.Topbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val leaderboardDatabase = viewModel.leaderboardDatabase.values
    val selectedCategory = viewModel.selectedCategory
    val selectedLeaderboardList = viewModel.leaderboardDatabase[selectedCategory.value]

    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Leaderboard")
    }) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomDropDownMenu()
            selectedLeaderboardList?.let {
                LazyColumn {
                    items(selectedLeaderboardList) { user ->
                        Text(text = user.username)
                        Text(text = user.score.toInt().toString())
                    }
                }
            }
        }
    }
}