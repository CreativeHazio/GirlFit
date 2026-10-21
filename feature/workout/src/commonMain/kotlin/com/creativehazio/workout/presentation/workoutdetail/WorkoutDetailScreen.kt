package com.creativehazio.workout.presentation.workoutdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.common.util.DateTimeUtil
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.WorkoutLevel
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.designsystem.SharedElementKey
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.components.GirlfitImage
import com.creativehazio.designsystem.sharedBoundsIfAvailable
import com.creativehazio.designsystem.sharedElementIfAvailable
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.add_friend_icon
import girlfit.feature.workout.generated.resources.back_icon
import girlfit.feature.workout.generated.resources.heart_icon
import girlfit.feature.workout.generated.resources.heart_icon_selected
import girlfit.feature.workout.generated.resources.music_icon
import girlfit.feature.workout.generated.resources.next_exercise_ic
import girlfit.feature.workout.generated.resources.previous_exercise_ic
import girlfit.feature.workout.generated.resources.spotify_logo
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.Lottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutDetailScreenRoot(
    workoutViewModel: WorkoutDetailViewModel,
    sharedElementKey: String,
    onNavigateToExercise: () -> Unit,
    onBack: () -> Unit
) {
    val uiState = workoutViewModel.uiState.collectAsStateWithLifecycle().value
    val event = workoutViewModel::onEvent

    LaunchedEffect(workoutViewModel.effect) {
        workoutViewModel.effect.collect {
            when (it) {
                WorkoutDetailEffect.NavigateToExercise -> onNavigateToExercise()
            }
        }
    }

    if (uiState.showExerciseBottomSheet) {
        ExerciseBottomSheet(
            exercise = uiState.currentExercise,
            exercises = uiState.workout.exercises,
            onNext = {
                event(WorkoutDetailEvent.OnNextExerciseOnSheetClicked)
            },
            onPrevious = {
                event(WorkoutDetailEvent.OnPreviousExerciseOnSheetClicked)
            }
        ) {
            event(WorkoutDetailEvent.ToggleOnExerciseSheet)
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            GirlFitPrimaryButton(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = Spacing.Medium, end = Spacing.Medium),
                text = "Start",
                onClick = {
                    event(WorkoutDetailEvent.StartWorkout)
                }
            )
        }
    ) { innerPadding ->
        WorkoutDetailScreen(
            sharedElementKey = sharedElementKey,
            uiState = uiState,
            event = event,
            onBack = onBack
        )
    }
}

@Composable
internal fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    sharedElementKey: String,
    uiState: WorkoutDetailState,
    event: (WorkoutDetailEvent) -> Unit,
    onBack: () -> Unit,
) {

    val workout = uiState.workout

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            GirlfitImage(
                modifier = Modifier
                    .sharedBoundsIfAvailable(
                        key = SharedElementKey(
                            id = sharedElementKey,
                            type = SharedElementKey.Type.Image
                        )
                    )
                    .fillMaxSize(),
                imageUrl = workout.imageUrl,
                contentScale = ContentScale.Crop
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
            if (workout.type == WorkoutType.CHALLENGE) {
                val currentDay by remember {
                    mutableStateOf(
                        workout.challenge.challengeDays.find {
                            it.state == ChallengeDayState.CURRENT
                        }
                    )
                }
                Text(
                    text = "Week ${currentDay?.weekNumber}, Day ${currentDay?.number} 🎈",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                modifier = Modifier.sharedElementIfAvailable(
                    key = SharedElementKey(
                        id = workout.id,
                        type = SharedElementKey.Type.Title
                    )
                ),
                text = workout.title,
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
                                text = DateTimeUtil.durationFormatter(workout.duration),
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
                                    style = MaterialTheme.typography.labelLarge,
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
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(workout.exercises, key = { it.id }) {
                    ExerciseCard(
                        exercise = it,
                        onClick = {
                            event(WorkoutDetailEvent.OnExerciseClicked(it))
                        },
                        onFavourite = {
                            event(WorkoutDetailEvent.OnExerciseFavorited(it))
                        }
                    )
                }
            }

        }
    }

}

@Composable
internal fun ExerciseCard(
    exercise: Exercise,
    onClick: (Exercise) -> Unit,
    onFavourite: (Exercise) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().height(66.dp)
            .clickable {
                onClick(exercise)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.JsonString(exercise.thumbnailGifUrl)
        )

        Lottie(
            modifier = Modifier
                .size(66.dp),
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Compottie.IterateForever,
            ),
            contentDescription = null
        )

        Spacer(Modifier.size(Spacing.Small))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Text(
                exercise.title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = DateTimeUtil.durationFormatter(exercise.duration),
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(
            modifier = Modifier.size(Sizing.IconExtraLarge),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = CircleShape,
            onClick = {
                onFavourite(exercise)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExerciseBottomSheet(
    exercise: Exercise,
    exercises: List<Exercise>,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onDismiss: () -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.height(605.dp),
        sheetState = bottomSheetState,
        dragHandle = {},
        onDismissRequest = onDismiss,
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {

                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.JsonString(exercise.thumbnailGifUrl)
                )

                Lottie(
                    modifier = Modifier
                        .width(188.dp)
                        .height(188.dp),
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = Compottie.IterateForever,
                    ),
                    contentDescription = null
                )

            }

            Text(
                text = exercise.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    enabled = exercises.indexOf(exercise) > 0,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = onPrevious
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.previous_exercise_ic),
                        contentDescription = null
                    )
                }

                Text(
                    text = "${exercises.indexOf(exercise) + 1}/${exercises.size}"
                )

                IconButton(
                    enabled = exercises.indexOf(exercise) + 1 < exercises.size,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = onNext
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.next_exercise_ic),
                        contentDescription = null
                    )
                }

            }

            GirlFitPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Close",
                onClick = onDismiss
            )

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WorkoutDetailPreview() {

    ExerciseBottomSheet(
        exercise = Exercise(
            id = "1",
            title = "Reverse crunch",
            duration = 10,
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat." +
                    "\n\n" +
                    "This strengthens your lower abs and helps improve core stability.",
            thumbnailGifUrl = "",
            gifUrl = "",
            isFavourite = false
        ),
        exercises = emptyList(),
        onNext = {},
        onPrevious = {},
        onDismiss = {}
    )

//    WorkoutDetailScreen(
//        modifier = Modifier,
//        uiState = WorkoutDetailState(),
//        event = {},
//        onBack = {},
//    )
}