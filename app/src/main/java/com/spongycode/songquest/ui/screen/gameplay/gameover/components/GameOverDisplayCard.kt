package com.spongycode.songquest.ui.screen.gameplay.gameover.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.R
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Fonts

@Composable
fun GameOverDisplayCard(
    game: GameModel,
    username: String
) {
    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp) * 3 / 4
    val height = configuration.screenHeightDp / 2
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
            .width(width.dp)
            .height(height.dp)
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier.background(Color.Red),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    modifier = Modifier
                        .background(Color.Red),
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
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF000000)),
                                startY = 0f,
                                endY = 200f
                            )
                        )
                        .padding(30.dp)
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
                        Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Score: ", fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Fonts.poppinsFamily,
                                color = Color.White
                            )
                            Text(
                                text = game.score?.toInt().toString(),
                                fontSize = 30.sp,
                                color = Color.White,
                                fontWeight = FontWeight.W600,
                                fontFamily = Fonts.poppinsFamily
                            )
                        }
                        Text(
                            text = "@$username",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.poppinsFamily
                        )
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "# ", fontSize = 25.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = Fonts.poppinsFamily,
                            color = Color.White
                        )

                        Text(
                            text = when (game.category) {
                                Constants.BOLLYWOOD_CODE -> Constants.BOLLYWOOD_DISPLAY_TEXT
                                Constants.HOLLYWOOD_CODE -> Constants.HOLLYWOOD_DISPLAY_TEXT
                                Constants.DESI_HIP_HOP_CODE -> Constants.DESI_HIP_HOP_DISPLAY_TEXT
                                else -> Constants.HIP_HOP_DISPLAY_TEXT
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = Fonts.poppinsFamily,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewGameOverDisplayCard() {
    GameOverDisplayCard(
        game = GameModel().dummy(),
        username = "dummy_user"
    )
}