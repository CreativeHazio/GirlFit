package com.creativehazio.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.user.domain.User
import com.creativehazio.data.workout.domain.CycleRelatedWorkout
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.home.presentation.HomeEffect.*
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.angry_emoji
import girlfit.feature.home.generated.resources.exhausted_emoji
import girlfit.feature.home.generated.resources.sad_emoji
import girlfit.feature.home.generated.resources.sick_emoji
import girlfit.feature.home.generated.resources.smile_emoji
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

data class HomeState(
    val isLoading: Boolean = false,
    val user: User = User("", "Ria", "", CyclePhase.OVULATION),
    val userFeelings: Map<String, DrawableResource> = mapOf(
        "Happy" to Res.drawable.smile_emoji,
        "Sad" to Res.drawable.sad_emoji,
        "Sick" to Res.drawable.sick_emoji,
        "Tired" to Res.drawable.exhausted_emoji,
        "Angry" to Res.drawable.angry_emoji,
    ),
    val isRecommendedLoading: Boolean = true,
    val isRelaxLoading: Boolean = true,
    val recommendedWorkouts: List<Workout> = emptyList(),
    val relaxWorkouts: List<Workout> = emptyList(),
) : State

sealed interface HomeEvent : Event {
    data class OnWorkoutCardClicked(val workout: Workout) : HomeEvent
    data object ReloadRecommendedWorkouts : HomeEvent
    data object ReloadRelaxWorkouts : HomeEvent
}

sealed interface HomeEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String) : HomeEffect
    data class NavigateToWorkoutChallengeCalender(val workoutId: String) : HomeEffect
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    init {
        loadCycleRelatedWorkouts()
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnWorkoutCardClicked -> {
                when (event.workout.type) {
                    WorkoutType.CHALLENGE -> {
                        sendEffect(NavigateToWorkoutChallengeCalender(event.workout.id))
                    }

                    WorkoutType.TIME -> {
                        sendEffect(NavigateToWorkoutDetail(event.workout.id))
                    }
                }
            }
            HomeEvent.ReloadRecommendedWorkouts -> {
                updateState { copy(isRecommendedLoading = true) }
                loadRecommendedWorkouts()
            }
            HomeEvent.ReloadRelaxWorkouts -> {
                updateState { copy(isRelaxLoading = true) }
                loadRelaxWorkouts()
            }
        }
    }

    private fun loadRecommendedWorkouts() {
        getCycleRelatedWorkouts(
            limit = 2,
            cyclePhase = CyclePhase.OVULATION,
            cycleRelatedWorkout = CycleRelatedWorkout.RECOMMENDED
        )
    }

    private fun loadRelaxWorkouts() {
        getCycleRelatedWorkouts(
            limit = 1,
            cyclePhase = CyclePhase.OVULATION,
            cycleRelatedWorkout = CycleRelatedWorkout.RELAX
        )
    }

    private fun loadCycleRelatedWorkouts() {
        getCycleRelatedWorkouts(
            limit = 2,
            cyclePhase = CyclePhase.OVULATION,
            cycleRelatedWorkout = CycleRelatedWorkout.RECOMMENDED
        )
        getCycleRelatedWorkouts(
            limit = 1,
            cyclePhase = CyclePhase.OVULATION,
            cycleRelatedWorkout = CycleRelatedWorkout.RELAX
        )
    }

    private fun getCycleRelatedWorkouts(
        limit: Int,
        cyclePhase: CyclePhase,
        cycleRelatedWorkout: CycleRelatedWorkout
    ) {
        viewModelScope.launch {
            workoutRepository.getCycleRelatedWorkouts(
                limit = limit,
                currentCyclePhase = cyclePhase,
                cycleRelatedWorkout = cycleRelatedWorkout
            ).catch {
                updateState {
                    when (cycleRelatedWorkout) {
                        CycleRelatedWorkout.RECOMMENDED -> copy(isRecommendedLoading = false)
                        CycleRelatedWorkout.RELAX -> copy(isRelaxLoading = false)
                    }
                }
            }
                .collect {
                    updateState {
                        when (cycleRelatedWorkout) {
                            CycleRelatedWorkout.RECOMMENDED -> copy(
                                recommendedWorkouts = it,
                                isRecommendedLoading = false
                            )

                            CycleRelatedWorkout.RELAX -> copy(
                                relaxWorkouts = it,
                                isRelaxLoading = false
                            )
                        }
                    }
                }
        }
    }
}