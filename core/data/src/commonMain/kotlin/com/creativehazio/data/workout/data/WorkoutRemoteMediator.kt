package com.creativehazio.data.workout.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
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

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WorkoutWithExercises>
    ): MediatorResult {
        return try {

            val lastTimestamp: Long? = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = girlFitDatabase.workoutDao().getLastWorkout()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    lastItem.workout.createdAt
                }
            }

            val firestoreWorkouts = workoutDataSource.getWorkouts(
                limit = state.config.pageSize,
                lastTimestamp = lastTimestamp
            )

            girlFitDatabase.useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    try {
                        if (loadType == LoadType.REFRESH) {
                            girlFitDatabase.workoutDao().clearAllWorkouts()
                            girlFitDatabase.workoutDao().clearAllExercises()
                            girlFitDatabase.workoutDao().clearAllChallengeDays()
                        }

                        val workoutEntities = firestoreWorkouts.map { it.toWorkoutEntity() }

                        val exerciseEntities = firestoreWorkouts.flatMap { workoutDto ->
                            workoutDto.exercises.map { exerciseDto ->
                                exerciseDto.toExerciseEntity(
                                    workoutId = workoutDto.id,
                                    // TODO: Also query progress doc and add isFavourite from there
                                    isFavourite = false
                                )
                            }
                        }

                        // TODO: Also query progress doc and add state from there
                        val challengeDayEntities = firestoreWorkouts.flatMap { workoutDto ->
                            workoutDto.challenge?.challengeDays?.map { dayDto ->
                                dayDto.toChallengeDayEntity(
                                    workoutId = workoutDto.id,
                                    state = "UPCOMING"
                                )
                            } ?: emptyList()
                        }

                        girlFitDatabase.workoutDao().insertWorkouts(workoutEntities)
                        girlFitDatabase.workoutDao().insertExercises(exerciseEntities)
                        girlFitDatabase.workoutDao().insertChallengeDays(challengeDayEntities)

                    } catch (e: Exception) {
                        throw e
                    }
                }

            }

            MediatorResult.Success(
                endOfPaginationReached = firestoreWorkouts.isEmpty()
            )

        } catch (e: Exception) {
            println("MEDIATOR CRASH: ${e.message}")
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }


}