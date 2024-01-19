package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.R
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Fonts

@Composable
fun PlayingScreenPlaceholder(
    text: String,
    category: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 200.dp),
            text = text,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.W600,
            fontFamily = Fonts.poppinsFamily
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