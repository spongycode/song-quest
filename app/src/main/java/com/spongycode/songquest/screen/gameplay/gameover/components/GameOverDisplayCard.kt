package com.spongycode.songquest.screen.gameplay.gameover.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.R
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.screen.gameplay.gameover.GameOverState
import com.spongycode.songquest.ui.theme.OptionDarkBlue
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.LARGE_HEIGHT
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT

@Composable
fun GameOverDisplayCard(
    gameOverState: GameOverState,
    game: GameModel,
    username: String
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
            .width(250.dp)
            .height(350.dp)
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
                .background(Color.White)
        ) {
            when (gameOverState) {
                GameOverState.Error -> Text(text = "Error Saving the game...")
                GameOverState.Loading -> Text(text = "Saving the game...")
                GameOverState.Success -> {
                    Box {
                        Image(
                            painter = painterResource(
                                id = when (game.category) {
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
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            alpha = 0.5f
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0x8D0C0C0C))
                                .fillMaxSize(),
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(SMALL_HEIGHT))
                                Image(
                                    modifier = Modifier
                                        .size(LARGE_HEIGHT)
                                        .clip(CircleShape)
                                        .background(Color.Red),
                                    painter = painterResource(id = R.drawable.ic_launcher_background),
                                    contentDescription = null
                                )
                                Text(
                                    text = "@$username",
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.background
                                )

                                Text(
                                    text = game.score?.toInt().toString(),
                                    fontSize = 50.sp,
                                    color = OptionDarkBlue,
                                    fontWeight = FontWeight.W800
                                )
                            }

                            Text(
                                modifier = Modifier.padding(bottom = 20.dp),
                                text = when (game.category) {
                                    Constants.BOLLYWOOD_CODE -> Constants.BOLLYWOOD_DISPLAY_TEXT
                                    Constants.HOLLYWOOD_CODE -> Constants.HOLLYWOOD_DISPLAY_TEXT
                                    Constants.DESI_HIP_HOP_CODE -> Constants.DESI_HIP_HOP_DISPLAY_TEXT
                                    else -> Constants.HIP_HOP_DISPLAY_TEXT
                                },
                                fontSize = 25.sp,
                                fontWeight = FontWeight.W600,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }
            }
        }
    }
}
