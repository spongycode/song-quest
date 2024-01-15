package com.spongycode.songquest.screen.gameplay.gameover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.gameplay.gameover.components.GameOverDisplayCard

@Composable
fun GameOverScreen(
    gameId: String,
    navController: NavHostController,
    viewModel: GameOverViewModel = hiltViewModel()
) {

    viewModel.saveGame(gameId)

    val game = viewModel.game.value
    val gameOverState = viewModel.gameOverState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "Game Over")
        GameOverDisplayCard(gameOverState, game, viewModel.username.value)
    }
}