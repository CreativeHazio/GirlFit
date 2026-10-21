package com.creativehazio.data.workout.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.mapper.toChallengeDayEntity
import com.creativehazio.data.workout.mapper.toExerciseEntity
import com.creativehazio.data.workout.mapper.toWorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query(
        """
    SELECT * FROM workouts
    WHERE :query = '' OR title LIKE '%' || :query || '%'
    ORDER BY createdAt DESC
    """
    )
    fun getWorkouts(query: String): PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query(
        """
    SELECT * FROM workouts
    WHERE category = :category
    AND (:query = '' OR title LIKE '%' || :query || '%')
    ORDER BY createdAt DESC
    """
    )
    fun getWorkoutsByCategory(
        category: String,
        query: String
    ): PagingSource<Int, WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY createdAt ASC LIMIT 1")
    suspend fun getLastWorkout(): WorkoutWithExercises?

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String): WorkoutWithExercises

    @Query("DELETE FROM workouts")
    suspend fun clearAllWorkouts()

    @Upsert
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Upsert
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM exercises")
    suspend fun clearAllExercises()

    @Upsert
    suspend fun insertChallengeDays(challengeDays: List<ChallengeDayEntity>)

    @Query("DELETE FROM challenge_days")
    suspend fun clearAllChallengeDays()

    @Transaction
    suspend fun insertWorkoutDtos(
        dtos: List<WorkoutDto>,
    ) {
        val workouts = dtos.map { it.toWorkoutEntity() }
        val exercises = dtos.flatMap { dto ->
            dto.exercises.map { it.toExerciseEntity(workoutId = dto.id, isFavourite = false) }
        }
        val challengeDays = dtos.flatMap { dto ->
            dto.challenge?.challengeDays?.mapIndexed { index, day ->
                day.toChallengeDayEntity(
                    parentChallengeId = dto.id,
                    state = if (index == 0) "CURRENT" else "UPCOMING"
                )
            } ?: emptyList()
        }

        insertWorkouts(workouts)
        insertExercises(exercises)
        insertChallengeDays(challengeDays)
    }

    @Transaction
    @Query(
        """
    SELECT w.*
    FROM workouts AS w
    INNER JOIN home_workout_selections AS s
        ON s.workoutId = w.id
    WHERE s.section = 'RECOMMENDED'
      AND s.cyclePhase = :cyclePhase
    ORDER BY s.position ASC
    LIMIT :limit
    """
    )
    fun observeHomeRecommendedWorkouts(
        cyclePhase: String,
        limit: Int = 2
    ): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query(
        """
    SELECT w.*
    FROM workouts AS w
    INNER JOIN home_workout_selections AS s
        ON s.workoutId = w.id
    WHERE s.section = 'RELAX'
      AND s.cyclePhase IS NULL
    ORDER BY s.position ASC
    LIMIT :limit
    """
    )
    fun observeHomeRelaxWorkouts(
        limit: Int = 1
    ): Flow<List<WorkoutWithExercises>>

    @Query(
        """
    DELETE FROM home_workout_selections
    WHERE section = 'RECOMMENDED'
      AND cyclePhase = :cyclePhase
    """
    )
    suspend fun deleteHomeRecommendations(
        cyclePhase: String
    )

    @Query(
        """
    DELETE FROM home_workout_selections
    WHERE section = 'RELAX'
    """
    )
    suspend fun deleteHomeRelaxSelection()

    @Upsert
    suspend fun insertHomeSelections(
        selections: List<HomeWorkoutSelectionEntity>
    )

    @Transaction
    suspend fun replaceHomeRecommendations(
        cyclePhase: String,
        workoutIds: List<String>
    ) {
        deleteHomeRecommendations(cyclePhase)

        val selections = workoutIds
            .take(2)
            .mapIndexed { index, workoutId ->
                HomeWorkoutSelectionEntity(
                    selectionId = "recommended:$cyclePhase:$index",
                    section = "RECOMMENDED",
                    cyclePhase = cyclePhase,
                    position = index,
                    workoutId = workoutId
                )
            }

        if (selections.isNotEmpty()) {
            insertHomeSelections(selections)
        }
    }

    @Transaction
    suspend fun replaceHomeRelaxSelection(
        workoutIds: List<String>
    ) {
        deleteHomeRelaxSelection()

        val selections = workoutIds
            .take(1)
            .mapIndexed { index, workoutId ->
                HomeWorkoutSelectionEntity(
                    selectionId = "relax:$index",
                    section = "RELAX",
                    cyclePhase = null,
                    position = index,
                    workoutId = workoutId
                )
            }

        if (selections.isNotEmpty()) {
            insertHomeSelections(selections)
        }
    }

}