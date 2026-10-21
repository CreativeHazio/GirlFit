package com.creativehazio.data.workout.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface WorkoutRemoteKeyDao {

    @Query(
        """
        SELECT * FROM workout_remote_keys
        WHERE id = 'all_workouts'
        LIMIT 1
        """
    )
    suspend fun getAllWorkoutsKey(): WorkoutRemoteKeyEntity?

    @Upsert
    suspend fun upsert(key: WorkoutRemoteKeyEntity)

    @Query(
        """
        DELETE FROM workout_remote_keys
        WHERE id = 'all_workouts'
        """
    )
    suspend fun clearAllWorkoutsKey()
}