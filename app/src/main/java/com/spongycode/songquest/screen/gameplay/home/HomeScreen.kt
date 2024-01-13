package com.spongycode.songquest.screen.gameplay.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
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
    ) {


        Header(username)

        Spacer(modifier = Modifier.height(LARGE_HEIGHT))

        Text(text = "Pick a category 🚀", fontSize = 20.sp, fontWeight = FontWeight.W600)

        Spacer(modifier = Modifier.height(SMALL_HEIGHT))


        Box {
            Column {
                Row {
                    CategorySelector(
                        Constants.BOLLYWOOD_DISPLAY_TEXT,
                        Constants.BOLLYWOOD_CODE,
                        navController,
                        R.drawable.bollywood_banner
                    )
                    CategorySelector(
                        Constants.HOLLYWOOD_DISPLAY_TEXT,
                        Constants.HOLLYWOOD_CODE,
                        navController,
                        R.drawable.hollywood_banner
                    )
                }

                Spacer(modifier = Modifier.height(MEDIUM_HEIGHT))

                Row {
                    CategorySelector(
                        Constants.DESI_HIP_HOP_DISPLAY_TEXT,
                        Constants.DESI_HIP_HOP_CODE,
                        navController,
                        R.drawable.desi_hip_hop_banner
                    )
                    CategorySelector(
                        Constants.HIP_HOP_DISPLAY_TEXT,
                        Constants.HIP_HOP_CODE,
                        navController,
                        R.drawable.hip_hop_banner
                    )
                }
            }
        }
    }
}


@Composable
fun Header(username: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 25.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(text = "Hi, $username", fontSize = 25.sp, fontWeight = FontWeight.W600)

        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(MEDIUM_HEIGHT),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "profile image"
        )
    }
}

@Composable
fun CategorySelector(
    categoryDisplayText: String,
    categoryDisplayCode: String,
    navController: NavHostController,
    bannerId: Int
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .clip(RoundedCornerShape(SMALL_HEIGHT))
            .width(200.dp)
            .height(150.dp)
            .padding(5.dp)
            .clickable {
                navController.navigate("playing/${categoryDisplayCode}")
            },
    ) {

        Card(
            modifier = Modifier
                .shadow(8.dp)
                .clip(RoundedCornerShape(SMALL_HEIGHT))
        ) {

            Image(
                painter = painterResource(id = bannerId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }
        Box(
            modifier = Modifier
                .width(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0x99000000)),
                        startY = 0f,
                        endY = 100f
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = categoryDisplayText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
