package com.creativehazio.data.workout.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val WORKOUT = "workouts"
private const val EXERCISE = "exercises"
private const val CHALLENGE_DAY = "challenge_days"

@Entity(tableName = WORKOUT)
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val title: String,
    val imageUrl: String,
    val details: String?,
    val duration: Int,
    val cyclePhase: String? = null,
    val level: String,
    val type: String,
    val category: String,
)

@Entity(tableName = CHALLENGE_DAY)
data class ChallengeDayEntity(
    @PrimaryKey val challengeDayId: String,
    val parentChallengeId: String,
    val workoutId: String,
    val number: Int,
    val state: String
)

@Entity(tableName = EXERCISE)
data class ExerciseEntity(
    @PrimaryKey val exerciseId: String,
    val workoutId: String,
    val title: String,
    val duration: Int,
    val description: String,
    val thumbnailGifUrl: String,
    val gifUrl: String,
    val isFavourite: Boolean,
)