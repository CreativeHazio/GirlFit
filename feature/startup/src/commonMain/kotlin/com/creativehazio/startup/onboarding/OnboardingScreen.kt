package com.creativehazio.startup.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.LocalAsyncImagePreviewHandler
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.getPoppinsFontFamily
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import girlfit.feature.startup.generated.resources.Res
import girlfit.feature.startup.generated.resources.back_icon
import girlfit.feature.startup.generated.resources.girlfit_splash_logo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreenRoot(
    viewModel: OnboardingViewModel,
    onNavigateToAuth: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                OnboardingEffect.NavigateToAuth -> {
                    onNavigateToAuth()
                }
            }
        }
    }

    OnboardingScreen(
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = { uiState.onboardingItems.size },
    )

    val isNotFirstPage = pagerState.currentPage > 0

    Box(
        modifier = modifier.fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 80.dp, top = 16.dp)
    ) {

        Row(
            modifier = Modifier.align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isNotFirstPage) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage - 1
                            )
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.back_icon),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    onEvent(OnboardingEvent.OnSkipOrContinuePressed)
                }
            ) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF2C5640),
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.align(Alignment.Center)
                .padding(top = 60.dp),
            state = pagerState,
        ) {

            val currentImage = uiState.onboardingItems[it].image
            val currentTopText = uiState.onboardingItems[it].topText
            val currentTopTextPadding = uiState.onboardingItems[it].topTextPadding
            val currentBottomText = uiState.onboardingItems[it].bottomText
            val currentBottomTextPadding = uiState.onboardingItems[it].bottomTextPadding

            OnboardingItemScreen(
                image = currentImage,
                topText = currentTopText.asString(),
                topTextPadding = currentTopTextPadding,
                bottomText = currentBottomText.asString(),
                bottomTextPadding = currentBottomTextPadding,
            )

        }

        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OnboardingIndicator(
                count = pagerState.pageCount,
                current = pagerState.currentPage
            )

            GirlFitPrimaryButton(
                modifier = Modifier.width(100.dp)
                    .height(Sizing.ButtonHeight),
                shape = MaterialTheme.shapes.large,
                text = "Next",
                onClick = {
                    if (pagerState.currentPage == pagerState.pageCount - 1) {
                        onEvent(OnboardingEvent.OnSkipOrContinuePressed)
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                            )
                        }
                    }
                },
            )

        }

    }


}

@Composable
internal fun OnboardingItemScreen(
    image: DrawableResource,
    topText: String,
    bottomText: String,
    topTextPadding: Dp,
    bottomTextPadding: Dp,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.height(435.dp).width(235.dp),
            painter = painterResource(image),
            contentDescription = null
        )

        Spacer(Modifier.size(40.dp))

        Text(
            modifier = Modifier.fillMaxWidth().padding(start = topTextPadding),
            text = topText,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontFamily = getPoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
            )
        )
        Spacer(Modifier.size(Spacing.ExtraSmall))
        Text(
            modifier = Modifier.fillMaxWidth().padding(end = bottomTextPadding),
            text = bottomText,
            textAlign = TextAlign.End,
            color = Color(0xFF2C5640),
            style = MaterialTheme.typography.headlineSmall
        )

    }

}

@Composable
internal fun OnboardingIndicator(
    count: Int,
    current: Int
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        List(count) {
            val isCurrent = current == it
            Box(
                modifier = Modifier.size(8.dp)
                    .background(
                        color = if (isCurrent) MaterialTheme.colorScheme.primary
                                    else greyDisabledButtonLight,
                        shape = CircleShape
                    )
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun OnboardingPreview() {

    OnboardingScreen(
        uiState = OnboardingState(onboardingItems = getAllOnboardingItems()),
        onEvent = {}
    )

}