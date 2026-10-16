package com.creativehazio.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.user.domain.User
import com.creativehazio.data.workout.data.Seeder
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.domain.WorkoutType
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.angry_emoji
import girlfit.feature.home.generated.resources.exhausted_emoji
import girlfit.feature.home.generated.resources.sad_emoji
import girlfit.feature.home.generated.resources.sick_emoji
import girlfit.feature.home.generated.resources.smile_emoji
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
    private val workoutRepository: WorkoutRepository,
    private val girlFitDatabase: GirlFitDatabase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    init {
        viewModelScope.launch {
            // TODO: Fix this making the workouts disappear
            val existingWorkout = girlFitDatabase.workoutDao().getLastWorkout()

            if (existingWorkout == null) {
                val seeder = Seeder(girlFitDatabase = girlFitDatabase)
                seeder.addRelaxAndRecommendedWorkouts()
            }
        }
        getRecommendedWorkouts(CyclePhase.MENSTRUAL)
        getRelaxWorkouts(CyclePhase.MENSTRUAL)
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

    private fun getRecommendedWorkouts(cyclePhase: CyclePhase) {
        viewModelScope.launch {
            workoutRepository.getRecommendedWorkouts(cyclePhase).collect {
                updateState { copy(recommendedWorkouts = it) }
            }
        }
    }

    private fun getRelaxWorkouts(cyclePhase: CyclePhase) {
        viewModelScope.launch {
            workoutRepository.getRelaxWorkouts(cyclePhase).collect {
                updateState { copy(relaxWorkouts = it) }
            }
        }
    }
}