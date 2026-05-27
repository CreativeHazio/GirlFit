package com.creativehazio.data.localdb

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import com.creativehazio.data.workout.data.local.WorkoutDao
import com.creativehazio.data.workout.data.local.WorkoutEntities

@Database(
    entities = [WorkoutEntities::class],
    version = 1
)
abstract class GirlFitDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}