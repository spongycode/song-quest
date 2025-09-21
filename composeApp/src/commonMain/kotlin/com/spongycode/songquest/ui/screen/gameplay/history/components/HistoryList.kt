package com.spongycode.songquest.ui.screen.gameplay.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.getScreenWidth
import com.spongycode.songquest.ui.theme.OptionLightYellow
import com.spongycode.songquest.util.CategoryConvertor
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.TimesAgo
import com.spongycode.songquest.util.defaultFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview


const val column1Weight = .3f
const val column2Weight = .3f
const val column3Weight: Float = .4f

@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    games: List<GameModel> = emptyList()
) {
    val width = (getScreenWidth() - 20) / 2
    Column(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NormalText(
                text = "Score",
                weight = column1Weight,
                title = true
            )
            NormalText(text = "Accurate", weight = column2Weight, title = true)
            NormalText(text = "Category", weight = column3Weight, title = true)
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            color = Color.LightGray
        )
        LazyColumn {
            itemsIndexed(games) { _, game ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height((width / 3).dp)
                        .clip(RoundedCornerShape(Constants.VERY_SMALL_HEIGHT))
                        .background(OptionLightYellow),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NormalText(
                            text = game.score?.toInt().toString(),
                            weight = column1Weight
                        )
                        NormalText(text = game.accurate.toString(), weight = column2Weight)

                        CategoryDateText(
                            category = game.category.toString(),
                            time = TimesAgo.getTimeAgo(game.createdAt.toString()),
                            weight = column3Weight
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.NormalText(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(horizontal = 10.dp, vertical = if (title) 10.dp else 0.dp)
            .basicMarquee(),
        fontWeight = if (title) FontWeight.W600 else FontWeight.W500,
        textAlign = alignment,
        fontSize = 18.sp,
        fontFamily = defaultFontFamily
    )
}

@Composable
fun RowScope.CategoryDateText(
    category: String,
    time: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center
) {
    Column(
        Modifier
            .weight(weight)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.basicMarquee(),
            text = CategoryConvertor.codeToDisplayText(category),
            fontWeight = FontWeight.W600,
            textAlign = alignment,
            fontSize = 16.sp,
            fontFamily = defaultFontFamily
        )
        Text(
            text = time,
            fontWeight = FontWeight.W400,
            textAlign = alignment,
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = defaultFontFamily
        )
    }
}

@Preview
@Composable
private fun PreviewHistoryList() {
    HistoryList(
        games = listOf(
            GameModel().dummy(),
            GameModel().dummy(),
            GameModel().dummy(),
        )
    )
}