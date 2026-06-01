package com.creativehazio.data.workout.domain

data class Workout(
    val id: String = "",
    val createdAt: Long = 0L,
    val title: String = "",
    val imageUrl: String = "",
    val duration: Int = 0,
    val details: String? = null,
    val level: WorkoutLevel = WorkoutLevel.NONE,
    val type: WorkoutType = WorkoutType.TIME,
    val category: WorkoutCategory = WorkoutCategory.ALL,
    val exercises: List<Exercise> = emptyList(),
    val challenge: Challenge = Challenge(),
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

data class Challenge(
    val id: String = "",
    val challengeDays: List<ChallengeDay> = emptyList(),
) {

    companion object {
        const val DAYS_IN_A_WEEK = 7
    }

    fun getChallengeWeeksFromDays() : Int {
        return challengeDays.size / DAYS_IN_A_WEEK
    }

    val challengeTitle: String
        get() {
            val weeks = getChallengeWeeksFromDays()

            return "${DAYS_IN_A_WEEK}x$weeks"
        }

    fun isWeekCompleted(weekNumber: Int): Boolean {
        val startDay = (weekNumber - 1) * DAYS_IN_A_WEEK + 1
        val endDay = weekNumber * DAYS_IN_A_WEEK

        val daysInWeek = challengeDays.filter { it.number in startDay..endDay }

        if (daysInWeek.isEmpty()) return false

        return daysInWeek
            .all { it.state == ChallengeDayState.COMPLETED }
    }
}

data class ChallengeDay(
    val id: String = "",
    val number: Int = 1,
    val state: ChallengeDayState = ChallengeDayState.COMPLETED,
    val workoutId: String = ""
) {
    val weekNumber: Int
        get() = (number - 1) / Challenge.DAYS_IN_A_WEEK + 1
}

enum class ChallengeDayState {
    COMPLETED, CURRENT, UPCOMING
}

enum class WorkoutCategory {
    ALL,
    CHALLENGE,
    YOGA,
    QUICK,
    STRENGTH,
    RELAX
}

data class Exercise(
    val id: String = "",
    val title: String = "",
    val duration: Int = 0,
    val description: String = "",
    val thumbnailGifUrl: String = "",
    val gifUrl: String = "",
    val isFavourite: Boolean = false,
)