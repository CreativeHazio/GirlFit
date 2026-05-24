package com.creativehazio.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.creativehazio.common.domain.workout.Challenge
import com.creativehazio.common.domain.workout.ChallengeDay
import com.creativehazio.common.domain.workout.ChallengeDayState
import com.creativehazio.common.domain.workout.Workout
import com.creativehazio.common.domain.workout.WorkoutCategory
import com.creativehazio.common.domain.workout.WorkoutType
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
    // TODO: Create a script to generate this boilerplate and also viewmodel boilerplate
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value
    val event = homeViewModel::onEvent

    LaunchedEffect(homeViewModel.effect) {
        homeViewModel.effect.collect {
            when(it) {
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
            val recommendedWorkouts = remember {
                mutableStateListOf(
                    Workout(
                        title = "Full body workout",
                        duration = "15 mins",
                        imageUrl = "https://images.unsplash.com/photo-1714646442330-9068099f5521?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        category = WorkoutCategory.RECOMMENDED
                    ),
                    Workout(
                        title = "Full body stretch",
                        duration = "12 mins",
                        imageUrl = "https://images.unsplash.com/photo-1630225760711-ac8eaa0c8947?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        category = WorkoutCategory.RECOMMENDED
                    ),
                )
            }
            RecommendedWorkoutSection(
                recommendedWorkouts = recommendedWorkouts,
                onNavigateToWorkoutDetail = {
                    event(HomeEvent.OnWorkoutCardClicked(it))
                }
            )
        }

        item {
            val relaxWorkouts = remember {
                mutableStateListOf(
                    Workout(
                        title = "De-stress",
                        type = WorkoutType.CHALLENGE,
                        imageUrl = "https://images.unsplash.com/photo-1571103774229-c4e9305aaad1?q=80&w=2069&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        category = WorkoutCategory.RELAX,
                        challenge = Challenge(
                            challengeDays = listOf(
                                ChallengeDay(
                                    number = 1,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 2,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 3,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 4,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 5,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 6,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 7,
                                    state = ChallengeDayState.COMPLETED,
                                ),
                                ChallengeDay(
                                    number = 8,
                                    state = ChallengeDayState.CURRENT,
                                ),
                                ChallengeDay(
                                    number = 9,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 10,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 11,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 12,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 13,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 14,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 15,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 16,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 17,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 18,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 19,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 20,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 21,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 22,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 23,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 24,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 25,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 26,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 27,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                                ChallengeDay(
                                    number = 28,
                                    state = ChallengeDayState.UPCOMING,
                                ),
                            )
                        )
                    )
                )
            }
            RelaxWorkoutSection(
                relaxWorkouts = relaxWorkouts,
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
    // TODO: Create a data class?
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
                durationText = if (workout.type == WorkoutType.TIME) workout.duration else null,
                detailsText = if (workout.type == WorkoutType.CHALLENGE) { "${workout.challenge.challengeTitle} Challenge" } else null,
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
                durationText = if (workout.type == WorkoutType.TIME) workout.duration else null,
                detailsText = if (workout.type == WorkoutType.CHALLENGE) { "${workout.challenge.challengeTitle} Challenge" } else null,
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