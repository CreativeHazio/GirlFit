package com.creativehazio.data.workout.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_workout_selections")
data class HomeWorkoutSelectionEntity(
    @PrimaryKey
    val selectionId: String,

    // "RECOMMENDED" or "RELAX"
    val section: String,

    // Null for the relaxed workout.
    val cyclePhase: String?,

    val position: Int,
    val workoutId: String
)