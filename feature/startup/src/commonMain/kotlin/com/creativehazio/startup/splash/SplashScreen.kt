package com.creativehazio.startup.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import girlfit.feature.startup.generated.resources.Res
import girlfit.feature.startup.generated.resources.girlfit_splash_logo
import girlfit.feature.startup.generated.resources.green_gradient
import girlfit.feature.startup.generated.resources.pink_gradient
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreenRoot(
    viewModel: SplashViewModel,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit,
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when(it) {
                SplashEffect.NavigateToAuth -> {
                    onNavigateToAuth()
                }
                SplashEffect.NavigateToMain -> {
                    onNavigateToMain()
                }

                SplashEffect.NavigateToOnboarding -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }

    val gradientAlpha by animateFloatAsState(
        targetValue = if (uiState.startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "gradient_fade"
    )

    SplashScreen(
        gradientAlpha = gradientAlpha,
    )
}

@Composable
internal fun SplashScreen(
    gradientAlpha: Float,
) {

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
