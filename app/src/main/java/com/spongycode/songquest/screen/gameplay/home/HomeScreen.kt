package com.spongycode.songquest.screen.gameplay.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.gameplay.home.components.CategorySelector
import com.spongycode.songquest.screen.gameplay.home.components.Header
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.LARGE_HEIGHT
import com.spongycode.songquest.util.Constants.MEDIUM_HEIGHT
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val username = viewModel.username.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Header({ navController.navigate("profile") }, username)

        Spacer(modifier = Modifier.height(LARGE_HEIGHT))

        Text(text = "Pick a category", fontSize = 20.sp, fontWeight = FontWeight.W600)

        Spacer(modifier = Modifier.height(SMALL_HEIGHT))

        Box {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
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
                    horizontalArrangement = Arrangement.SpaceAround
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
    }
}
