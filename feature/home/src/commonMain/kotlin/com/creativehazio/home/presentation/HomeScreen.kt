package com.creativehazio.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.home.presentation.components.HomeScreenCycleCalenderSection
import com.creativehazio.home.presentation.components.HomeScreenWelcomeSection
import com.creativehazio.home.presentation.components.WorkoutSection

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
            HomeScreenWelcomeSection(
                userName = uiState.user.name,
                userFeelings = uiState.userFeelings,
                onUserFeelingClicked = {

                }
            )
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