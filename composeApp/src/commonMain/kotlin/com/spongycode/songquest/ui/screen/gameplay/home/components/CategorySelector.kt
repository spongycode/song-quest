package com.spongycode.songquest.ui.screen.gameplay.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spongycode.songquest.getScreenWidth
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.defaultFontFamily
import com.spongycode.songquest.util.bounceClick
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun CategorySelector(
    categoryDisplayText: String,
    onClick: () -> Unit,
    bannerId: DrawableResource
) {
    val width = (getScreenWidth() - 40) / 2
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .bounceClick(0.98f)
            .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
            .width(width.dp)
            .height((width + 30).dp)
            .clickable {
                onClick()
            },
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
        ) {

            Image(
                painter = painterResource(resource = bannerId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }
        Box(
            modifier = Modifier
                .width(width.dp)
                .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF000000)),
                        startY = 0f,
                        endY = 100f
                    )
                )
                .padding(10.dp)
        ) {
            Text(
                text = categoryDisplayText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W600,
                color = Color.White,
                fontFamily = defaultFontFamily
            )
        }
    }
}
