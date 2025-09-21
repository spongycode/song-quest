package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.ui.theme.OptionLightBlue
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.defaultFontFamily

@Preview
@Composable
fun QuestionTitle(title: String = "") {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(Constants.SMALL_HEIGHT)),
            colors = CardDefaults.cardColors(
                containerColor = OptionLightBlue,
            ),
        ) {
            Text(
                modifier = Modifier
                    .padding(30.dp),
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                fontFamily = defaultFontFamily
            )
        }
    }
}