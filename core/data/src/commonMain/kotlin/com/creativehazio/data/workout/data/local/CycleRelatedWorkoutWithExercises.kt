package com.creativehazio.data.workout.data.local

import androidx.room.Embedded
import androidx.room.Relation

private const val ID = "id"
private const val WORKOUT_ID = "workoutId"
private const val PARENT_CHALLENGE_ID = "parentChallengeId"

data class CycleRelatedWorkoutWithExercises(
    @Embedded
    val workout: CycleRelatedWorkoutEntity,

    @Relation(
        parentColumn = ID,
        entityColumn = WORKOUT_ID
    )
    val exercises: List<CycleRelatedExerciseEntity>,

    @Relation(
        parentColumn = ID,
        entityColumn = PARENT_CHALLENGE_ID
    )
    val challengeDays: List<CycleRelatedChallengeDayEntity>
)