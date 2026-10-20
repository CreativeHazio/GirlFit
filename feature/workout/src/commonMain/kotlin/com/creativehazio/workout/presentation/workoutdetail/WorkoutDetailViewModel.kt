package com.creativehazio.workout.presentation.workoutdetail

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutRepository
import kotlinx.coroutines.launch

data class WorkoutDetailState(
    val isLoading: Boolean = false,
    val showExerciseBottomSheet: Boolean = false,
    val currentExercise: Exercise = Exercise(),
    val workout: Workout = Workout()
) : State

sealed interface WorkoutDetailEvent : Event {
    data class GetWorkoutById(val workoutId: String) : WorkoutDetailEvent
    data class OnExerciseClicked(val exercise: Exercise) : WorkoutDetailEvent
    data object ToggleOnExerciseSheet : WorkoutDetailEvent
    data class OnExerciseFavorited(val exercise: Exercise) : WorkoutDetailEvent

    data object StartWorkout : WorkoutDetailEvent
}

sealed interface WorkoutDetailEffect : Effect {
    data object NavigateToExercise : WorkoutDetailEffect
}

class WorkoutDetailViewModel(
    workoutId: String,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutDetailState, WorkoutDetailEvent, WorkoutDetailEffect>(
    WorkoutDetailState()
) {

    init {
        onEvent(WorkoutDetailEvent.GetWorkoutById(workoutId))
    }

    override fun onEvent(event: WorkoutDetailEvent) {
        when(event) {
            is WorkoutDetailEvent.GetWorkoutById -> getWorkout(event.workoutId)
            is WorkoutDetailEvent.OnExerciseClicked -> {
                updateState { copy(currentExercise = event.exercise, showExerciseBottomSheet = true) }
            }
            is WorkoutDetailEvent.OnExerciseFavorited -> {

            }

            WorkoutDetailEvent.ToggleOnExerciseSheet -> {
                updateState { copy(showExerciseBottomSheet = !showExerciseBottomSheet) }
            }

            WorkoutDetailEvent.StartWorkout -> {
                sendEffect(WorkoutDetailEffect.NavigateToExercise)
            }
        }
    }

    private fun getWorkout(workoutId: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val workout = workoutRepository.getWorkout(workoutId)

            updateState { copy(isLoading = false, workout = workout) }
        }
    }
}

