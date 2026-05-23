package com.creativehazio.workout.presentation.workoutdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.creativehazio.common.domain.workout.Exercise
import com.creativehazio.common.domain.workout.Workout
import com.creativehazio.common.domain.workout.WorkoutLevel
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.add_friend_icon
import girlfit.feature.workout.generated.resources.back_icon
import girlfit.feature.workout.generated.resources.heart_icon
import girlfit.feature.workout.generated.resources.heart_icon_selected
import girlfit.feature.workout.generated.resources.music_icon
import girlfit.feature.workout.generated.resources.spotify_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutDetailScreenRoot(
    workoutViewModel: WorkoutDetailViewModel,
    onBack: () -> Unit
) {
    val uiState = workoutViewModel.uiState.collectAsStateWithLifecycle().value
    val event = workoutViewModel::onEvent

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            GirlFitPrimaryButton(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = Spacing.Medium, end = Spacing.Medium),
                text = "Start",
                onClick = {}
            )
        }
    ) { innerPadding ->
        WorkoutDetailScreen(
            workout = uiState.workout,
            onBack = onBack
        )
    }
}

@Composable
internal fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    workout: Workout,
    onBack: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = workout.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Large),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(Res.drawable.back_icon),
                        tint = Color.White,
                        contentDescription = null,
                    )
                }

                IconButton(onClick = { /* Handle action */ }) {
                    Icon(
                        painter = painterResource(Res.drawable.add_friend_icon),
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
            }

        }

        Spacer(Modifier.size(Spacing.ExtraLarge))


        Column(
            modifier = Modifier.padding(start = Spacing.Medium, end = Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            Text(
                workout.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.weight(1f)
                        .heightIn(min = 60.dp, max = Sizing.CardHeightMedium),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.wrapContentHeight().padding(Spacing.Large),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                        ) {
                            Text(text = "Duration:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = workout.duration,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (workout.level != WorkoutLevel.NONE) {
                            Spacer(Modifier.size(Spacing.Medium))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                            ) {
                                Text(text = "Level:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = workout.level.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.size(Spacing.Medium))

                Card(
                    modifier = Modifier.weight(1f).height(Sizing.CardHeightMedium),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(Spacing.Medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Music",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Image(
                                modifier = Modifier.size(Sizing.IconLarge),
                                painter = painterResource(Res.drawable.spotify_logo),
                                contentDescription = null
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(Sizing.IconExtraLarge),
                                painter = painterResource(Res.drawable.music_icon),
                                contentDescription = null
                            )
                            Text("Soothing Lofi", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Text(
                    "Exercises",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "(${workout.exercises.size})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Spacing.Large),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                items(workout.exercises, key = { it.id }) {
                    ExerciseCard(it)
                }
            }

        }
    }

}

@Composable
internal fun ExerciseCard(
    exercise: Exercise
) {

    Row(
        modifier = Modifier.fillMaxWidth().height(66.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier.size(66.dp),
            model = exercise.thumbnailGifUrl,
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Text(
                exercise.title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(exercise.duration, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(
            modifier = Modifier.size(Sizing.IconExtraLarge),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = CircleShape,
            onClick = {

            }
        ) {
            Icon(
                painter = painterResource(
                    if (exercise.isFavourite) Res.drawable.heart_icon_selected else Res.drawable.heart_icon
                ),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WorkoutDetailPreview() {
    WorkoutDetailScreen(
        workout = getDummyWorkout(),
        onBack = {},
    )
}