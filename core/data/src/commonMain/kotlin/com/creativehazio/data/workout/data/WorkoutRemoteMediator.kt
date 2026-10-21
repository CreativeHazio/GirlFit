package com.creativehazio.data.workout.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.workout.data.local.WorkoutRemoteKeyEntity
import com.creativehazio.data.workout.data.local.WorkoutWithExercises
import com.creativehazio.data.workout.data.remote.WorkoutDataSource
import com.creativehazio.data.workout.mapper.toChallengeDayEntity
import com.creativehazio.data.workout.mapper.toExerciseEntity
import com.creativehazio.data.workout.mapper.toWorkoutEntity

@OptIn(ExperimentalPagingApi::class)
class WorkoutRemoteMediator(
    private val girlFitDatabase: GirlFitDatabase,
    private val workoutDataSource: WorkoutDataSource
) : RemoteMediator<Int, WorkoutWithExercises>() {

    private val workoutDao = girlFitDatabase.workoutDao()
    private val remoteKeyDao = girlFitDatabase.workoutRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WorkoutWithExercises>
    ): MediatorResult {
        return try {

            val lastTimestamp: Long? = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    remoteKeyDao.getAllWorkoutsKey()
                        ?.lastTimestamp
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                }
            }

            val firestoreWorkouts = workoutDataSource.getWorkouts(
                limit = state.config.pageSize,
                lastTimestamp = lastTimestamp
            )

            workoutDao.insertWorkoutDtos(firestoreWorkouts)

            if (firestoreWorkouts.isNotEmpty()) {
                remoteKeyDao.upsert(
                    WorkoutRemoteKeyEntity(
                        lastTimestamp =
                            firestoreWorkouts.last().createdAt
                    )
                )
            } else if (loadType == LoadType.REFRESH) {
                remoteKeyDao.clearAllWorkoutsKey()
            }

            MediatorResult.Success(
                endOfPaginationReached = firestoreWorkouts.size < state.config.pageSize
            )

        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }


}