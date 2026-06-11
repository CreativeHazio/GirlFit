package com.creativehazio.data.localdb

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.creativehazio.data.localdb.converters.StringListConverter
import com.creativehazio.data.meal.data.local.MealDao
import com.creativehazio.data.workout.data.local.ChallengeDayEntity
import com.creativehazio.data.workout.data.local.ExerciseEntity
import com.creativehazio.data.workout.data.local.WorkoutDao
import com.creativehazio.data.workout.data.local.WorkoutEntity
import com.creativehazio.data.workout.domain.Challenge

@Database(
    entities = [WorkoutEntity::class, ExerciseEntity::class, ChallengeDayEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
@ConstructedBy(GirlFitDatabaseConstructor::class)
abstract class GirlFitDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun mealDao(): MealDao
}

@Suppress("KotlinNoActualForExpect")
expect object GirlFitDatabaseConstructor : RoomDatabaseConstructor<GirlFitDatabase> {
    override fun initialize(): GirlFitDatabase
}