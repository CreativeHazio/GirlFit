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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    data class OnWorkoutCardClicked(val workout: Workout, val sharedElementKey: String) : HomeEvent
    data object ReloadRecommendedWorkouts : HomeEvent
    data object ReloadRelaxWorkouts : HomeEvent
}

sealed interface HomeEffect : Effect {
    data class NavigateToWorkoutDetail(val workoutId: String, val sharedElementKey: String) :
        HomeEffect

    data class NavigateToWorkoutChallengeCalender(
        val workoutId: String,
        val sharedElementKey: String
    ) : HomeEffect
}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    init {
        observeWorkouts()
        refreshRelaxWorkouts()
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnWorkoutCardClicked -> {
                when (event.workout.type) {
                    WorkoutType.CHALLENGE -> {
                        sendEffect(
                            NavigateToWorkoutChallengeCalender(
                                event.workout.id,
                                event.sharedElementKey
                            )
                        )
                    }

                    WorkoutType.TIME -> {
                        sendEffect(
                            NavigateToWorkoutDetail(
                                event.workout.id,
                                event.sharedElementKey
                            )
                        )
                    }
                }
            }

            HomeEvent.ReloadRecommendedWorkouts -> {
                refreshRecommendedWorkouts(
                    uiState.value.user.cyclePhase
                )
            }

            HomeEvent.ReloadRelaxWorkouts -> {
                refreshRelaxWorkouts()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeWorkouts() {
        viewModelScope.launch {
            uiState
                .map { it.user.cyclePhase }
                .distinctUntilChanged()
                .onEach { phase ->
                    refreshRecommendedWorkouts(phase)
                }
                .flatMapLatest { phase ->
                    workoutRepository.observeRecommendedWorkouts(
                        cyclePhase = phase.name
                    )
                }
                .collect { workouts ->
                    updateState {
                        copy(
                            recommendedWorkouts = workouts
                        )
                    }
                }
        }

        viewModelScope.launch {
            workoutRepository.observeRelaxWorkouts()
                .collect { workouts ->
                    updateState {
                        copy(
                            relaxWorkouts = workouts
                        )
                    }
                }
        }
    }

    private fun refreshRecommendedWorkouts(
        cyclePhase: CyclePhase
    ) {
        viewModelScope.launch {
            updateState {
                copy(
                    isRecommendedLoading = recommendedWorkouts.isEmpty()
                )
            }

            try {
                workoutRepository.refreshRecommendedWorkouts(
                    cyclePhase = cyclePhase,
                    limit = 2
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // An older request should not change the loading
                // state after the user has switched cycle phases.
                if (uiState.value.user.cyclePhase == cyclePhase) {
                    updateState {
                        copy(isRecommendedLoading = false)
                    }
                }
            }
        }
    }

    private fun refreshRelaxWorkouts() {
        viewModelScope.launch {
            updateState { copy(isRelaxLoading = relaxWorkouts.isEmpty()) }

            runCatching {
                workoutRepository.refreshRelaxWorkouts(
                    limit = 1
                )
            }

            updateState { copy(isRelaxLoading = false) }
        }
    }
}