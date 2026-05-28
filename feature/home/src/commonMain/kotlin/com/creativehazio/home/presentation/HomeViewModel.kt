package com.creativehazio.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import kotlinx.coroutines.launch

data class HomeState(
    val isLoading: Boolean = false,
    val recommendedWorkouts: List<Workout> = emptyList(),
    val relaxWorkouts: List<Workout> = emptyList(),
) : State

sealed interface HomeEvent : Event {
    data class OnWorkoutCardClicked(val workout: Workout) : HomeEvent
}

sealed interface HomeEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String) : HomeEffect
    data class NavigateToWorkoutChallengeCalender(val workoutId: String) : HomeEffect
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    init {
        getRecommendedWorkouts()
        getRelaxWorkouts()
    }

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

    private fun getRecommendedWorkouts() {
        viewModelScope.launch {
            workoutRepository.getRecommendedWorkouts(CyclePhase.OVULATION).collect {
                updateState { copy(recommendedWorkouts = it) }
            }
        }
    }

    private fun getRelaxWorkouts() {
        viewModelScope.launch {
            workoutRepository.getRelaxWorkouts().collect {
                updateState { copy(relaxWorkouts = it) }
            }
        }
    }
}