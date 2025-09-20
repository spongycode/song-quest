package com.spongycode.songquest.ui.screen.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.spongycode.songquest.util.Fonts

@Composable
fun CustomAnnotatedString(
    str1: String,
    tag: String,
    str2: String,
    onClick: () -> Unit
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Fonts.poppinsFamily
                )
            ) {
                append(str1)
            }
            pushStringAnnotation(
                tag = tag,
                annotation = str2
            )
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF267BC4), textDecoration = TextDecoration.Underline,
                    fontFamily = Fonts.poppinsFamily, fontWeight = FontWeight.W600
                )
            ) {
                append(str2)
            }
            pop()
        },

        modifier = Modifier
            .clickable {
                onClick()
            }
    )
}