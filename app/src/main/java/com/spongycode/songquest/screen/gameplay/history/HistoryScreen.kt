package com.spongycode.songquest.screen.gameplay.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.gameplay.history.components.GameListItem
import com.spongycode.songquest.screen.gameplay.profile.components.Topbar
import com.spongycode.songquest.ui.theme.OptionLightBlue
import com.spongycode.songquest.ui.theme.OptionLightGreen

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
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    start = 10.dp,
                    end = 10.dp
                )
                .fillMaxWidth()
        ) {
            var index = 0
            items(games) { game ->
                GameListItem(
                    score = game.score?.toInt(),
                    category = game.category.toString(),
                    bgColor = if (index % 2 == 0) OptionLightBlue else OptionLightGreen
                )
                index++
            }
        }
    }
}
