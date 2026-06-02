package com.creativehazio.data.workout.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDto(
    val id: String = "",
    val createdAt: Long = 0L,
    val title: String = "",
    val imageUrl: String = "",
    val details: String? = null,
    val duration: Int = 0,
    val level: String = "NONE",
    val type: String = "TIME",
    val category: String = "ALL",
    val exercises: List<ExerciseDto> = emptyList(),
    val challenge: ChallengeDto? = null,
)

@Serializable
data class ChallengeDto(
    val id: String = "",
    val challengeDays: List<ChallengeDayDto> = emptyList()
)

@Serializable
data class ChallengeDayDto(
    val id: String = "",
    val number: Int = 1,
    val workoutId: String = ""
)


@Serializable
data class ExerciseDto(
    val id: String = "",
    val title: String = "",
    val duration: Int = 0,
    val description: String = "",
    val thumbnailGifUrl: String = "",
    val gifUrl: String = ""
)

data class WorkoutProgressDto(
    val workoutId: String,
    // After a user complete a workout, this date updates,for history page
    val lastCompleted: Long,
    val streakNumber: Int,
    val completedDayNumbers: List<Int>,
    val favouriteExerciseIds: List<String>
)