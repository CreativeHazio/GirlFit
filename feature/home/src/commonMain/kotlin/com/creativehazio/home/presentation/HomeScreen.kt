package com.creativehazio.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.common.util.DateTimeUtil
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.ChallengeDay
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.components.GirlFitWorkoutCard
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.angry_emoji
import girlfit.feature.home.generated.resources.drop_emoji
import girlfit.feature.home.generated.resources.exhausted_emoji
import girlfit.feature.home.generated.resources.lollipop
import girlfit.feature.home.generated.resources.sad_emoji
import girlfit.feature.home.generated.resources.sick_emoji
import girlfit.feature.home.generated.resources.smile_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel,
    contentPaddingValues: PaddingValues,
    onNavigateToWorkoutDetail: (String) -> Unit,
    onNavigateToWorkoutChallengeCalender: (String) -> Unit,
) {
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value
    val event = homeViewModel::onEvent

    LaunchedEffect(homeViewModel.effect) {
        homeViewModel.effect.collect {
            when (it) {
                is HomeEffect.NavigateToWorkoutChallengeCalender -> {
                    onNavigateToWorkoutChallengeCalender(it.workoutId)
                }

                is HomeEffect.NavigateToWorkoutDetail -> {
                    onNavigateToWorkoutDetail(it.workoutId)
                }
            }
        }
    }

    HomeScreen(
        contentPaddingValues = contentPaddingValues,
        uiState = uiState,
        event = event,
    )

}

@Composable
internal fun HomeScreen(
    contentPaddingValues: PaddingValues,
    uiState: HomeState,
    event: (HomeEvent) -> Unit,
) {

    LazyColumn(
        modifier = Modifier.padding(
            start = Spacing.Medium,
            end = Spacing.Medium
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge),
        contentPadding = contentPaddingValues
    ) {

        item {
            Spacer(Modifier.size(Spacing.Medium))
            HomeScreenWelcomeSection()
        }

        item {
            HomeScreenCycleCalenderSection()
        }

        item {
            RecommendedWorkoutSection(
                recommendedWorkouts = uiState.recommendedWorkouts,
                onNavigateToWorkoutDetail = {
                    event(HomeEvent.OnWorkoutCardClicked(it))
                }
            )
        }

        item {
            RelaxWorkoutSection(
                relaxWorkouts = uiState.relaxWorkouts,
                onNavigateToWorkoutDetail = {
                    event(HomeEvent.OnWorkoutCardClicked(it))
                }
            )
            Spacer(Modifier.size(Spacing.Small))
        }

    }

}

@Composable
internal fun HomeScreenWelcomeSection() {
    val feelings = remember {
        mutableStateMapOf(
            "Happy" to Res.drawable.smile_emoji,
            "Sad" to Res.drawable.sad_emoji,
            "Sick" to Res.drawable.sick_emoji,
            "Tired" to Res.drawable.exhausted_emoji,
            "Angry" to Res.drawable.angry_emoji,
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text(
                    "Hi Ria ❤️",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "How do you feel today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Image(
                    modifier = Modifier.size(Sizing.IconExtraLarge),
                    painter = painterResource(Res.drawable.lollipop),
                    contentDescription = null
                )
                Text(
                    "6",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }

        Spacer(Modifier.size(Spacing.Large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            feelings.forEach { resource ->
                Column(
                    modifier = Modifier.clickable {

                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                ) {
                    Image(
                        modifier = Modifier.size(Sizing.IconLarge),
                        painter = painterResource(resource.value),
                        contentDescription = null
                    )
                    Text(resource.key, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

    }
}

@Composable
internal fun HomeScreenCycleCalenderSection() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Image(
                modifier = Modifier.size(Sizing.IconExtraLarge),
                painter = painterResource(Res.drawable.drop_emoji),
                contentDescription = null

            )
            Text(
                "14th day of Cycle",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.size(Spacing.Medium))
        CycleCalender()
        Spacer(Modifier.size(Spacing.Medium))
        GirlFitInfoBubble(
            text = "Hormonal shift may lead to mood\n" +
                    "swings, bloating & fatigue"
        )
    }
}

@Composable
internal fun CycleCalender() {
    val daysOfTheWeek = remember {
        mutableStateListOf(
            "Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"
        )
    }

    val thisWeekDay = remember {
        mutableStateListOf(
            "30", "01", "02", "03", "04", "05", "06"
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Medium)
        ) {
            Text(
                "2 December",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.size(Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfTheWeek.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.size(Spacing.Small))
                        Text(
                            text = thisWeekDay[index],
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.background(
                                color = if (index == 3) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            ).padding(Spacing.ExtraSmall)
                        )
                    }
                }
            }

        }
    }
}

@Composable
internal fun RecommendedWorkoutSection(
    recommendedWorkouts: List<Workout>,
    onNavigateToWorkoutDetail: (Workout) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            "Recommended workouts",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.size(Spacing.ExtraSmall))
        recommendedWorkouts.forEach { workout ->
            GirlFitWorkoutCard(
                modifier = Modifier.fillMaxWidth().height(Sizing.CardHeightMedium),
                title = workout.title,
                titleStyle = MaterialTheme.typography.headlineSmall,
                durationText = if (workout.type == WorkoutType.TIME) DateTimeUtil.durationFormatter(
                    workout.duration
                ) else null,
                detailsText = if (workout.type == WorkoutType.CHALLENGE) {
                    "${workout.challenge.challengeTitle} Challenge"
                } else null,
                imageUrl = workout.imageUrl,
                onCardClick = {
                    onNavigateToWorkoutDetail(workout)
                }
            )
        }
    }
}

@Composable
internal fun RelaxWorkoutSection(
    relaxWorkouts: List<Workout>,
    onNavigateToWorkoutDetail: (Workout) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            "Relax instead?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.size(Spacing.ExtraSmall))
        relaxWorkouts.forEach { workout ->
            GirlFitWorkoutCard(
                modifier = Modifier.fillMaxWidth().height(Sizing.CardHeightMedium),
                title = workout.title,
                titleStyle = MaterialTheme.typography.headlineSmall,
                durationText = if (workout.type == WorkoutType.TIME) DateTimeUtil.durationFormatter(
                    workout.duration
                ) else null,
                detailsText = if (workout.type == WorkoutType.CHALLENGE) {
                    "${workout.challenge.challengeTitle} Challenge"
                } else null,
                imageUrl = workout.imageUrl,
                onCardClick = {
                    onNavigateToWorkoutDetail(workout)
                }
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        contentPaddingValues = PaddingValues.Zero,
        uiState = HomeState(),
        event = {},
    )
}