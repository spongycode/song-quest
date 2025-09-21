package com.spongycode.songquest.ui.screen.gameplay.gameover

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.PlatformContext
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.share
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.gameplay.components.PlaceholderMessageText
import com.spongycode.songquest.ui.screen.gameplay.gameover.components.GameOverDisplayCard
import com.spongycode.songquest.ui.theme.OptionDarkBlue
import com.spongycode.songquest.ui.theme.OptionDarkGreen
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.PLAYING_SCREEN
import com.spongycode.songquest.util.defaultFontFamily
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GameOverScreenRoot(
    gameId: String,
    viewModel: GameOverViewModel
) {
    val navController = LocalNavController.current
    LaunchedEffect(null) {
        viewModel.onEvent(GameOverEvent.GetData)
        viewModel.onEvent(GameOverEvent.SaveGame(gameId))
        viewModel.viewEffect.collectLatest {
            when (it) {
                is GameOverViewEffect.Navigate -> {
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    if (it.navigateUp) {
                        navController.navigateUp()
                    }
                    it.route?.let { route ->
                        navController.navigate(route)
                    }
                }
            }
        }
    }
    GameOverScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun GameOverScreen(
    modifier: Modifier = Modifier,
    uiState: GameOverUiState = GameOverUiState(),
    onEvent: (GameOverEvent) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier.padding(vertical = Constants.MEDIUM_HEIGHT),
            text = "Game Over",
            fontSize = 30.sp,
            fontWeight = FontWeight.W600,
            fontFamily = defaultFontFamily,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.gameOverState) {
                GameOverState.Error -> PlaceholderMessageText(text = "Oops, some error occurred.")
                GameOverState.Loading -> PlaceholderMessageText(text = "Saving your game..")
                GameOverState.Success -> {
                    GameOverDisplayCard(
                        modifier = Modifier
                            .drawWithContent {
                                graphicsLayer.record {
                                    this@drawWithContent.drawContent()
                                }
                                drawLayer(graphicsLayer)
                            },
                        game = uiState.game,
                        username = uiState.username
                    )

                    Column {
                        CustomButton(
                            onClick = {
                                coroutineScope.launch {
                                    graphicsLayer.toImageBitmap().share(
                                        PlatformContext,
                                        uiState.game.score?.toInt(),
                                        uiState.game.category
                                    )
                                }
                            },
                            containerColor = OptionDarkGreen,
                            contentColor = Color.White,
                            displayText = "SHARE  🚀"
                        )
                        Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))

                        CustomButton(
                            onClick = {
                                onEvent(
                                    GameOverEvent.Navigate(
                                        route = "$PLAYING_SCREEN/${uiState.game.category}",
                                        navigateUp = true,
                                        popBackStack = false
                                    )
                                )
                            },
                            containerColor = OptionDarkBlue,
                            contentColor = Color.White,
                            displayText = "RESTART"
                        )

                        Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))

                        CustomButton(
                            onClick = {
                                onEvent(
                                    GameOverEvent.Navigate(
                                        navigateUp = true,
                                        popBackStack = false
                                    )
                                )
                            },
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

@Preview
@Composable
private fun PreviewGameOverScreenSuccess() {
    GameOverScreen(
        uiState = GameOverUiState(
            username = "dummy_user",
            game = GameModel().dummy(),
            gameOverState = GameOverState.Success
        )
    )
}

@Preview
@Composable
private fun PreviewGameOverScreenLoading() {
    GameOverScreen(
        uiState = GameOverUiState(
            gameOverState = GameOverState.Loading
        )
    )
}

@Preview
@Composable
private fun PreviewGameOverScreenError() {
    GameOverScreen(
        uiState = GameOverUiState(
            gameOverState = GameOverState.Error
        )
    )
}