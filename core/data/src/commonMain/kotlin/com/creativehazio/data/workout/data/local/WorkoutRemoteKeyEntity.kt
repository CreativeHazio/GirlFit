package com.creativehazio.data.workout.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_remote_keys")
data class WorkoutRemoteKeyEntity(
    @PrimaryKey
    val id: String = "all_workouts",

    val lastTimestamp: Long
)