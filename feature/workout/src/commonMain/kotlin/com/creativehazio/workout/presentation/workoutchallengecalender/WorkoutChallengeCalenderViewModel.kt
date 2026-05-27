package com.creativehazio.workout.presentation.workoutchallengecalender

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.ChallengeDay
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.workout.presentation.workoutdetail.getDummyWorkout

data class WorkoutChallengeCalenderState(
    val isLoading: Boolean = false,
    val workout: Workout = Workout(
        title = "De-Stress",
        challenge = Challenge(
            challengeDays = listOf(
                ChallengeDay(
                    number = 1,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 2,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 3,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 4,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 5,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 6,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 7,
                    state = ChallengeDayState.COMPLETED,
                ),
                ChallengeDay(
                    number = 8,
                    state = ChallengeDayState.CURRENT,
                    workout = getDummyWorkout()
                ),
                ChallengeDay(
                    number = 9,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 10,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 11,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 12,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 13,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 14,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 15,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 16,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 17,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 18,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 19,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 20,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 21,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 22,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 23,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 24,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 25,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 26,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 27,
                    state = ChallengeDayState.UPCOMING,
                ),
                ChallengeDay(
                    number = 28,
                    state = ChallengeDayState.UPCOMING,
                ),
            )
        )
    )
) : State

sealed interface WorkoutChallengeCalenderEvent : Event {
    data class OnChallengeDayClicked(val workoutId: String) : WorkoutChallengeCalenderEvent
    data object OnBackClicked : WorkoutChallengeCalenderEvent
}

sealed interface WorkoutChallengeCalenderEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String) : WorkoutChallengeCalenderEffect
    data object NavigateBack : WorkoutChallengeCalenderEffect
}

class WorkoutChallengeCalenderViewModel (
    private val id: String
) : BaseViewModel<WorkoutChallengeCalenderState, WorkoutChallengeCalenderEvent, WorkoutChallengeCalenderEffect>(
    WorkoutChallengeCalenderState()
) {
    override fun onEvent(event: WorkoutChallengeCalenderEvent) {
        when(event) {
            WorkoutChallengeCalenderEvent.OnBackClicked -> {
                sendEffect(WorkoutChallengeCalenderEffect.NavigateBack)
            }
            is WorkoutChallengeCalenderEvent.OnChallengeDayClicked -> {
                sendEffect(WorkoutChallengeCalenderEffect.NavigateToWorkoutDetail(event.workoutId))
            }
        }
    }
}