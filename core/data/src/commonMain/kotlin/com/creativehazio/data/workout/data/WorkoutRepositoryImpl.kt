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
import com.creativehazio.data.workout.mapper.toCycleRelatedChallengeDayEntity
import com.creativehazio.data.workout.mapper.toCycleRelatedExerciseEntity
import com.creativehazio.data.workout.mapper.toCycleRelatedWorkoutEntity
import com.creativehazio.data.workout.mapper.toExerciseEntity
import com.creativehazio.data.workout.mapper.toWorkout
import com.creativehazio.data.workout.mapper.toWorkoutEntity
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
    override fun getWorkouts(category: WorkoutCategory): Flow<PagingData<Workout>> {
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
                    girlFitDatabase.workoutDao().getWorkouts()
                } else {
                    girlFitDatabase.workoutDao().getWorkoutsByCategory(category.name)
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

    override suspend fun getCycleRelatedWorkouts(
        limit: Int,
        currentCyclePhase: CyclePhase,
        cycleRelatedWorkout: CycleRelatedWorkout
    ): Flow<List<Workout>> {
        return try {

            val workoutDao = girlFitDatabase.workoutDao()

            val localWorkoutsFlow = when (cycleRelatedWorkout) {
                CycleRelatedWorkout.RECOMMENDED ->
                    workoutDao.getRecommendedWorkoutByPhase(
                        currentCyclePhase.name
                    )

                CycleRelatedWorkout.RELAX ->
                    workoutDao.getRelaxWorkoutByPhase(
                        currentCyclePhase.name
                    )
            }


            val cachedWorkouts = localWorkoutsFlow.first()

            if (cachedWorkouts.isNotEmpty()) {
                return localWorkoutsFlow.map { workouts ->
                    workouts
                        .take(limit)
                        .map { it.toWorkout() }
                }
            }

            val firestoreWorkouts = workoutDataSource.getWorkoutsByPhase(
                limit = limit,
                cyclePhase = currentCyclePhase.name
            )

            firestoreWorkoutDtosToCycleEntityMapper(
                firestoreWorkouts = firestoreWorkouts
            )

            return when (cycleRelatedWorkout) {
                CycleRelatedWorkout.RECOMMENDED -> {
                    girlFitDatabase.workoutDao()
                        .getRecommendedWorkoutByPhase(currentCyclePhase.name)
                        .map { cycleRelatedWorkoutWithExercises ->
                            cycleRelatedWorkoutWithExercises.map { it.toWorkout() }
                        }
                }

                CycleRelatedWorkout.RELAX -> {
                    girlFitDatabase.workoutDao().getRelaxWorkoutByPhase(currentCyclePhase.name)
                        .map { cycleRelatedWorkoutWithExercises ->
                            cycleRelatedWorkoutWithExercises.map { it.toWorkout() }
                        }
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(emptyList())
        }
    }

    suspend fun firestoreWorkoutDtosToCycleEntityMapper(
        firestoreWorkouts: List<WorkoutDto>,
    ) {

        girlFitDatabase.useWriterConnection { transactor ->
            transactor.immediateTransaction {
                try {

                    val workoutEntities = firestoreWorkouts.map { it.toCycleRelatedWorkoutEntity() }

                    val exerciseEntities = firestoreWorkouts.flatMap { workoutDto ->
                        workoutDto.exercises.map { exerciseDto ->
                            exerciseDto.toCycleRelatedExerciseEntity(
                                workoutId = workoutDto.id,
                                isFavourite = false
                            )
                        }
                    }

                    val challengeDayEntities = firestoreWorkouts.flatMap { workoutDto ->
                        workoutDto.challenge?.challengeDays?.mapIndexed { index, dayDto ->
                            dayDto.toCycleRelatedChallengeDayEntity(
                                parentChallengeId = workoutDto.id,
                                state = if (index == 0) "CURRENT"
                                else "UPCOMING"
                            )
                        } ?: emptyList()
                    }

                    girlFitDatabase.workoutDao().insertCycleRelatedWorkouts(workoutEntities)
                    girlFitDatabase.workoutDao().insertCycleRelatedExercises(exerciseEntities)
                    girlFitDatabase.workoutDao()
                        .insertCycleRelatedChallengeDays(challengeDayEntities)
                } catch (e: Exception) {
                    throw e
                }
            }
        }

    }

}

