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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
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
        return try {
            girlFitDatabase.workoutDao().getWorkoutById(workoutId).toWorkout()
        }
        catch (e: Exception) {
            e.printStackTrace()
            Workout()
        }
    }

    // TODO: For relax and recommended, add cloud sync incase user wipes app's data
    // TODO: And change this to a list instead of flow
    override suspend fun getRecommendedWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                girlFitDatabase.workoutDao().getRecommendedWorkoutByPhase(currentCyclePhase.name)
                    .map { workoutsWithExercises -> workoutsWithExercises.map { it.toWorkout() } }
            } catch (e: Exception) {
                e.printStackTrace()
                flowOf(emptyList())
            }
        }

    override suspend fun getRelaxWorkouts(currentCyclePhase: CyclePhase): Flow<List<Workout>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                girlFitDatabase.workoutDao().getRelaxWorkoutByPhase(currentCyclePhase.name)
                    .map { workoutsWithExercises -> workoutsWithExercises.map { it.toWorkout() } }
            } catch (e: Exception) {
                e.printStackTrace()
                flowOf(emptyList())
            }

        }

}

class Seeder(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val girlFitDatabase: GirlFitDatabase
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

    suspend fun addRelaxAndRecommendedWorkouts() {
        val workouts = listOf(

            // --- MENSTRUAL PHASE ---
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Bedtime Unwind",
                imageUrl = "https://images.unsplash.com/photo-1518611012118-696072aa579a?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Maximize rest and recovery during your heaviest fatigue days.",
                cyclePhase = "MENSTRUAL",
                level = "BEGINNER",
                type = "TIME",
                category = "RELAX",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Upper Body Sculpt",
                imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Keep the body moving without putting heavy stress on the lower abdomen.",
                cyclePhase = "MENSTRUAL",
                level = "INTERMEDIATE",
                type = "TIME",
                category = "STRENGTH",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Glute Activation",
                imageUrl = "https://images.unsplash.com/photo-1574680096145-d05b474e2155?q=80&w=2069&auto=format&fit=crop",
                duration = 180,
                details = "Mat-based, low-impact strength that won't spike your heart rate.",
                cyclePhase = "MENSTRUAL",
                level = "BEGINNER",
                type = "TIME",
                category = "STRENGTH",
            ),

            // --- FOLLICULAR PHASE ---
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Morning Warm Up",
                imageUrl = "https://images.unsplash.com/photo-1552286450-32128ce6097a?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Harness your rising morning energy levels to start the day right.",
                cyclePhase = "FOLLICULAR",
                level = "BEGINNER",
                type = "TIME",
                category = "RELAX",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Lower Body Power",
                imageUrl = "https://images.unsplash.com/photo-1601422407692-ec4eeec1d9b3?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Your body is primed for muscle building. Time to focus on large muscle groups.",
                cyclePhase = "FOLLICULAR",
                level = "EXPERT",
                type = "TIME",
                category = "STRENGTH",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Core Burner",
                imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Rising estrogen helps with stamina. Push through this intense core circuit.",
                cyclePhase = "FOLLICULAR",
                level = "INTERMEDIATE",
                type = "TIME",
                category = "STRENGTH",
            ),

            // --- OVULATION PHASE ---
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Deep Tissue Stretch",
                imageUrl = "https://images.unsplash.com/photo-1552196563-5527ee323d47?q=80&w=2069&auto=format&fit=crop",
                duration = 180,
                details = "Essential recovery between your high-intensity sessions to prevent injury.",
                cyclePhase = "OVULATION",
                level = "BEGINNER",
                type = "TIME",
                category = "RELAX",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "HIIT Cardio Blast",
                imageUrl = "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Estrogen and testosterone are peaking. Maximize your highest energy point of the month.",
                cyclePhase = "OVULATION",
                level = "EXPERT",
                type = "TIME",
                category = "STRENGTH",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Lower Body Power",
                imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Capitalize on peak hormones for maximum strength output.",
                cyclePhase = "OVULATION",
                level = "EXPERT",
                type = "TIME",
                category = "STRENGTH",
            ),

            // --- LUTEAL PHASE ---
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Evening Relax",
                imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Calm the central nervous system as progesterone naturally makes the body feel sleepier.",
                cyclePhase = "LUTEAL",
                level = "BEGINNER",
                type = "TIME",
                category = "RELAX",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Glute Activation",
                imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=2070&auto=format&fit=crop",
                duration = 180,
                details = "Maintain strength with moderate, focused effort rather than exhausting compound lifts.",
                cyclePhase = "LUTEAL",
                level = "INTERMEDIATE",
                type = "TIME",
                category = "STRENGTH",
            ),
            WorkoutEntity(
                id = generateFirestoreId(),
                createdAt = Clock.System.now().toEpochMilliseconds(),
                title = "Core Burner",
                imageUrl = "https://images.unsplash.com/photo-1554284126-aa88f22d8b74?q=80&w=2094&auto=format&fit=crop",
                duration = 180,
                details = "Highly effective moderate core work before transitioning to full rest.",
                cyclePhase = "LUTEAL",
                level = "INTERMEDIATE",
                type = "TIME",
                category = "STRENGTH",
            )
        )


        val exercises = workouts.flatMap { workout ->
            List(3) { index ->
                ExerciseEntity(
                    exerciseId = generateFirestoreId(),
                    workoutId = workout.id,
                    title = "Exercise ${index + 1}",
                    duration = workout.duration / 3,
                    description = "Focus on form and steady breathing.",
                    thumbnailGifUrl = "https://example.com/thumb.gif",
                    gifUrl = "https://example.com/full.gif",
                    isFavourite = false
                )
            }
        }

        // Insert both into the database
        girlFitDatabase.workoutDao().insertWorkouts(workouts)
        girlFitDatabase.workoutDao().insertExercises(exercises)
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
            val es = generateDummyExercises(3)
            workouts.add(
                WorkoutDto(
                    id = generateFirestoreId(),
                    createdAt = currentTime - i,
                    title = relaxTitles[i],
                    imageUrl = imageUrls[i],
                    details = "A perfect way to de-stress and stretch your body.",
                    level = "BEGINNER",
                    type = "TIME",
                    category = if (i % 2 == 0) "RELAX" else "YOGA",
                    exercises = generateDummyExercises(3),
                    duration = es.sumOf { it.duration }
                )
            )
        }

        // 2. Generate 5 "Strength / Quick" Workouts (Time Based)
        for (i in 5..9) {
            val es = generateDummyExercises(5)
            workouts.add(
                WorkoutDto(
                    id = generateFirestoreId(),
                    createdAt = currentTime - i,
                    title = strengthTitles[i - 5],
                    imageUrl = imageUrls[i],
                    details = "Build strength and tone muscles effectively.",
                    duration = es.sumOf { it.duration },
                    level = "INTERMEDIATE",
                    type = "TIME",
                    category = if (i % 2 == 0) "STRENGTH" else "QUICK",
                    exercises = es
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
                    duration = 0,
                    level = "EXPERT",
                    type = "CHALLENGE",
                    category = "CHALLENGE",
                    exercises = emptyList(),
                    challenge = ChallengeDto(
                        id = workoutId,
                        challengeDays = List(daysInChallenge) { dayIndex ->
                            val linkedTimeWorkoutId = workouts[i - 10].id

                            ChallengeDayDto(
                                id = generateFirestoreId(),
                                number = dayIndex + 1,
                                workoutId = linkedTimeWorkoutId
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
                duration = 45,
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

