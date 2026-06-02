package com.creativehazio.data.workout.data.local

import androidx.room.Embedded
import androidx.room.Relation

private const val ID = "id"
private const val WORKOUT_ID = "workoutId"
private const val PARENT_CHALLENGE_ID = "parentChallengeId"

data class WorkoutWithExercises(
    @Embedded
    val workout: WorkoutEntity,

    @Relation(
        parentColumn = ID,
        entityColumn = WORKOUT_ID
    )
    val exercises: List<ExerciseEntity>,

    @Relation(
        parentColumn = ID,
        entityColumn = PARENT_CHALLENGE_ID
    )
    val challengeDays: List<ChallengeDayEntity>
)