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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.home.components.CardInfo
import com.spongycode.songquest.ui.screen.gameplay.home.components.CategorySelector
import com.spongycode.songquest.ui.screen.gameplay.home.components.Header
import com.spongycode.songquest.ui.theme.OptionLightBlue
import com.spongycode.songquest.ui.theme.OptionLightGreen
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT
import com.spongycode.songquest.util.Fonts

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val username = viewModel.username.value
    LaunchedEffect(Unit) {
        viewModel.getData()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Header({ navController.navigate("profile") }, username)

        CardInfo(
            "Games played: ${viewModel.gamesPlayed.intValue}",
            trailingIcon = R.drawable.gameplay_count,
            bgColor = OptionLightBlue,
            onClick = { navController.navigate("history") }
        )

        Text(
            text = "Pick a category",
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            fontFamily = Fonts.poppinsFamily
        )

        Box {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategorySelector(
                        Constants.BOLLYWOOD_DISPLAY_TEXT,
                        { navController.navigate("playing/${Constants.BOLLYWOOD_CODE}") },
                        R.drawable.bollywood_banner
                    )
                    CategorySelector(
                        Constants.HOLLYWOOD_DISPLAY_TEXT,
                        { navController.navigate("playing/${Constants.HOLLYWOOD_CODE}") },
                        R.drawable.hollywood_banner
                    )
                }

                Spacer(modifier = Modifier.height(SMALL_HEIGHT))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategorySelector(
                        Constants.DESI_HIP_HOP_DISPLAY_TEXT,
                        { navController.navigate("playing/${Constants.DESI_HIP_HOP_CODE}") },
                        R.drawable.desi_hip_hop_banner
                    )
                    CategorySelector(
                        Constants.HIP_HOP_DISPLAY_TEXT,
                        { navController.navigate("playing/${Constants.HIP_HOP_CODE}") },
                        R.drawable.hip_hop_banner
                    )
                }
            }
        }

        CardInfo(
            "Leaderboard",
            trailingIcon = R.drawable.leader_board_icon,
            bgColor = OptionLightGreen,
            onClick = { navController.navigate("leaderboard") }
        )
    }
}
