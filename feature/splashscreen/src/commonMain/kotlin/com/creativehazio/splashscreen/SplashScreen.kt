package com.creativehazio.splashscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.creativehazio.designsystem.theme.textHighlightedLight
import girlfit.feature.splashscreen.generated.resources.Res
import girlfit.feature.splashscreen.generated.resources.girlfit_splash_logo
import girlfit.feature.splashscreen.generated.resources.green_gradient
import girlfit.feature.splashscreen.generated.resources.pink_gradient
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreenRoot(
    onNavigateToHome: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true

        // Wait for your database checks, then navigate
        delay(2500)
        onNavigateToHome()
    }

    val gradientAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "gradient_fade"
    )

    SplashScreen(gradientAlpha = gradientAlpha)
}

@Composable
internal fun SplashScreen(gradientAlpha: Float) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 67.dp)
                .alpha(gradientAlpha),
            painter = painterResource(Res.drawable.green_gradient),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = (-67).dp)
                .alpha(gradientAlpha),
            painter = painterResource(Res.drawable.pink_gradient),
            contentDescription = null
        )

        Image(
            painter = painterResource(Res.drawable.girlfit_splash_logo),
            contentDescription = null,
            modifier = Modifier
                .size(288.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ssp() {
    SplashScreen(1f)
}
