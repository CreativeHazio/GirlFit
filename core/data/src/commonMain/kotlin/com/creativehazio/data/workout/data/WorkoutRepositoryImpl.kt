package com.creativehazio.data.workout.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.user.domain.CyclePhase
import com.creativehazio.data.workout.data.local.ExerciseEntity
import com.creativehazio.data.workout.data.local.WorkoutEntity
import com.creativehazio.data.workout.data.remote.ChallengeDayDto
import com.creativehazio.data.workout.data.remote.ChallengeDto
import com.creativehazio.data.workout.data.remote.ExerciseDto
import com.creativehazio.data.workout.data.remote.WorkoutDataSource
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.mapper.toWorkout
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlin.time.Clock

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
        }
        catch (e: Exception) {
            e.printStackTrace()
            Workout()
        }
    }

    override fun getRecommendedWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>> {
        return girlFitDatabase.workoutDao().getRecommendedWorkoutByPhase(currentCyclePhase.name)
            .map { workoutsWithExercises -> workoutsWithExercises.map { it.toWorkout() } }
}

    override fun getRelaxWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>> {
        return girlFitDatabase.workoutDao().getRelaxWorkoutByPhase(currentCyclePhase.name)
            .map { workoutsWithExercises -> workoutsWithExercises.map { it.toWorkout() } }
    }

}

