package com.spongycode.songquest.screen.gameplay.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.util.Constants.MEDIUM_HEIGHT
import com.spongycode.songquest.util.Constants.SMALL_HEIGHT

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Home Screen", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(MEDIUM_HEIGHT))

        Box {
            Column {
                Row {
                    CategorySelector("BOLLYWOOD", navController)
                    CategorySelector("HOLLYWOOD", navController)
                }

                Row {
                    CategorySelector("DESI_HIP_HOP", navController)
                    CategorySelector("HIP_HOP", navController)
                }
            }
        }
    }
}


@Composable
fun CategorySelector(
    category: String = "Category",
    navController: NavHostController,

    ) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(SMALL_HEIGHT)
            .clickable {
                navController.navigate("playing/${category}")
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_music_note_24),
            contentDescription = "category_icon"
        )
        Text(text = category)
    }
}