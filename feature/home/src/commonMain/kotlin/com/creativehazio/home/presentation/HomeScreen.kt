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
import com.creativehazio.home.presentation.components.cycleCalender.CycleCalenderSection
import com.creativehazio.home.presentation.components.WelcomeSection
import com.creativehazio.home.presentation.components.WorkoutSection
import com.creativehazio.home.presentation.components.cycleCalender.CycleCalenderViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel,
    modifier: Modifier,
    contentPaddingValues: PaddingValues,
    onNavigateToWorkoutDetail: (workoutId: String, sharedElementKey: String) -> Unit,
    onNavigateToWorkoutChallengeCalender: (workoutId: String, sharedElementKey: String) -> Unit,
) {
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value
    val event = homeViewModel::onEvent

    LaunchedEffect(homeViewModel.effect) {
        homeViewModel.effect.collect {
            when (it) {
                is HomeEffect.NavigateToWorkoutChallengeCalender -> {
                    onNavigateToWorkoutChallengeCalender(
                        it.workoutId,
                        it.sharedElementKey
                    )
                }

                is HomeEffect.NavigateToWorkoutDetail -> {
                    onNavigateToWorkoutDetail(
                        it.workoutId,
                        it.sharedElementKey
                    )
                }
            }
        }
    }

    HomeScreen(
        modifier = modifier,
        contentPaddingValues = contentPaddingValues,
        uiState = uiState,
        event = event,
    )

}

@Composable
internal fun HomeScreen(
    modifier: Modifier,
    contentPaddingValues: PaddingValues,
    uiState: HomeState,
    event: (HomeEvent) -> Unit,
) {

    LazyColumn(
        modifier = modifier.padding(
            start = Spacing.Medium,
            end = Spacing.Medium
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge),
        contentPadding = contentPaddingValues
    ) {

        item {
            Spacer(Modifier.size(Spacing.Medium))
            WelcomeSection(
                userName = uiState.user.name,
                userFeelings = uiState.userFeelings,
                onUserFeelingClicked = {

                }
            )
        }

        item {
            val cycleCalenderViewModel: CycleCalenderViewModel = koinViewModel()
            CycleCalenderSection(
                viewModel = cycleCalenderViewModel
            )
        }

        item {
            WorkoutSection(
                isLoading = uiState.isRecommendedLoading,
                name = "Recommended workouts",
                reloadWorkouts = {
                    event(HomeEvent.ReloadRecommendedWorkouts)
                },
                workouts = uiState.recommendedWorkouts,
                onNavigateToWorkoutDetail = { workout, sharedElementKey ->
                    event(HomeEvent.OnWorkoutCardClicked(workout, sharedElementKey))
                }
            )
        }

        item {
            WorkoutSection(
                isLoading = uiState.isRelaxLoading,
                name = "Relax instead?",
                reloadWorkouts = {
                    event(HomeEvent.ReloadRelaxWorkouts)
                },
                workouts = uiState.relaxWorkouts,
                onNavigateToWorkoutDetail = { workout, sharedElementKey ->
                    event(HomeEvent.OnWorkoutCardClicked(workout, sharedElementKey))
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
        modifier = Modifier,
        contentPaddingValues = PaddingValues.Zero,
        uiState = HomeState(),
        event = {},
    )
}