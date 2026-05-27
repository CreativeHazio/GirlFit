package com.creativehazio.data.workout.domain

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.creativehazio.data.workout.data.remote.WorkoutDto
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun fetchWorkouts() : Flow<PagingData<WorkoutDto>>
}