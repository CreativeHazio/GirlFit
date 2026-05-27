package com.creativehazio.data.workout.data.remote

data class WorkoutDto(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val details: String? = null,
    val duration: String = "",
    val level: String = "NONE",
    val type: String = "TIME",
    val category: String = "ALL",
    val exercises: List<ExerciseDto> = emptyList(),
    val challenge: ChallengeDto? = null
)

data class ChallengeDto(
    val id: String = "",
    val challengeDays: List<ChallengeDayDto> = emptyList()
)

data class ChallengeDayDto(
    val id: String = "",
    val number: Int = 1,
    val workoutId: String = ""
)


data class ExerciseDto(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val description: String = "",
    val thumbnailGifUrl: String = "",
    val gifUrl: String = ""
)

data class WorkoutProgressDto(
    val workoutId: String,
    // After a user complete a workout, this date updates,for history page
    val lastCompleted: Long,
    val completedDayNumbers: List<Int>,
    val favouriteExerciseIds: List<String>
)