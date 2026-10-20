package com.creativehazio.data.workout.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val CYCLE_RELATED_WORKOUT = "cycle_related_workouts"
private const val CYCLE_RELATED_EXERCISE = "cycle_related_exercises"
private const val CYCLES_RELATED_CHALLENGE_DAY = "cycles_related_challenge_days"

@Entity(tableName = CYCLE_RELATED_WORKOUT)
data class CycleRelatedWorkoutEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val title: String,
    val imageUrl: String,
    val details: String?,
    val duration: Int,
    val cyclePhase: String,
    val level: String,
    val type: String,
    val category: String,
)

@Entity(tableName = CYCLES_RELATED_CHALLENGE_DAY)
data class CycleRelatedChallengeDayEntity(
    @PrimaryKey val challengeDayId: String,
    val parentChallengeId: String,
    val workoutId: String,
    val number: Int,
    val state: String
)

@Entity(tableName = CYCLE_RELATED_EXERCISE)
data class CycleRelatedExerciseEntity(
    @PrimaryKey val exerciseId: String,
    val workoutId: String,
    val title: String,
    val duration: Int,
    val description: String,
    val thumbnailGifUrl: String,
    val gifUrl: String,
    val isFavourite: Boolean,
)