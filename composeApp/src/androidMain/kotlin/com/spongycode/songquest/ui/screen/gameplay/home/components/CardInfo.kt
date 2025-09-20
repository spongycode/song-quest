package com.spongycode.songquest.ui.screen.gameplay.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spongycode.songquest.R
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.MEDIUM_HEIGHT
import com.spongycode.songquest.util.Fonts
import com.spongycode.songquest.util.bounceClick


@Preview
@Composable
fun CardInfo(
    categoryDisplayText: String = "Leaderboard",
    onClick: () -> Unit = {},
    trailingIcon: Int = R.drawable.gameplay_count,
    bgColor: Color = Color.Red
) {
    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp - 20) / 2

    Box(
        modifier = Modifier
            .bounceClick(0.98f)
            .fillMaxWidth()
            .height((width / 3).dp)
            .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
            .background(bgColor)
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryDisplayText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontFamily = Fonts.poppinsFamily
            )
            Image(
                modifier = Modifier.size(MEDIUM_HEIGHT + 10.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = trailingIcon), contentDescription = null
            )
        }
    }
}
