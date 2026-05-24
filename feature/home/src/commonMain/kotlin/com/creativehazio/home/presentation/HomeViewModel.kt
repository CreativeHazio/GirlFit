package com.creativehazio.home.presentation

import androidx.lifecycle.SavedStateHandle
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.domain.workout.Workout
import com.creativehazio.common.domain.workout.WorkoutType

data class HomeState(
    val isLoading: Boolean = false
) : State

sealed interface HomeEvent : Event {
    data class OnWorkoutCardClicked(val workout: Workout) : HomeEvent
}

sealed interface HomeEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String) : HomeEffect
    data class NavigateToWorkoutChallengeCalender(val workoutId: String) : HomeEffect
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    override fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OnWorkoutCardClicked -> {
                when(event.workout.type) {
                    WorkoutType.CHALLENGE -> {
                        sendEffect(HomeEffect.NavigateToWorkoutChallengeCalender(event.workout.id))
                    }
                    WorkoutType.TIME -> {
                        sendEffect(HomeEffect.NavigateToWorkoutDetail(event.workout.id))
                    }
                }
            }
        }
    }
}