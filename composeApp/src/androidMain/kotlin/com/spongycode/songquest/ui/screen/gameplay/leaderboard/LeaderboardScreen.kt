package com.spongycode.songquest.ui.screen.gameplay.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.components.CustomDropDownMenu
import com.spongycode.songquest.ui.screen.gameplay.leaderboard.components.CustomTableList
import com.spongycode.songquest.ui.screen.gameplay.profile.components.Topbar
import com.spongycode.songquest.util.ComposeLocalWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreenRoot(
    viewModel: LeaderboardViewModel
) {
    val navController = LocalNavController.current
    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Leaderboard")
    }) {
        LeaderboardScreen(
            modifier = Modifier.padding(top = it.calculateTopPadding() + 10.dp),
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    uiState: LeaderboardUiState = LeaderboardUiState(),
    onEvent: (LeaderboardEvent) -> Unit = {}
) {
    LaunchedEffect(null) {
        onEvent(LeaderboardEvent.FetchLeaderboard)
    }
    val selectedLeaderboardList = uiState.leaderboardDatabase[uiState.selectedCategory]
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomDropDownMenu(
            selectedCategory = uiState.selectedCategory,
            onChangeCategory = { category -> onEvent(LeaderboardEvent.ChangeCategory(category)) }
        )
        when (uiState.leaderboardState) {
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

@Preview
@Composable
private fun PreviewLeaderboardScreen() {
    ComposeLocalWrapper {
        LeaderboardScreen()
    }
}