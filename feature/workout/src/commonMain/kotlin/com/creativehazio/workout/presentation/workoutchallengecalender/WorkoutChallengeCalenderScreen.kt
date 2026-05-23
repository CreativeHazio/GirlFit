package com.creativehazio.workout.presentation.workoutchallengecalender

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkoutChallengeCalenderScreenRoot(
    viewModel: WorkoutChallengeCalenderViewModel,
    onBack: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    Scaffold { innerPadding ->
        WorkoutChallengeCalenderScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onEvent = event
        )
    }
}

@Composable
internal fun WorkoutChallengeCalenderScreen(
    modifier: Modifier = Modifier,
    uiState: WorkoutChallengeCalenderState,
    onEvent: (WorkoutChallengeCalenderEvent) -> Unit
) {

}