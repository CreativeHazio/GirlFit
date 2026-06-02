package com.creativehazio.data.workout.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY createdAt DESC")
    fun getWorkouts() : PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts WHERE category = :category ORDER BY createdAt DESC LIMIT 5")
    fun getWorkoutsByCategoryAsFlow(category: String): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE category = :category ORDER BY createdAt DESC")
    fun getWorkoutsByCategory(category: String): PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY createdAt ASC LIMIT 1")
    suspend fun getLastWorkout(): WorkoutWithExercises?

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String) : WorkoutWithExercises

    @Query("DELETE FROM workouts")
    suspend fun clearAllWorkouts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM exercises")
    suspend fun clearAllExercises()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallengeDays(challengeDays: List<ChallengeDayEntity>)

    @Query("DELETE FROM challenge_days")
    suspend fun clearAllChallengeDays()

    @Transaction
    @Query("SELECT * FROM workouts WHERE cyclePhase = :phase AND category = 'RELAX'")
    fun getRelaxWorkoutByPhase(phase: String): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE cyclePhase = :phase AND category = 'STRENGTH'")
    fun getRecommendedWorkoutByPhase(phase: String): Flow<List<WorkoutWithExercises>>
}