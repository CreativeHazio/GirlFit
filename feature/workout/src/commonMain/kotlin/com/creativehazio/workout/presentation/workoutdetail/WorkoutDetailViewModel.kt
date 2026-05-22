package com.creativehazio.workout.presentation.workoutdetail

import androidx.lifecycle.SavedStateHandle
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State

data class WorkoutDetailState(
    val isLoading: Boolean = false
) : State

sealed interface WorkoutDetailEvent : Event {}

sealed interface WorkoutDetailEffect : Effect {}

class WorkoutDetailViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<WorkoutDetailState, WorkoutDetailEvent, WorkoutDetailEffect>(
    WorkoutDetailState()
) {
    override fun onEvent(event: WorkoutDetailEvent) {

    }
}

