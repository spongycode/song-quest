package com.spongycode.songquest.ui.screen.gameplay.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.defaultFontFamily
import org.jetbrains.compose.resources.painterResource
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.baseline_account_circle_24

@Composable
fun Header(
    onClick: () -> Unit,
    username: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hi, $username",
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            fontFamily = defaultFontFamily
        )

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .size(Constants.MEDIUM_HEIGHT)
                .clickable {
                    onClick()
                },
            painter = painterResource(resource = Res.drawable.baseline_account_circle_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "profile image"
        )
    }
}