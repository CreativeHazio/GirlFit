package com.creativehazio.data.workout.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.creativehazio.data.workout.data.local.WorkoutEntities.ExerciseEntity
import com.creativehazio.data.workout.data.local.WorkoutEntities.WorkoutEntity
import com.creativehazio.data.workout.data.local.WorkoutEntities.ChallengeDayEntity

private const val ID = "id"
private const val WORKOUT_ID = "workoutId"

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
        entityColumn = WORKOUT_ID
    )
    val challengeDays: List<ChallengeDayEntity>
)