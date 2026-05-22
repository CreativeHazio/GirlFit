package com.creativehazio.common.domain.workout

import com.creativehazio.common.domain.workout.WorkoutType

data class Workout(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val detailsText: String? = null,
    val durationText: String = "",
    val workoutType: WorkoutType = WorkoutType.TIME,
    val workoutCategory: WorkoutCategory = WorkoutCategory.ALL
)

enum class WorkoutType {
    CHALLENGE,
    TIME
}

enum class WorkoutCategory {
    ALL,
    CHALLENGE,
    YOGA,
    QUICK,
    STRENGTH,
    RECOMMENDED,
    RELAX
}