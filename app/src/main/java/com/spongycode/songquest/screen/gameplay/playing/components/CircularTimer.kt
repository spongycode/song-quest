package com.spongycode.songquest.screen.gameplay.playing.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.Constants.CIRCULAR_TIMER_RADIUS
import com.spongycode.songquest.util.Fonts

@Composable
fun CircularTimer(
    transitionData: CircularTransitionData,
    modifier: Modifier = Modifier,
    time: Int
) {
    val textMeasurer = rememberTextMeasurer()

    val textToDraw = time.toString()

    val style = androidx.compose.ui.text.TextStyle(
        fontSize = 20.sp,
        color = Color.Black,
        background = Color.Transparent,
        fontWeight = FontWeight.W800,
        fontFamily = Fonts.poppinsFamily
    )

    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(textToDraw, style)
    }
    Canvas(
        modifier = modifier
            .requiredWidth(CIRCULAR_TIMER_RADIUS.dp)
            .requiredHeight(CIRCULAR_TIMER_RADIUS.dp)
    ) {

        inset(size.width / 2 - CIRCULAR_TIMER_RADIUS, size.height / 2 - CIRCULAR_TIMER_RADIUS) {
            drawCircle(
                color = Color.LightGray,
                radius = CIRCULAR_TIMER_RADIUS.toFloat(),
                center = center,
                style = Stroke(width = 25f, cap = StrokeCap.Round)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = textToDraw,
                style = style,
                topLeft = Offset(
                    x = center.x - textLayoutResult.size.width / 2,
                    y = center.y - textLayoutResult.size.height / 2,
                ),
            )
            drawArc(
                startAngle = 270f,
                sweepAngle = transitionData.progress,
                useCenter = false,
                color = transitionData.color,
                style = Stroke(width = 25f, cap = StrokeCap.Round)
            )
        }
    }
}
