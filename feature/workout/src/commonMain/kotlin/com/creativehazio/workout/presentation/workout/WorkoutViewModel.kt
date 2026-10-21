package com.creativehazio.workout.presentation.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val WORKOUT_CATEGORY = "category"

data class WorkoutState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val workoutCategory: WorkoutCategory = WorkoutCategory.ALL,
) : State

sealed interface WorkoutEvent : Event {

    data class OnSearchQueryChange(val query: String) : WorkoutEvent
    data class OnWorkoutCategoryPillClicked(val category: WorkoutCategory) : WorkoutEvent
    data class OnWorkoutCardClicked(val workout: Workout, val sharedElementKey: String) : WorkoutEvent
    data object OnPersonalCardClicked : WorkoutEvent
    data object OnFavouriteInfoBubbleClicked : WorkoutEvent
}

sealed interface WorkoutEffect : Effect {
    data object NavigateToPersonalPlan : WorkoutEffect
    data object NavigateToFavourite : WorkoutEffect
    data class NavigateToWorkoutDetail(val workoutId: String, val sharedElementKey: String) : WorkoutEffect
    data class NavigateToWorkoutChallengeCalender(val workoutId: String, val sharedElementKey: String) : WorkoutEffect
}

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<WorkoutState, WorkoutEvent, WorkoutEffect>(
    WorkoutState(
        workoutCategory = savedStateHandle[WORKOUT_CATEGORY] ?: WorkoutCategory.ALL
    )
) {

    @OptIn(FlowPreview::class)
    val workouts = combine (
        uiState.map {
            it.workoutCategory
        }.distinctUntilChanged(),
        uiState.map {
            it.searchQuery.trim()
        }.distinctUntilChanged()
            .debounce(500)
    ) { category, searchQuery ->
        category to searchQuery
    }.flatMapLatest { (category, searchQuery) ->
        workoutRepository.getWorkouts(category, searchQuery)
    }.cachedIn(viewModelScope)

    override fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.OnWorkoutCategoryPillClicked -> {
                savedStateHandle[WORKOUT_CATEGORY] = event.category
                updateState { copy(workoutCategory = event.category) }
            }

            WorkoutEvent.OnFavouriteInfoBubbleClicked -> {
                sendEffect(WorkoutEffect.NavigateToFavourite)
            }

            WorkoutEvent.OnPersonalCardClicked -> {
                sendEffect(WorkoutEffect.NavigateToPersonalPlan)
            }

            is WorkoutEvent.OnSearchQueryChange -> {
                updateState { copy(searchQuery = event.query) }
            }
            is WorkoutEvent.OnWorkoutCardClicked -> {
                when (event.workout.type) {
                    WorkoutType.CHALLENGE -> sendEffect(
                        WorkoutEffect.NavigateToWorkoutChallengeCalender(
                            event.workout.id,
                            event.sharedElementKey
                        )
                    )

                    WorkoutType.TIME -> sendEffect(WorkoutEffect.NavigateToWorkoutDetail(
                        event.workout.id,
                        event.sharedElementKey
                    ))
                }
            }
        }
    }
}