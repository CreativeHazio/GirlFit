package com.creativehazio.workout.presentation.workout

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.domain.workout.Workout
import com.creativehazio.common.domain.workout.WorkoutCategory
import com.creativehazio.common.domain.workout.WorkoutType
import com.creativehazio.workout.presentation.workoutdetail.getDummyWorkout
import kotlinx.coroutines.launch

data class WorkoutState(
    val isLoading: Boolean = false,
    val workouts: List<Workout> = listOf(
        getDummyWorkout(),
        getDummyWorkout(),
        getDummyWorkout(),
        getDummyWorkout(),
        getDummyWorkout(),
        getDummyWorkout(),
    )
) : State

sealed interface WorkoutEvent : Event {

    data class OnSearchWorkoutClicked(val query: String) : WorkoutEvent
    data class OnWorkoutCategoryPillClicked(val category: WorkoutCategory) : WorkoutEvent
    data object OnPersonalCardClicked : WorkoutEvent
    data object OnFavouriteInfoBubbleClicked : WorkoutEvent
}

sealed interface WorkoutEffect : Effect {
    data object NavigateToPersonalPlan : WorkoutEffect
    data object NavigateToFavourite : WorkoutEffect
}

class WorkoutViewModel(
) : BaseViewModel<WorkoutState, WorkoutEvent, WorkoutEffect>(
    WorkoutState()
) {
    override fun onEvent(event: WorkoutEvent) {
        when(event) {
            is WorkoutEvent.OnWorkoutCategoryPillClicked -> {
                val workouts = when(event.category) {
                    WorkoutCategory.ALL -> {
                        listOf(
                            getDummyWorkout(),
                            getDummyWorkout(),
                            getDummyWorkout(),
                            getDummyWorkout(),
                            getDummyWorkout(),
                            getDummyWorkout(),
                        )
                    }
                    else -> {
                        uiState.value.workouts.filter { it.category == event.category }
                    }
                }
                updateState {
                    copy(
                        workouts = workouts
                    )
                }
            }

            WorkoutEvent.OnFavouriteInfoBubbleClicked -> {
                sendEffect(WorkoutEffect.NavigateToFavourite)
            }
            WorkoutEvent.OnPersonalCardClicked -> {
                sendEffect(WorkoutEffect.NavigateToPersonalPlan)
            }

            is WorkoutEvent.OnSearchWorkoutClicked -> searchWorkout(event.query)
        }
    }

    private fun searchWorkout(query: String) {
        viewModelScope.launch {

        }
    }
}