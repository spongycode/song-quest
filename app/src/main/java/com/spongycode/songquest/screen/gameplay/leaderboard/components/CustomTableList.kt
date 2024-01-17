package com.spongycode.songquest.screen.gameplay.leaderboard.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.data.model.gameplay.LeaderboardUsersModel
import com.spongycode.songquest.ui.theme.OptionLightYellow
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.TimesAgo

const val column1Weight = .3f
const val column2Weight = .3f
const val column3Weight: Float = .4f

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTableList(
    listItems: List<LeaderboardUsersModel>,
    topPadding: Dp
) {
    val configuration = LocalConfiguration.current
    val width = (configuration.screenWidthDp - 20) / 2
    Column(Modifier.padding(start = 10.dp, end = 10.dp, top = topPadding)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NormalText(text = "Score", weight = column1Weight, title = true)
            NormalText(text = "User", weight = column3Weight, title = true)
            NormalText(text = "Accurate", weight = column2Weight, title = true)
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .height(1.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        LazyColumn {
            itemsIndexed(listItems) { _, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height((width / 3).dp)
                        .clip(RoundedCornerShape(Constants.SMALL_HEIGHT))
                        .background(OptionLightYellow),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NormalText(
                            text = item.score.toInt().toString(),
                            weight = column1Weight
                        )
                        CategoryDateText(
                            username = item.username,
                            time = TimesAgo.getTimeAgo(item.createdAt.toString()),
                            weight = column3Weight
                        )
                        NormalText(text = item.accurate.toString(), weight = column2Weight)


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
            .padding(horizontal = 10.dp, vertical = if (title) 10.dp else 0.dp),
        fontWeight = if (title) FontWeight.W600 else FontWeight.W500,
        textAlign = alignment,
        fontSize = 18.sp
    )
}

@Composable
fun RowScope.CategoryDateText(
    username: String,
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
            text = username,
            fontWeight = FontWeight.W600,
            textAlign = alignment,
            fontSize = 16.sp
        )
        Text(
            text = time,
            fontWeight = FontWeight.W400,
            textAlign = alignment,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}