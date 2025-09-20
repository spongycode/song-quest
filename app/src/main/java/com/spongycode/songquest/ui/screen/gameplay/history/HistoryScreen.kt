package com.spongycode.songquest.ui.screen.gameplay.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.ui.screen.gameplay.history.components.HistoryList
import com.spongycode.songquest.ui.screen.gameplay.profile.components.Topbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel
) {
    val navController = LocalNavController.current
    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Your games")
    }) {
        HistoryScreen(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState = HistoryUiState(),
    onEvent: (HistoryEvent) -> Unit = {}
) {
    LaunchedEffect(null) {
        onEvent(HistoryEvent.FetchHistoryGames)
    }
    when (uiState.historyState) {
        HistoryState.Error -> PlaceholderMessageText("Oops, some error occurred.")
        HistoryState.Loading -> PlaceholderMessageText("Loading your latest games..")
        HistoryState.Success -> HistoryList(modifier = modifier, games = uiState.games)
    }
}

@Preview
@Composable
private fun PreviewHistoryScreenSuccess() {
    HistoryScreen(
        uiState = HistoryUiState(
            games = listOf(
                GameModel().dummy(),
                GameModel().dummy(),
                GameModel().dummy(),
            ),
            historyState = HistoryState.Success
        )
    )
}

@Preview
@Composable
private fun PreviewHistoryScreenLoading() {
    HistoryScreen(uiState = HistoryUiState(historyState = HistoryState.Loading))
}

@Preview
@Composable
private fun PreviewHistoryScreenError() {
    HistoryScreen(uiState = HistoryUiState(historyState = HistoryState.Error))
}