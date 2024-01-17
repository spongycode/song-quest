package com.spongycode.songquest.screen.gameplay.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.gameplay.leaderboard.components.CustomDropDownMenu
import com.spongycode.songquest.screen.gameplay.profile.components.Topbar
import com.spongycode.songquest.util.CategoryConvertor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Leaderboard")
    }) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomDropDownMenu(categories = CategoryConvertor.giveAllCategories())
            Text(text = "LIST AREA")
            Text(text = "LIST AREA")
        }
    }
}