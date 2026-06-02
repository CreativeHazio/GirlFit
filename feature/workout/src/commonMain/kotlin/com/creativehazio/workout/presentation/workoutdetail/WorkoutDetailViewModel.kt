package com.creativehazio.workout.presentation.workoutdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import kotlinx.coroutines.launch

data class WorkoutDetailState(
    val isLoading: Boolean = false,
    val workout: Workout = Workout()
) : State

sealed interface WorkoutDetailEvent : Event {
    data class GetWorkoutById(val workoutId: String) : WorkoutDetailEvent
}

sealed interface WorkoutDetailEffect : Effect {}

class WorkoutDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutDetailState, WorkoutDetailEvent, WorkoutDetailEffect>(
    WorkoutDetailState()
) {

    override fun onEvent(event: WorkoutDetailEvent) {
        when(event) {
            is WorkoutDetailEvent.GetWorkoutById -> getWorkout(event.workoutId)
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

