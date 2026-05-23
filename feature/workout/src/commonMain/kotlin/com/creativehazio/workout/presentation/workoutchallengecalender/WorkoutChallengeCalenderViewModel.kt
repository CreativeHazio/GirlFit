package com.creativehazio.workout.presentation.workoutchallengecalender

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State

data class WorkoutChallengeCalenderState(
    val isLoading: Boolean = false
) : State

sealed interface WorkoutChallengeCalenderEvent : Event {}

sealed interface WorkoutChallengeCalenderEffect : Effect {}

class WorkoutChallengeCalenderViewModel(
) : BaseViewModel<WorkoutChallengeCalenderState, WorkoutChallengeCalenderEvent, WorkoutChallengeCalenderEffect>(
    WorkoutChallengeCalenderState()
) {
    override fun onEvent(event: WorkoutChallengeCalenderEvent) {

    }
}