package com.spongycode.songquest.screen.gameplay.gameover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.screen.auth.components.CustomButton
import com.spongycode.songquest.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.screen.gameplay.gameover.components.GameOverDisplayCard
import com.spongycode.songquest.ui.theme.OptionDarkBlue
import com.spongycode.songquest.ui.theme.OptionDarkGreen
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Fonts

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
        Text(
            modifier = Modifier.padding(vertical = Constants.MEDIUM_HEIGHT),
            text = "Game Over",
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            fontFamily = Fonts.poppinsFamily,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (gameOverState) {
                GameOverState.Error -> PlaceholderMessageText(text = "Oops, some error occurred.")
                GameOverState.Loading -> PlaceholderMessageText(text = "Saving your game..")
                GameOverState.Success -> {
                    GameOverDisplayCard(game, viewModel.username.value)

                    Column {
                        CustomButton(
                            onClick = {
                                navController.navigateUp()
                                navController.navigate("playing/${viewModel.game.value.category}")
                            },
                            containerColor = OptionDarkBlue,
                            contentColor = Color.White,
                            displayText = "RESTART"
                        )

                        Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))

                        CustomButton(
                            onClick = { navController.navigateUp() },
                            containerColor = OptionDarkGreen,
                            contentColor = Color.White,
                            displayText = "HOME"
                        )
                    }
                }
            }
        }
    }
}