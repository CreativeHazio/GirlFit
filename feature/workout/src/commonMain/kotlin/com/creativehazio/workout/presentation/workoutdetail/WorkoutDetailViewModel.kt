package com.creativehazio.workout.presentation.workoutdetail

import androidx.lifecycle.SavedStateHandle
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutType

data class WorkoutDetailState(
    val isLoading: Boolean = false,
    val workout: Workout = getDummyWorkout()
) : State

sealed interface WorkoutDetailEvent : Event {}

sealed interface WorkoutDetailEffect : Effect {}

class WorkoutDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val id: String
) : BaseViewModel<WorkoutDetailState, WorkoutDetailEvent, WorkoutDetailEffect>(
    WorkoutDetailState()
) {
    override fun onEvent(event: WorkoutDetailEvent) {

    }
}

fun getDummyWorkout() : Workout {
    val exercises = listOf(
        Exercise(
            id = "1",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "2",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "3",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = "",
            isFavourite = true
        ),
        Exercise(
            id = "4",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = "",
            isFavourite = true
        ),
        Exercise(
            id = "5",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "6",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "7",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "8",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),
        Exercise(
            id = "9",
            title = "Catcow pose",
            duration = "30 sec",
            description = "Lie on your back with knees bent, tighten your core, lift your knees toward your chest, then slowly lower your legs back down and repeat.",
            thumbnailGifUrl = "",
            gifUrl = ""
        ),

    )
    return Workout(
        id = "11",
        title = "De-Stress",
        imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        details = "",
        duration = "15 mins",
//        level = WorkoutLevel.BEGINNER,
        type = WorkoutType.TIME,
        category = WorkoutCategory.RELAX,
        exercises = exercises
    )
}

