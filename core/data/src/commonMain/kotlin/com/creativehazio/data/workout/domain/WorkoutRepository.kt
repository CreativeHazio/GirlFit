package com.creativehazio.data.workout.domain

import androidx.paging.PagingData
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.workout.data.local.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(category: WorkoutCategory) : Flow<PagingData<Workout>>
    suspend fun getWorkout(workoutId: String) : Workout
    suspend fun getRecommendedWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>>
    suspend fun getRelaxWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>>
}