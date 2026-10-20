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
    @Query(
        """
    SELECT * FROM workouts
    WHERE :query = '' OR title LIKE '%' || :query || '%'
    ORDER BY createdAt DESC
    """
    )
    fun getWorkouts(query: String): PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query(
        """
    SELECT * FROM workouts
    WHERE category = :category
    AND (:query = '' OR title LIKE '%' || :query || '%')
    ORDER BY createdAt DESC
    """
    )
    fun getWorkoutsByCategory(category: String, query: String): PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY createdAt ASC LIMIT 1")
    suspend fun getLastWorkout(): WorkoutWithExercises?

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String): WorkoutWithExercises

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycleRelatedWorkouts(workouts: List<CycleRelatedWorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycleRelatedExercises(exercises: List<CycleRelatedExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycleRelatedChallengeDays(challengeDays: List<CycleRelatedChallengeDayEntity>)

    @Transaction
    @Query("SELECT * FROM cycle_related_workouts WHERE cyclePhase = :phase AND category = 'RELAX' LIMIT 1")
    fun getRelaxWorkoutByPhase(phase: String): Flow<List<CycleRelatedWorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM cycle_related_workouts WHERE cyclePhase = :phase AND category = 'STRENGTH' LIMIT 2")
    fun getRecommendedWorkoutByPhase(phase: String): Flow<List<CycleRelatedWorkoutWithExercises>>
}