package com.creativehazio.workout.presentation.exercise

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.anim.GirlFitBreathingAnim
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.pinkAccentLight
import com.creativehazio.designsystem.theme.textHighlightedLight
import com.creativehazio.designsystem.theme.textLabelLight
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.back_icon
import girlfit.feature.workout.generated.resources.info_ic
import girlfit.feature.workout.generated.resources.minus_ic
import girlfit.feature.workout.generated.resources.next_exercise_ic
import girlfit.feature.workout.generated.resources.pause_ic
import girlfit.feature.workout.generated.resources.plus_ic
import girlfit.feature.workout.generated.resources.previous_exercise_ic
import girlfit.feature.workout.generated.resources.restart_ic
import girlfit.feature.workout.generated.resources.spotify_logo
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.Lottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExerciseScreenRoot(
    viewModel: ExerciseViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        event(ExerciseEvent.AppBackgrounded)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ExerciseScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onEvent = event
        )
    }
}

@Composable
internal fun ExerciseScreen(
    modifier: Modifier,
    uiState: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
) {

    val exercise = uiState.currentExercise ?: return

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(0.52f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)),
            ) {

                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.JsonString(exercise.gifUrl)
                )

                Lottie(
                    modifier = Modifier.align(Alignment.Center)
                        .width(240.dp)
                        .height(240.dp),
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = Compottie.IterateForever
                    ),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                ) {
                    DashedProgressBar(
                        progress = uiState.overallProgress,
                        segmentCount = uiState.exercises.size,
                        modifier = Modifier
                            .padding(vertical = Spacing.Medium, horizontal = Spacing.Medium),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(onClick = { onEvent(ExerciseEvent.BackClicked) }) {
                            Icon(
                                painter = painterResource(Res.drawable.back_icon),
                                tint = Color.Black,
                                contentDescription = null,
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { onEvent(ExerciseEvent.SpotifyClicked) }) {
                                Icon(
                                    painter = painterResource(Res.drawable.spotify_logo),
                                    contentDescription = "Open Spotify",
                                    tint = Color.Unspecified,
                                )
                            }
                            IconButton(onClick = { onEvent(ExerciseEvent.InfoClicked) }) {
                                Icon(
                                    painter = painterResource(Res.drawable.info_ic),
                                    contentDescription = "Exercise info",
                                )
                            }

                            IconButton(onClick = { onEvent(ExerciseEvent.RepeatClicked) }) {
                                Icon(
                                    painter = painterResource(Res.drawable.restart_ic),
                                    contentDescription = "Restart Workout",
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.48f)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = Spacing.Medium),
            ) {
                Spacer(Modifier.weight(1f))

                GirlFitBreathingAnim { scale, alpha ->
                    GirlFitInfoBubble(
                        modifier = Modifier
                            .width(112.dp)
                            .graphicsLayer{
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                                compositingStrategy = CompositingStrategy.ModulateAlpha
                            },
                        text = "Let's gooo ❤️"
                    )
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RoundIconButton(Res.drawable.minus_ic, "Decrease time") {
                                onEvent(ExerciseEvent.DecreaseDuration)
                            }
                            Text(
                                text = "${uiState.remainingSeconds} secs",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(horizontal = 14.dp),
                            )
                            RoundIconButton(Res.drawable.plus_ic, "Increase time") {
                                onEvent(ExerciseEvent.IncreaseDuration)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            exercise.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        uiState.friends.forEach { p ->
                            Row {
                                Text(
                                    text = if (p.isYou) "You:" else "${p.name}:",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(Modifier.size(6.dp))
                                Text(
                                    text = "${p.progressPercent}%",
                                    style = MaterialTheme.typography.labelMedium
                                        .copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(36.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.Center),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (uiState.isPaused) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            onEvent(ExerciseEvent.TogglePause)
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.pause_ic),
                            contentDescription = null
                        )
                    }

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            onEvent(ExerciseEvent.NextExercise)
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.next_exercise_ic),
                            contentDescription = null
                        )
                    }

                }
            }
        }

        CountdownOverlay(count = uiState.countdown)
    }

}

@Composable
internal fun CountdownOverlay(count: Int?) {
    AnimatedVisibility(
        visible = count != null,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .pointerInput(Unit) { detectTapGestures { } },
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = count ?: 1,
                transitionSpec = {
                    (scaleIn(initialScale = 0.4f) + fadeIn()) togetherWith
                            (scaleOut(targetScale = 1.8f) + fadeOut()) using SizeTransform(clip = false)
                },
                label = "countdown",
            ) { n ->
                Text(
                    text = n.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 80.sp)
                )
            }
        }
    }
}

@Composable
internal fun RoundIconButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(painterResource(icon), contentDescription, modifier = Modifier.size(20.dp))
    }
}


@Composable
internal fun DashedProgressBar(
    progress: Float,
    segmentCount: Int,
    modifier: Modifier = Modifier,
    gap: Dp = 6.dp,
) {
    Canvas(modifier.fillMaxWidth().height(4.dp)) {
        if (segmentCount <= 0) return@Canvas

        val stroke = 2.dp.toPx()
        val gapPx = gap.toPx()
        val segmentWidth = (size.width - gapPx * (segmentCount - 1)) / segmentCount
        val y = size.height / 2
        val overall = progress.coerceIn(0f, 1f) * segmentCount

        repeat(segmentCount) { i ->
            val startX = i * (segmentWidth + gapPx)
            val endX = startX + segmentWidth

            // Track
            drawLine(textLabelLight, Offset(startX, y), Offset(endX, y), stroke, StrokeCap.Butt)

            // Fill: 0f..1f for this segment only
            val fill = (overall - i).coerceIn(0f, 1f)
            if (fill > 0f) {
                drawLine(
                    color = textHighlightedLight,
                    start = Offset(startX, y),
                    end = Offset(startX + segmentWidth * fill, y),
                    strokeWidth = stroke,
                    cap = StrokeCap.Butt,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun ExerciseScreenPrev() {
    ExerciseScreen(
        modifier = Modifier,
        uiState = ExerciseState(),
        onEvent = {}
    )
}