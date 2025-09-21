package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.spongycode.songquest.ui.theme.OptionDarkRed
import com.spongycode.songquest.util.Constants
import org.jetbrains.compose.resources.painterResource
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.heart
import kotlin.math.min

@Composable
fun HealthMeter(totalLife: Int = Constants.TOTAL_CHANCE) {
    LazyRow {
        repeat(totalLife) {
            item {
                Icon(
                    modifier = Modifier
                        .size(2 * Constants.SMALL_HEIGHT)
                        .padding(2.dp),
                    painter = painterResource(resource = Res.drawable.heart),
                    contentDescription = "fill",
                    tint = OptionDarkRed
                )
            }
        }
        repeat(min(Constants.TOTAL_CHANCE, Constants.TOTAL_CHANCE - totalLife)) {
            item {
                Icon(
                    modifier = Modifier
                        .size(2 * Constants.SMALL_HEIGHT)
                        .padding(2.dp),
                    painter = painterResource(resource = Res.drawable.heart),
                    contentDescription = "empty",
                    tint = Color.LightGray
                )
            }
        }
    }

}