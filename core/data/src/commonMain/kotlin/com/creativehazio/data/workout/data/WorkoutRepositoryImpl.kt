package com.creativehazio.data.workout.data

import androidx.paging.PagingData
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.domain.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl(
    private val girlFitDatabase: GirlFitDatabase,
) : WorkoutRepository {

    override suspend fun fetchWorkouts(): Flow<PagingData<WorkoutDto>> = withContext(Dispatchers.IO) {
        return@withContext emptyFlow()
    }

}

