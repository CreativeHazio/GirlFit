package com.creativehazio.data.workout.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutWithExercises() : PagingSource<Int, WorkoutWithExercises>
}