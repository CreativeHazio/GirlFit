package com.creativehazio.workout.presentation.workoutdetail

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutRepository
import girlfit.feature.workout.generated.resources.Res
import kotlinx.coroutines.launch

data class WorkoutDetailState(
    val isLoading: Boolean = false,
    val showExerciseBottomSheet: Boolean = false,
    val currentExercise: Exercise = Exercise(),
    val workout: Workout = Workout()
) : State

sealed interface WorkoutDetailEvent : Event {
    data class GetWorkoutById(val workoutId: String) : WorkoutDetailEvent
    data class OnExerciseClicked(val exercise: Exercise) : WorkoutDetailEvent
    data object OnNextExerciseOnSheetClicked : WorkoutDetailEvent
    data object OnPreviousExerciseOnSheetClicked : WorkoutDetailEvent
    data object ToggleOnExerciseSheet : WorkoutDetailEvent
    data class OnExerciseFavorited(val exercise: Exercise) : WorkoutDetailEvent

    data object StartWorkout : WorkoutDetailEvent
}

sealed interface WorkoutDetailEffect : Effect {
    data object NavigateToExercise : WorkoutDetailEffect
}

class WorkoutDetailViewModel(
    workoutId: String,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutDetailState, WorkoutDetailEvent, WorkoutDetailEffect>(
    WorkoutDetailState()
) {

    init {
        onEvent(WorkoutDetailEvent.GetWorkoutById(workoutId))
    }

    override fun onEvent(event: WorkoutDetailEvent) {
        when (event) {
            is WorkoutDetailEvent.GetWorkoutById -> getWorkout(event.workoutId)
            is WorkoutDetailEvent.OnExerciseClicked -> {
                updateState {
                    copy(
                        currentExercise = event.exercise,
                        showExerciseBottomSheet = true
                    )
                }
            }

            is WorkoutDetailEvent.OnExerciseFavorited -> {
                updateState {
                    val updatedExercises = workout.exercises.map { exercise ->
                        if (exercise.id == event.exercise.id) {
                            exercise.copy(isFavourite = !exercise.isFavourite)
                        } else {
                            exercise
                        }
                    }

                    copy(workout = workout.copy(exercises = updatedExercises))
                }
            }

            WorkoutDetailEvent.ToggleOnExerciseSheet -> {
                updateState { copy(showExerciseBottomSheet = !showExerciseBottomSheet) }
            }

            WorkoutDetailEvent.StartWorkout -> {
                sendEffect(WorkoutDetailEffect.NavigateToExercise)
            }

            WorkoutDetailEvent.OnNextExerciseOnSheetClicked -> {
                updateState {
                    val currentExerciseIndex = workout.exercises.indexOf(currentExercise)
                    val nextExerciseIndex = currentExerciseIndex + 1
                    val nextExercise = workout.exercises[nextExerciseIndex]
                    copy(currentExercise = nextExercise)
                }
            }
            WorkoutDetailEvent.OnPreviousExerciseOnSheetClicked -> {
                updateState {
                    val currentExerciseIndex = workout.exercises.indexOf(currentExercise)
                    val previousExerciseIndex = currentExerciseIndex - 1
                    val previousExercise = workout.exercises[previousExerciseIndex]
                    copy(currentExercise = previousExercise)
                }
            }
        }
    }

    private fun getWorkout(workoutId: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val workout = workoutRepository.getWorkout(workoutId)

            val lottieOne = Res.readBytes("files/pushup.json").decodeToString()
            val lottieTwo = Res.readBytes("files/lunges.json").decodeToString()
            val pool = listOf(lottieOne, lottieTwo)

            val modifiedWorkout = workout.copy(
                exercises = workout.exercises.map { exercise ->
                    val randomAnimation = pool.random()
                    exercise.copy(
                        thumbnailGifUrl = randomAnimation,
                        gifUrl = randomAnimation
                    )
                }
            )

            updateState { copy(isLoading = false, workout = modifiedWorkout) }
        }
    }
}

