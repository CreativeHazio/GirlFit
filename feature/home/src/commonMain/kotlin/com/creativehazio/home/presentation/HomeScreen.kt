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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.common.util.DateTimeUtil
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.components.GirlFitWorkoutCard
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.home.presentation.components.HomeScreenCycleCalenderSection
import com.creativehazio.home.presentation.components.HomeScreenWelcomeSection
import com.creativehazio.home.presentation.components.WorkoutSection
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
            WorkoutSection(
                name = "Recommended workouts",
                workouts = uiState.recommendedWorkouts,
                onNavigateToWorkoutDetail = {
                    event(HomeEvent.OnWorkoutCardClicked(it))
                }
            )
        }

        item {
            WorkoutSection(
                name = "Relax instead?",
                workouts = uiState.relaxWorkouts,
                onNavigateToWorkoutDetail = {
                    event(HomeEvent.OnWorkoutCardClicked(it))
                }
            )
            Spacer(Modifier.size(Spacing.Small))
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