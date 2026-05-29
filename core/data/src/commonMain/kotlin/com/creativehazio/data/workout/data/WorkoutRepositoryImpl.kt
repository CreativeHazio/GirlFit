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
import com.creativehazio.data.workout.data.remote.ChallengeDayDto
import com.creativehazio.data.workout.data.remote.ChallengeDto
import com.creativehazio.data.workout.data.remote.ExerciseDto
import com.creativehazio.data.workout.data.remote.WorkoutDataSource
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.ChallengeDay
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutRepository
import com.creativehazio.data.workout.mapper.toChallengeDay
import com.creativehazio.data.workout.mapper.toExerciseEntity
import com.creativehazio.data.workout.mapper.toWorkout
import com.creativehazio.data.workout.mapper.toWorkoutEntity
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
        return girlFitDatabase.workoutDao().getWorkoutById(workoutId).toWorkout()
    }

    override suspend fun getWorkoutChallenge(workoutId: String): Challenge {
        val challengeDaysFlow = girlFitDatabase.workoutDao().getWorkoutChallengeDaysById(workoutId)
        var challengeDays = emptyList<ChallengeDay>()

        withContext(Dispatchers.Default) {
            challengeDaysFlow.collect { challengeDayEntities ->
                challengeDays = challengeDayEntities.map { it.toChallengeDay() }
            }
        }

        challengeDays.forEach {
            println(it.number)
        }

        return Challenge(
            id = workoutId,
            challengeDays = challengeDays
        )
    }

    override fun getRecommendedWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>> {
        return emptyFlow()
    }

    override suspend fun getRelaxWorkouts(): Flow<List<Workout>> = withContext(Dispatchers.IO){
        return@withContext try {
            val firestoreWorkouts = workoutDataSource.getWorkoutsByCategory (
                limit = 5,
                category = "RELAX"
            )

            girlFitDatabase.useWriterConnection { transactor ->
                transactor.immediateTransaction {
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

                    girlFitDatabase.workoutDao().insertWorkouts(workoutEntities)
                    girlFitDatabase.workoutDao().insertExercises(exerciseEntities)
                }
            }

            girlFitDatabase.workoutDao().getWorkoutsByCategoryAsFlow("RELAX").map { workoutsFlow ->
                workoutsFlow.map {
                    it.toWorkout()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(emptyList())
        }

    }

}

class FirestoreSeeder(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    private val imageUrls = listOf(
        "https://plus.unsplash.com/premium_photo-1681486763166-2a07e312fd35?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1676107240833-c5475c83c56a?q=80&w=983&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1758599880608-fcaa7104327b?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1674675646725-5b4aca5adb21?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1697060598741-7ca9b696febb?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1714646442330-9068099f5521?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1649888317149-05d953c9fef8?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1714646442274-18620f280bd0?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1714646442369-29d630229e71?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1758599878868-52cced2f8154?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1714646442265-9cec35271c7a?q=80&w=2532&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1666736568848-4f237d6e23b7?q=80&w=997&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1661589455782-b9ba18ede928?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1434682966726-19ad3a76e143?q=80&w=2074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1674600625774-6b1e37a63b99?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1518310952931-b1de897abd40?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1664910607414-6e5eb86a9dfe?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1648634558270-6533b7578915?q=80&w=986&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1683848644228-0ddc0c8dc8e6?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1606903037631-f09fd0bd74b4?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    )

    suspend fun seedDatabase() {
        val batch = firestore.batch()
        val workoutsCollection = firestore.collection("workouts")

        val dummyWorkouts = generateWorkouts()

        dummyWorkouts.forEach { workoutDto ->
            val docRef = workoutsCollection.document(workoutDto.id)
            batch.set(docRef, workoutDto)
        }

        try {
            batch.commit()
            println("Successfully uploaded 20 workouts to Firestore!")
        } catch (e: Exception) {
            println("Error uploading workouts: ${e.message}")
        }
    }

    private fun generateWorkouts(): List<WorkoutDto> {
        val workouts = mutableListOf<WorkoutDto>()
        val currentTime = Clock.System.now().toEpochMilliseconds()

        val relaxTitles = listOf(
            "Morning Warm Up",
            "Evening Relax",
            "Deep Tissue Stretch",
            "Mindful Yoga Flow",
            "Bedtime Unwind"
        )

        val strengthTitles = listOf(
            "Glute Activation",
            "Core Burner",
            "Upper Body Sculpt",
            "HIIT Cardio Blast",
            "Lower Body Power"
        )

        val challengeTitles = listOf(
            "Full Body Challenge",
            "Booty Challenge",
            "Summer Shred Challenge",
            "Core Crusher Challenge",
            "Flexibility Journey",
            "Glow Up Challenge",
            "Endurance Builder Challenge",
            "Lean Muscle Challenge",
            "Posture Corrector Challenge",
            "Total Body Toning Challenge"
        )

        // 1. Generate 5 "Relax / Yoga" Workouts (Time Based)
        for (i in 0..4) {
            workouts.add(
                WorkoutDto(
                    id = generateFirestoreId(),
                    createdAt = currentTime - i,
                    title = relaxTitles[i],
                    imageUrl = imageUrls[i],
                    details = "A perfect way to de-stress and stretch your body.",
                    duration = "${10 + (i * 5)} mins",
                    level = "BEGINNER",
                    type = "TIME",
                    category = if (i % 2 == 0) "RELAX" else "YOGA",
                    exercises = generateDummyExercises(3)
                )
            )
        }

        // 2. Generate 5 "Strength / Quick" Workouts (Time Based)
        for (i in 5..9) {
            workouts.add(
                WorkoutDto(
                    id = generateFirestoreId(),
                    createdAt = currentTime - i,
                    title = strengthTitles[i - 5],
                    imageUrl = imageUrls[i],
                    details = "Build strength and tone muscles effectively.",
                    duration = "20 mins",
                    level = "INTERMEDIATE",
                    type = "TIME",
                    category = if (i % 2 == 0) "STRENGTH" else "QUICK",
                    exercises = generateDummyExercises(5)
                )
            )
        }

        // 3. Generate 10 "Challenge" Workouts
        for (i in 10..19) {
            val workoutId = generateFirestoreId()
            val daysInChallenge = if (i % 2 == 0) 14 else 28

            workouts.add(
                WorkoutDto(
                    id = workoutId,
                    createdAt = currentTime - i,
                    title = challengeTitles[i - 10],
                    imageUrl = imageUrls[i],
                    details = "Commit to days of consistent growth.",
                    duration = "", // 🔑 Duration set to empty string for challenges
                    level = "EXPERT",
                    type = "CHALLENGE",
                    category = "CHALLENGE",
                    exercises = generateDummyExercises(4),
                    challenge = ChallengeDto(
                        id = generateFirestoreId(),
                        challengeDays = List(daysInChallenge) { dayIndex ->
                            ChallengeDayDto(
                                id = generateFirestoreId(),
                                number = dayIndex + 1,
                                workoutId = workoutId
                            )
                        }
                    )
                )
            )
        }

        return workouts
    }

    private fun generateDummyExercises(count: Int): List<ExerciseDto> {
        return List(count) { index ->
            ExerciseDto(
                id = generateFirestoreId(),
                title = "Exercise ${index + 1}",
                duration = "45s",
                description = "Keep your core tight and breathe steadily.",
                thumbnailGifUrl = "https://example.com/thumb.gif",
                gifUrl = "https://example.com/full.gif"
            )
        }
    }

    private fun generateFirestoreId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..20)
            .map { chars.random() }
            .joinToString("")
    }
}

