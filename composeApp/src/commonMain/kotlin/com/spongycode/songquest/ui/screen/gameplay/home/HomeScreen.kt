package com.spongycode.songquest.ui.screen.gameplay.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.home.components.CardInfo
import com.spongycode.songquest.ui.screen.gameplay.home.components.CategorySelector
import com.spongycode.songquest.ui.screen.gameplay.home.components.Header
import com.spongycode.songquest.ui.theme.OptionLightBlue
import com.spongycode.songquest.ui.theme.OptionLightGreen
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.HISTORY_SCREEN
import com.spongycode.songquest.util.Constants.LEADERBOARD_SCREEN
import com.spongycode.songquest.util.Constants.PLAYING_SCREEN
import com.spongycode.songquest.util.Constants.PROFILE_SCREEN
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT
import com.spongycode.songquest.util.defaultFontFamily
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.bollywood_banner
import song_quest.composeapp.generated.resources.desi_hip_hop_banner
import song_quest.composeapp.generated.resources.gameplay_count
import song_quest.composeapp.generated.resources.hip_hop_banner
import song_quest.composeapp.generated.resources.hollywood_banner
import song_quest.composeapp.generated.resources.leader_board_icon

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel
) {
    val navController = LocalNavController.current
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is HomeViewEffect.Navigate -> {
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    navController.navigate(it.route)
                }
            }
        }
    }
    HomeScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    onEvent: (HomeEvent) -> Unit = {},
) {
    LaunchedEffect(null) {
        onEvent(HomeEvent.GetData)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Header(
            { onEvent(HomeEvent.Navigate(route = PROFILE_SCREEN, popBackStack = false)) },
            uiState.username
        )

        CardInfo(
            "Games played: ${uiState.gamesPlayed}",
            trailingIcon = Res.drawable.gameplay_count,
            bgColor = OptionLightBlue,
            onClick = { onEvent(HomeEvent.Navigate(route = HISTORY_SCREEN, popBackStack = false)) }
        )

        Text(
            text = "Pick a category",
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            fontFamily = defaultFontFamily
        )

        Box {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategorySelector(
                        Constants.BOLLYWOOD_DISPLAY_TEXT,
                        {
                            onEvent(
                                HomeEvent.Navigate(
                                    route = "$PLAYING_SCREEN/${Constants.BOLLYWOOD_CODE}",
                                    popBackStack = false
                                )
                            )
                        },
                        Res.drawable.bollywood_banner
                    )
                    CategorySelector(
                        Constants.HOLLYWOOD_DISPLAY_TEXT,
                        {
                            onEvent(
                                HomeEvent.Navigate(
                                    route = "$PLAYING_SCREEN/${Constants.HOLLYWOOD_CODE}",
                                    popBackStack = false
                                )
                            )
                        },
                        Res.drawable.hollywood_banner
                    )
                }

                Spacer(modifier = Modifier.height(SMALL_HEIGHT))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategorySelector(
                        Constants.DESI_HIP_HOP_DISPLAY_TEXT,
                        {
                            onEvent(
                                HomeEvent.Navigate(
                                    route = "$PLAYING_SCREEN/${Constants.DESI_HIP_HOP_CODE}",
                                    popBackStack = false
                                )
                            )
                        },
                        Res.drawable.desi_hip_hop_banner
                    )
                    CategorySelector(
                        Constants.HIP_HOP_DISPLAY_TEXT,
                        {
                            onEvent(
                                HomeEvent.Navigate(
                                    route = "$PLAYING_SCREEN/${Constants.HIP_HOP_CODE}",
                                    popBackStack = false
                                )
                            )
                        },
                        Res.drawable.hip_hop_banner
                    )
                }
            }
        }

        CardInfo(
            "Leaderboard",
            trailingIcon = Res.drawable.leader_board_icon,
            bgColor = OptionLightGreen,
            onClick = {
                onEvent(
                    HomeEvent.Navigate(
                        route = LEADERBOARD_SCREEN,
                        popBackStack = false
                    )
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen(
        uiState = HomeUiState(username = "dummy_user")
    )
}