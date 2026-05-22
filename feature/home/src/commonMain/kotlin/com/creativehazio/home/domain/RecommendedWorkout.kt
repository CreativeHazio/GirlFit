package com.creativehazio.home.domain

import com.creativehazio.common.domain.WorkoutType

data class RecommendedWorkout(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val detailsText: String? = null,
    val durationText: String = "",
    val workoutType: WorkoutType = WorkoutType.TIME
)
