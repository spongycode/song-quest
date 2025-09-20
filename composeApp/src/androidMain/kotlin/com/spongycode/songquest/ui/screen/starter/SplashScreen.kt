package com.spongycode.songquest.ui.screen.starter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.util.Fonts
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreenRoot(
    viewModel: SplashViewModel
) {
    val navController = LocalNavController.current
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is SplashViewEffect.Navigate -> {
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    navController.navigate(it.route)
                }
            }
        }
    }

    SplashScreen(onEvent = viewModel::onEvent)
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onEvent: (SplashEvent) -> Unit = {}
) {
    LaunchedEffect(null) {
        onEvent(SplashEvent.RefreshToken)
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .width(210.dp),
                painter = painterResource(id = R.drawable.logo_text), contentDescription = null
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Authenticating  ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.poppinsFamily,
                color = Color.White
            )
            val strokeWidth = 3.dp
            CircularProgressIndicator(
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            Color.White,
                            radius = size.width / 2 - strokeWidth.toPx() / 2,
                            style = Stroke(strokeWidth.toPx())
                        )
                    }
                    .size(25.dp),
                color = Color.LightGray,
                strokeWidth = strokeWidth
            )
        }
    }
}

@Preview
@Composable
private fun PreviewStarterScreen() {
    SplashScreen()
}