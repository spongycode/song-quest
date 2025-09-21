package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.Constants.LARGE_HEIGHT
import com.spongycode.songquest.util.defaultFontFamily
import com.spongycode.songquest.util.bounceClick

@Preview
@Composable
fun OptionField(
    text: String = "Option",
    singleLine: Boolean = true,
    onClick: () -> Unit = {},
    fillColor: Color = Color.White,
    tint: Color = Color.Black,
    iconId: Int? = null
) {
    Surface(
        color = Color.Transparent,
        border = BorderStroke(2.dp, tint),
        modifier = Modifier
            .bounceClick(0.98f)
            .background(
                MaterialTheme.colorScheme.primary
                    .copy(alpha = 0.0f)
                    .compositeOver(Color.White),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .height(LARGE_HEIGHT)
                .fillMaxWidth()
                .background(fillColor)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = defaultFontFamily
            )
            iconId?.let { painterResource(id = it) }?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = tint
                )
            }
        }
    }
}
