package com.creativehazio.workout.presentation.workoutchallengecalender

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderEffect.*
import kotlinx.coroutines.launch

data class WorkoutChallengeCalenderState(
    val isLoading: Boolean = false,
    val workout: Workout = Workout(),
) : State

sealed interface WorkoutChallengeCalenderEvent : Event {

    data class GetWorkoutChallengeById(val workoutId: String) : WorkoutChallengeCalenderEvent

    data class OnChallengeDayClicked(val workoutId: String) : WorkoutChallengeCalenderEvent
    data object OnBackClicked : WorkoutChallengeCalenderEvent
}

sealed interface WorkoutChallengeCalenderEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String) : WorkoutChallengeCalenderEffect
    data object NavigateBack : WorkoutChallengeCalenderEffect
}

class WorkoutChallengeCalenderViewModel(
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutChallengeCalenderState, WorkoutChallengeCalenderEvent, WorkoutChallengeCalenderEffect>(
    WorkoutChallengeCalenderState()
) {
    override fun onEvent(event: WorkoutChallengeCalenderEvent) {
        when (event) {
            WorkoutChallengeCalenderEvent.OnBackClicked -> {
                sendEffect(NavigateBack)
            }

            is WorkoutChallengeCalenderEvent.OnChallengeDayClicked -> {
                sendEffect(NavigateToWorkoutDetail(event.workoutId))
            }

            is WorkoutChallengeCalenderEvent.GetWorkoutChallengeById -> getWorkoutChallenge(event.workoutId)
        }
    }

    private fun getWorkoutChallenge(workoutId: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val workout = workoutRepository.getWorkout(workoutId)

            updateState { copy(isLoading = false, workout = workout) }
        }
    }
}