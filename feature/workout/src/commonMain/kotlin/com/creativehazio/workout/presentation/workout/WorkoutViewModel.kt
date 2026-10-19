package com.creativehazio.workout.presentation.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

private const val WORKOUT_CATEGORY = "category"

data class WorkoutState(
    val isLoading: Boolean = false,
    val workouts: Flow<PagingData<Workout>> = flowOf(PagingData.empty()),
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

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<WorkoutState, WorkoutEvent, WorkoutEffect>(
    WorkoutState()
) {

    private val _workoutCategory =
        MutableStateFlow(savedStateHandle[WORKOUT_CATEGORY] ?: WorkoutCategory.ALL)
    val workoutCategory = _workoutCategory.asStateFlow()

    val workouts = workoutCategory
        .flatMapLatest { category ->
            workoutRepository.getWorkouts(category)
        }
        .cachedIn(viewModelScope)

    override fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.OnWorkoutCategoryPillClicked -> {
                savedStateHandle[WORKOUT_CATEGORY] = event.category
                _workoutCategory.update { event.category }
            }

            WorkoutEvent.OnFavouriteInfoBubbleClicked -> {
                sendEffect(WorkoutEffect.NavigateToFavourite)
            }

            WorkoutEvent.OnPersonalCardClicked -> {
                sendEffect(WorkoutEffect.NavigateToPersonalPlan)
            }

            is WorkoutEvent.OnSearchWorkoutClicked -> searchWorkout(event.query)
            is WorkoutEvent.OnWorkoutCardClicked -> {
                when (event.workout.type) {
                    WorkoutType.CHALLENGE -> sendEffect(
                        WorkoutEffect.NavigateToWorkoutChallengeCalender(
                            event.workout.id
                        )
                    )

                    WorkoutType.TIME -> sendEffect(WorkoutEffect.NavigateToWorkoutDetail(event.workout.id))
                }
            }
        }
    }

    private fun searchWorkout(query: String) {
        viewModelScope.launch {

        }
    }
}