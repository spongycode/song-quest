package com.spongycode.songquest.screen.gameplay.playing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.gameplay.playing.OptionTapState.Checking
import com.spongycode.songquest.screen.gameplay.playing.OptionTapState.CorrectAnswer
import com.spongycode.songquest.screen.gameplay.playing.OptionTapState.Idle
import com.spongycode.songquest.screen.gameplay.playing.OptionTapState.WrongAnswer
import com.spongycode.songquest.screen.gameplay.playing.components.OptionField
import com.spongycode.songquest.ui.theme.OptionDarkBlue
import com.spongycode.songquest.ui.theme.OptionDarkRed
import com.spongycode.songquest.ui.theme.OptionDarkYellow
import com.spongycode.songquest.ui.theme.OptionLightBlue
import com.spongycode.songquest.ui.theme.OptionLightRed
import com.spongycode.songquest.ui.theme.OptionLightYellow
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT
import com.spongycode.songquest.util.Constants.TOTAL_CHANCE
import kotlin.math.min

@Composable
fun PlayingScreen(
    category: String,
    navController: NavHostController,
    viewModel: PlayingViewModel = hiltViewModel()
) {
    val currentSongIndex = viewModel.currentSongIndex.intValue
    val optionTapState = viewModel.optionTapState.value
    val createGameState = viewModel.createGameState.value
    val totalLife = viewModel.totalLife.intValue
    val currentScore = viewModel.currentScore.value
    val isGameOver = viewModel.isGameOver.value

    LaunchedEffect(category) {
        viewModel.createGame(category)
    }

    if (isGameOver) {
        navController.popBackStack()
        navController.navigate("gameover/${viewModel.game.value._id}")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        when (createGameState) {
            CreateGameState.Error -> Text(text = "Error fetching question")
            CreateGameState.Fetching -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(top = 200.dp),
                        text = "Loading questions..",
                        fontSize = 35.sp,
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.W800
                    )
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Red),
                        painter = painterResource(
                            id = when (category) {
                                Constants.BOLLYWOOD_CODE -> {
                                    R.drawable.bollywood_banner
                                }

                                Constants.HOLLYWOOD_CODE -> {
                                    R.drawable.hollywood_banner
                                }

                                Constants.DESI_HIP_HOP_CODE -> {
                                    R.drawable.desi_hip_hop_banner
                                }

                                else -> {
                                    R.drawable.hip_hop_banner
                                }
                            }
                        ), contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            CreateGameState.Success -> {
                if (currentSongIndex < viewModel.questions.size) {
                    LazyRow {
                        repeat(totalLife) {
                            item {
                                Icon(
                                    modifier = Modifier
                                        .size(2 * SMALL_HEIGHT)
                                        .padding(2.dp),
                                    painter = painterResource(id = R.drawable.heart),
                                    contentDescription = null,
                                    tint = OptionDarkRed
                                )
                            }
                        }
                        repeat(min(TOTAL_CHANCE, TOTAL_CHANCE - totalLife)) {
                            item {
                                Icon(
                                    modifier = Modifier
                                        .size(2 * SMALL_HEIGHT)
                                        .padding(2.dp),
                                    painter = painterResource(id = R.drawable.heart),
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }

                    Text(
                        text = "Score: ${currentScore.toInt()}",
                        fontWeight = W800,
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = viewModel.questions[currentSongIndex].title.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600
                    )

                    LaunchedEffect(currentSongIndex) {
                        viewModel.playCurrentSong()
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(viewModel.questions[currentSongIndex].options) { option ->
                            OptionField(
                                text = option.value!!,
                                onClick = {
                                    if ((optionTapState == Idle)) {
                                        viewModel.tapButton(option.optionid!!)
                                        viewModel.checkAnswer(option.optionid)
                                    }
                                },
                                fillColor = if (option.optionid!! != viewModel.tappedButtonId.intValue) Color.White else {
                                    when (optionTapState) {
                                        Checking -> OptionLightYellow
                                        CorrectAnswer -> OptionLightBlue
                                        Idle -> Color.White
                                        WrongAnswer -> OptionLightRed
                                    }
                                },
                                tint = if (option.optionid!! != viewModel.tappedButtonId.intValue) MaterialTheme.colorScheme.primary else {
                                    when (optionTapState) {
                                        Checking -> OptionDarkYellow
                                        CorrectAnswer -> OptionDarkBlue
                                        Idle -> MaterialTheme.colorScheme.primary
                                        WrongAnswer -> OptionDarkRed
                                    }
                                },
                                iconId = if (option.optionid!! != viewModel.tappedButtonId.intValue) null else {
                                    when (optionTapState) {
                                        Checking -> null
                                        CorrectAnswer -> R.drawable.baseline_check_circle_24
                                        Idle -> null
                                        WrongAnswer -> R.drawable.baseline_cancel_24
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(SMALL_HEIGHT))
                        }
                    }
                }
            }
        }
    }
}