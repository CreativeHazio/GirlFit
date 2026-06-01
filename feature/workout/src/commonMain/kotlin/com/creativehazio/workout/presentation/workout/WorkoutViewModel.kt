package com.creativehazio.workout.presentation.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.room.RoomMasterTable.NAME
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private const val WORKOUT_CATEGORY = "category"

data class WorkoutState(
    val isLoading: Boolean = false,
    val workouts: Flow<PagingData<Workout>> = flowOf(PagingData.empty()),
    val workoutCategory: WorkoutCategory = WorkoutCategory.ALL
) : State

sealed interface WorkoutEvent : Event {

    data class OnSearchWorkoutClicked(val query: String) : WorkoutEvent
    data class OnWorkoutCategoryPillClicked(val category: WorkoutCategory) : WorkoutEvent
    data class OnWorkoutCardClicked(val workout: Workout) : WorkoutEvent
    data object OnPersonalCardClicked : WorkoutEvent
    data object OnFavouriteInfoBubbleClicked : WorkoutEvent
}

sealed interface WorkoutEffect : Effect {
    data object NavigateToPersonalPlan : WorkoutEffect
    data object NavigateToFavourite : WorkoutEffect
    data class NavigateToWorkoutDetail(val workoutId: String) : WorkoutEffect
    data class NavigateToWorkoutChallengeCalender(val workoutId: String) : WorkoutEffect
}

class WorkoutViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<WorkoutState, WorkoutEvent, WorkoutEffect>(
    WorkoutState(
        workoutCategory = savedStateHandle[WORKOUT_CATEGORY] ?: WorkoutCategory.ALL,
    )
) {

    init {
        getWorkouts(uiState.value.workoutCategory)
    }

    override fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.OnWorkoutCategoryPillClicked -> {
                savedStateHandle[WORKOUT_CATEGORY] = event.category
                updateState { copy(workoutCategory = event.category) }
                getWorkouts(event.category)
            }

            WorkoutEvent.OnFavouriteInfoBubbleClicked -> {
                sendEffect(WorkoutEffect.NavigateToFavourite)
            }

            WorkoutEvent.OnPersonalCardClicked -> {
                sendEffect(WorkoutEffect.NavigateToPersonalPlan)
            }

            is WorkoutEvent.OnSearchWorkoutClicked -> searchWorkout(event.query)
            is WorkoutEvent.OnWorkoutCardClicked -> {
                when(event.workout.type) {
                    WorkoutType.CHALLENGE -> sendEffect(WorkoutEffect.NavigateToWorkoutChallengeCalender(event.workout.id))
                    WorkoutType.TIME -> sendEffect(WorkoutEffect.NavigateToWorkoutDetail(event.workout.id))
                }
            }
        }
    }

    private fun searchWorkout(query: String) {
        viewModelScope.launch {

        }
    }

    private fun getWorkouts(category: WorkoutCategory) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val workouts = workoutRepository.getWorkouts(category).cachedIn(viewModelScope)
            updateState { copy(workouts = workouts, isLoading = false) }
        }
    }
}