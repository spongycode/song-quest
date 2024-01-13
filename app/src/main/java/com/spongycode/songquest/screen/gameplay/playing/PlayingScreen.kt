package com.spongycode.songquest.screen.gameplay.playing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import com.spongycode.songquest.util.Constants.LARGE_HEIGHT
import com.spongycode.songquest.util.Constants.MEDIUM_HEIGHT
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT

@Composable
fun PlayingScreen(
    category: String,
    navController: NavHostController,
    viewModel: PlayingViewModel = hiltViewModel()
) {
    LaunchedEffect(category) {
        viewModel.createGame(category)
    }

    val currentSongIndex = viewModel.currentSongIndex.intValue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (currentSongIndex < viewModel.questions.size) {

            Spacer(modifier = Modifier.height(MEDIUM_HEIGHT))
            Text(
                text = viewModel.questions[currentSongIndex].title.toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
            LaunchedEffect(currentSongIndex) {
                viewModel.playCurrentSong()
            }
            Spacer(modifier = Modifier.height(LARGE_HEIGHT))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewModel.questions[currentSongIndex].options) { option ->
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = option.value.toString(),
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(SMALL_HEIGHT))
                    Spacer(modifier = Modifier.height(SMALL_HEIGHT))
                }
            }
        }
    }
}