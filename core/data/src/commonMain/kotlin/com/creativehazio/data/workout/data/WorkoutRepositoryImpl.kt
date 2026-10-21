package com.creativehazio.data.workout.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.workout.data.remote.WorkoutDataSource
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.domain.CycleRelatedWorkout
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.mapper.toChallengeDayEntity
import com.creativehazio.data.workout.mapper.toExerciseEntity
import com.creativehazio.data.workout.mapper.toWorkout
import com.creativehazio.data.workout.mapper.toWorkoutEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class WorkoutRepositoryImpl(
    private val girlFitDatabase: GirlFitDatabase,
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getWorkouts(
        category: WorkoutCategory,
        searchQuery: String
    ): Flow<PagingData<Workout>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = WorkoutRemoteMediator(
                girlFitDatabase = girlFitDatabase,
                workoutDataSource = workoutDataSource
            ),
            pagingSourceFactory = {
                if (category == WorkoutCategory.ALL) {
                    girlFitDatabase.workoutDao().getWorkouts(searchQuery)
                } else {
                    girlFitDatabase.workoutDao().getWorkoutsByCategory(category.name, searchQuery)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toWorkout() }
        }
    }

    override suspend fun getWorkout(workoutId: String): Workout {
        return try {
            girlFitDatabase.workoutDao().getWorkoutById(workoutId).toWorkout()
        } catch (e: Exception) {
            e.printStackTrace()
            Workout()
        }
    }

    override fun observeRecommendedWorkouts(
        cyclePhase: String
    ): Flow<List<Workout>> =
        girlFitDatabase.workoutDao()
            .observeHomeRecommendedWorkouts(
                cyclePhase = cyclePhase,
                limit = 2
            )
            .map { workouts ->
                workouts.map { it.toWorkout() }
            }

    override fun observeRelaxWorkouts(): Flow<List<Workout>> =
        girlFitDatabase.workoutDao()
            .observeHomeRelaxWorkouts(limit = 1)
            .map { workouts ->
                workouts.map { it.toWorkout() }
            }

    override suspend fun refreshRecommendedWorkouts(
        cyclePhase: CyclePhase,
        limit: Int
    ) {
        val dtos = workoutDataSource.getWorkoutsByPhase(
            limit = limit,
            cyclePhase = cyclePhase.name
        )

        // Save the actual workout content first.
        girlFitDatabase.workoutDao()
            .insertWorkoutDtos(dtos)

        // Then update which IDs Home should feature.
        girlFitDatabase.workoutDao()
            .replaceHomeRecommendations(
                cyclePhase = cyclePhase.name,
                workoutIds = dtos.map { it.id }
            )
    }

    override suspend fun refreshRelaxWorkouts(
        limit: Int
    ) {
        val dtos = workoutDataSource.getWorkoutsByCategory(
            limit = limit,
            category = WorkoutCategory.RELAX.name
        )

        girlFitDatabase.workoutDao()
            .insertWorkoutDtos(dtos)

        girlFitDatabase.workoutDao()
            .replaceHomeRelaxSelection(
                workoutIds = dtos.map { it.id }
            )
    }

}

