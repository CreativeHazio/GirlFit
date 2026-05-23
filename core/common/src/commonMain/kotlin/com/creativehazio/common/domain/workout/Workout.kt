package com.creativehazio.common.domain.workout

data class Workout(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val details: String? = null,
    val duration: String = "",
    val level: WorkoutLevel = WorkoutLevel.NONE,
    val workoutType: WorkoutType = WorkoutType.TIME,
    val workoutCategory: WorkoutCategory = WorkoutCategory.ALL,
    val exercises: List<Exercise> = emptyList()
)


enum class WorkoutLevel {
    NONE,
    BEGINNER,
    INTERMEDIATE,
    EXPERT
}
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

data class Exercise(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val description: String = "",
    val thumbnailGifUrl: String = "",
    val gifUrl: String = "",
    val isFavourite: Boolean = false,
)