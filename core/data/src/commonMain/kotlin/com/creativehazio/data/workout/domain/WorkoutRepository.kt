package com.creativehazio.data.workout.domain

import androidx.paging.PagingData
import com.creativehazio.data.user.domain.CyclePhase
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(category: WorkoutCategory, searchQuery: String): Flow<PagingData<Workout>>
    suspend fun getWorkout(workoutId: String): Workout

    fun observeRecommendedWorkouts(cyclePhase: String): Flow<List<Workout>>

    fun observeRelaxWorkouts(): Flow<List<Workout>>

    suspend fun refreshRelaxWorkouts(limit: Int = 1)
    suspend fun refreshRecommendedWorkouts(cyclePhase: CyclePhase, limit: Int = 2)
}