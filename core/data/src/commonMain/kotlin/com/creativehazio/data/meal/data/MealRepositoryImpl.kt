package com.creativehazio.data.meal.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.meal.data.remote.FilterItemDto
import com.creativehazio.data.meal.data.remote.MealDataSource
import com.creativehazio.data.meal.data.remote.MealDto
import com.creativehazio.data.meal.data.remote.MealFilterDto
import com.creativehazio.data.meal.data.remote.MealNutrientDto
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealRepository
import com.creativehazio.data.meal.mapper.toFilterItemEntity
import com.creativehazio.data.meal.mapper.toMeal
import com.creativehazio.data.meal.mapper.toMealFilter
import com.creativehazio.data.meal.mapper.toMealFilterEntity
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class MealRepositoryImpl(
    private val girlFitDatabase: GirlFitDatabase,
    private val mealDataSource: MealDataSource
) : MealRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMeals(filters: List<String>): Flow<PagingData<Meal>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = MealRemoteMediator(
                girlFitDatabase = girlFitDatabase,
                mealDataSource = mealDataSource
            ),
            pagingSourceFactory = {
                girlFitDatabase.mealDao().getMeals(filters)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMeal() }
        }
    }

    override suspend fun getMeal(mealId: String): Meal {
        return try {
            girlFitDatabase.mealDao().getMealById(mealId).toMeal()
        } catch (e: Exception) {
            e.printStackTrace()
            Meal()
        }
    }

    override suspend fun getMealFilters(): List<MealFilter> {
        return try {
            val localFilters = girlFitDatabase.mealDao().getMealFilters()

            if (localFilters.isNotEmpty()) {
                localFilters.map { it.toMealFilter() }
            }

            val remoteFilters = mealDataSource.getMealFilters()

            girlFitDatabase.useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    try {

                        val mealFilterEntities = remoteFilters.map { mealFilterDto ->
                            mealFilterDto.toMealFilterEntity()
                        }

                        val filterItemEntities = remoteFilters.flatMap { mealFilterDto ->
                            mealFilterDto.items.map { filterItemDto ->
                                filterItemDto.toFilterItemEntity(mealFilterDto.id)
                            }
                        }

                        girlFitDatabase.mealDao().insertMealFilters(mealFilterEntities)
                        girlFitDatabase.mealDao().insertFilterItems(filterItemEntities)

                    } catch (e: Exception) {
                        throw e
                    }
                }
            }

            remoteFilters.map { it.toMealFilter() }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}

class MealSeeder(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {
    // 1. KMP ID Generator
    private fun generateFirestoreId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..20)
            .map { chars.random() }
            .joinToString("")
    }

    // Replace these 21 empty strings with your actual image URLs
    private val imageUrls = listOf(
        "https://plus.unsplash.com/premium_photo-1705460221630-e43e7a6599a7?q=80&w=988&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1676106623583-e68dd66683e3?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1571230389215-b34a89739ef1?q=80&w=1026&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1506084868230-bb9d95c24759?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1694670233199-77b204d60606?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1666599028424-e316d4e34aa6?q=80&w=1480&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1663853052046-95cc00fd20b0?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1702689767359-5e8b57d66d42?q=80&w=927&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1707080023804-ccf44ad50af2?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1560717845-968823efbee1?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1624234734133-7f1342047e30?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1664478291780-0c67f5fb15e6?q=80&w=1480&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1727187824542-29cf5d138634?q=80&w=1071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1667807515956-fbe501d03e26?q=80&w=1094&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1699152530640-aa13fbd36cda?q=80&w=2069&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1575236448134-8bcfcbd28fa1?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1666318300348-a4d0226d81ad?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1654084767590-a38c7f0f5bd3?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1726039468346-2f3e0f1f5b52?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://plus.unsplash.com/premium_photo-1667899298243-81e81f3e2155?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    )

    // --- 2. SEED THE FILTERS ---
    private val sampleFilters = listOf(
        MealFilterDto(
            id = "1",
            createdAt = Clock.System.now().toEpochMilliseconds(),
            name = "Goal",
            items = listOf(
                FilterItemDto(
                    id = "goal_1",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Fat loss"
                ),
                FilterItemDto(
                    id = "goal_2",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Muscle tone & strength"
                ),
                FilterItemDto(
                    id = "goal_3",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Energy & performance"
                ),
                FilterItemDto(
                    id = "goal_4",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Hormone & cycle balance"
                ),
                FilterItemDto(
                    id = "goal_5",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Gut health & digestion"
                )
            )
        ),
        MealFilterDto(
            id = "2",
            createdAt = Clock.System.now().toEpochMilliseconds(),
            name = "Meal time",
            items = listOf(
                FilterItemDto(
                    id = "meal_time_1",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Breakfast"
                ),
                FilterItemDto(
                    id = "meal_time_2",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Lunch"
                ),
                FilterItemDto(
                    id = "meal_time_3",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Dinner"
                ),
                FilterItemDto(
                    id = "meal_time_4",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Snacks"
                ),
                FilterItemDto(
                    id = "meal_time_5",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Smoothies & Drinks"
                )
            )
        ),
        MealFilterDto(
            id = "3",
            createdAt = Clock.System.now().toEpochMilliseconds(),
            name = "Time needed",
            items = listOf(
                FilterItemDto(
                    id = "time_needed_1",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Under 15 mins"
                ),
                FilterItemDto(
                    id = "time_needed_2",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "15 - 30 mins"
                ),
                FilterItemDto(
                    id = "time_needed_3",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "30 - 45 mins"
                ),
                FilterItemDto(
                    id = "time_needed_4",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "45 - 60 mins"
                ),
                FilterItemDto(
                    id = "time_needed_5",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    name = "Over 60 mins"
                )
            )
        )
    )

    // --- 3. HELPER FUNCTION FOR CLEANER CODE ---
    private fun createMeal(
        name: String,
        imageIndex: Int,
        filterIds: List<String>,
        ingredients: List<String>,
        cals: Int,
        protein: Int,
        carbs: Int,
        fats: Int,
        fiber: Int, // 💡 Added Fiber parameter
        score: Float
    ): MealDto {
        val now = Clock.System.now().toEpochMilliseconds()
        return MealDto(
            id = generateFirestoreId(),
            createdAt = now,
            name = name,
            imageUrl = imageUrls.getOrElse(imageIndex) { "" },
            filterIds = filterIds,
            ingredients = ingredients,
            totalCalories = cals,
            mealNutrients = listOf(
                MealNutrientDto(id = generateFirestoreId(), createdAt = now, name = "Protein", gramTotal = protein),
                MealNutrientDto(id = generateFirestoreId(), createdAt = now, name = "Carbohydrate", gramTotal = carbs), // 💡 Renamed
                MealNutrientDto(id = generateFirestoreId(), createdAt = now, name = "Fat", gramTotal = fats), // 💡 Renamed
                MealNutrientDto(id = generateFirestoreId(), createdAt = now, name = "Fiber", gramTotal = fiber) // 💡 Added Fiber
            ),
            score = score
        )
    }

    // --- 4. SEED THE 21 MEALS ---
    private val sampleMeals = listOf(
        // BREAKFASTS
        createMeal(
            name = "High-Protein Berry Oatmeal", imageIndex = 0,
            filterIds = listOf("goal_1", "goal_2", "meal_time_1", "time_needed_1"),
            ingredients = listOf("1/2 cup rolled oats", "1 scoop whey protein", "1/2 cup berries", "1 cup almond milk", "1 tbsp chia seeds"),
            cals = 345, protein = 32, carbs = 40, fats = 8, fiber = 12, score = 94f
        ),
        createMeal(
            name = "Avocado Toast with Poached Eggs", imageIndex = 1,
            filterIds = listOf("goal_3", "goal_4", "meal_time_1", "time_needed_1"),
            ingredients = listOf("2 slices sourdough", "1/2 avocado", "2 eggs", "Everything bagel seasoning"),
            cals = 410, protein = 18, carbs = 32, fats = 24, fiber = 8, score = 88f
        ),
        createMeal(
            name = "Greek Yogurt Parfait", imageIndex = 2,
            filterIds = listOf("goal_5", "meal_time_1", "time_needed_1"),
            ingredients = listOf("1 cup plain Greek yogurt", "1 tbsp honey", "1/4 cup walnuts", "1/2 cup strawberries"),
            cals = 290, protein = 22, carbs = 28, fats = 12, fiber = 4, score = 90f
        ),
        createMeal(
            name = "Protein Pancakes", imageIndex = 3,
            filterIds = listOf("goal_2", "meal_time_1", "time_needed_2"),
            ingredients = listOf("1 banana", "2 eggs", "1 scoop vanilla protein", "1/4 cup oats"),
            cals = 380, protein = 35, carbs = 42, fats = 10, fiber = 5, score = 85f
        ),
        createMeal(
            name = "Tofu Scramble with Spinach", imageIndex = 4,
            filterIds = listOf("goal_1", "goal_3", "meal_time_1", "time_needed_2"),
            ingredients = listOf("1/2 block firm tofu", "1 cup spinach", "1/4 tsp turmeric", "1 slice whole wheat toast"),
            cals = 260, protein = 20, carbs = 22, fats = 11, fiber = 6, score = 92f
        ),

        // LUNCHES
        createMeal(
            name = "Grilled Chicken & Quinoa Salad", imageIndex = 5,
            filterIds = listOf("goal_1", "goal_5", "meal_time_2", "time_needed_2"),
            ingredients = listOf("4oz grilled chicken", "1/2 cup quinoa", "2 cups mixed greens", "1 tbsp vinaigrette"),
            cals = 420, protein = 38, carbs = 25, fats = 18, fiber = 7, score = 88f
        ),
        createMeal(
            name = "Turkey Wrap with Hummus", imageIndex = 6,
            filterIds = listOf("goal_3", "meal_time_2", "time_needed_1"),
            ingredients = listOf("1 whole wheat wrap", "4oz deli turkey", "2 tbsp hummus", "Cucumber & tomato slices"),
            cals = 340, protein = 28, carbs = 30, fats = 12, fiber = 8, score = 85f
        ),
        createMeal(
            name = "Lentil Soup", imageIndex = 7,
            filterIds = listOf("goal_5", "meal_time_2", "time_needed_3"),
            ingredients = listOf("1 cup cooked lentils", "Carrots & celery", "Vegetable broth", "1 slice sourdough"),
            cals = 310, protein = 18, carbs = 52, fats = 4, fiber = 16, score = 95f
        ),
        createMeal(
            name = "Tuna Salad Stuffed Bell Peppers", imageIndex = 8,
            filterIds = listOf("goal_1", "goal_2", "meal_time_2", "time_needed_1"),
            ingredients = listOf("1 can tuna", "1 tbsp Greek yogurt", "Diced celery", "2 bell peppers, halved"),
            cals = 220, protein = 32, carbs = 12, fats = 4, fiber = 5, score = 89f
        ),
        createMeal(
            name = "Sweet Potato & Black Bean Bowl", imageIndex = 9,
            filterIds = listOf("goal_3", "goal_4", "meal_time_2", "time_needed_2"),
            ingredients = listOf("1 roasted sweet potato", "1/2 cup black beans", "Salsa", "1/4 avocado"),
            cals = 390, protein = 12, carbs = 65, fats = 9, fiber = 14, score = 91f
        ),

        // DINNERS
        createMeal(
            name = "Baked Salmon with Asparagus", imageIndex = 10,
            filterIds = listOf("goal_4", "meal_time_3", "time_needed_2"),
            ingredients = listOf("5oz wild salmon", "1 bunch asparagus", "1/2 tbsp olive oil", "Lemon juice"),
            cals = 380, protein = 34, carbs = 8, fats = 22, fiber = 4, score = 82f
        ),
        createMeal(
            name = "Lean Beef Stir-Fry", imageIndex = 11,
            filterIds = listOf("goal_2", "meal_time_3", "time_needed_2"),
            ingredients = listOf("5oz lean beef strips", "1 cup broccoli", "1 tbsp soy sauce", "1/2 cup brown rice"),
            cals = 450, protein = 36, carbs = 45, fats = 14, fiber = 7, score = 86f
        ),
        createMeal(
            name = "Turkey Meatballs with Zucchini Noodles", imageIndex = 12,
            filterIds = listOf("goal_1", "meal_time_3", "time_needed_3"),
            ingredients = listOf("5 lean turkey meatballs", "2 cups zucchini noodles", "1/2 cup marinara sauce"),
            cals = 320, protein = 30, carbs = 20, fats = 14, fiber = 4, score = 89f
        ),
        createMeal(
            name = "Baked Cod with Roasted Vegetables", imageIndex = 13,
            filterIds = listOf("goal_4", "goal_5", "meal_time_3", "time_needed_3"),
            ingredients = listOf("6oz cod fillet", "1 cup Brussels sprouts", "1 cup diced carrots", "1 tbsp olive oil"),
            cals = 340, protein = 32, carbs = 24, fats = 16, fiber = 8, score = 92f
        ),
        createMeal(
            name = "Chicken Parmesan (Light)", imageIndex = 14,
            filterIds = listOf("goal_2", "goal_3", "meal_time_3", "time_needed_4"),
            ingredients = listOf("5oz chicken breast", "1/4 cup mozzarella", "1/2 cup marinara", "1 cup whole wheat pasta"),
            cals = 520, protein = 45, carbs = 48, fats = 16, fiber = 6, score = 80f
        ),

        // SNACKS
        createMeal(
            name = "Apple Slices with Almond Butter", imageIndex = 15,
            filterIds = listOf("goal_3", "meal_time_4", "time_needed_1"),
            ingredients = listOf("1 medium apple", "1.5 tbsp almond butter"),
            cals = 240, protein = 5, carbs = 25, fats = 14, fiber = 6, score = 85f
        ),
        createMeal(
            name = "Cottage Cheese & Pineapple", imageIndex = 16,
            filterIds = listOf("goal_2", "meal_time_4", "time_needed_1"),
            ingredients = listOf("1/2 cup low-fat cottage cheese", "1/2 cup pineapple chunks"),
            cals = 160, protein = 14, carbs = 20, fats = 2, fiber = 2, score = 88f
        ),
        createMeal(
            name = "Edamame", imageIndex = 17,
            filterIds = listOf("goal_4", "goal_5", "meal_time_4", "time_needed_1"),
            ingredients = listOf("1 cup steamed edamame pods", "Pinch of sea salt"),
            cals = 188, protein = 17, carbs = 14, fats = 8, fiber = 8, score = 94f
        ),

        // SMOOTHIES & DRINKS
        createMeal(
            name = "Green Detox Smoothie", imageIndex = 18,
            filterIds = listOf("goal_1", "goal_5", "meal_time_5", "time_needed_1"),
            ingredients = listOf("1 cup spinach", "1/2 green apple", "1/2 cucumber", "Juice of 1/2 lemon", "Water"),
            cals = 90, protein = 2, carbs = 22, fats = 0, fiber = 5, score = 96f
        ),
        createMeal(
            name = "Chocolate Peanut Butter Shake", imageIndex = 19,
            filterIds = listOf("goal_2", "goal_3", "meal_time_5", "time_needed_1"),
            ingredients = listOf("1 scoop chocolate protein", "1 tbsp peanut butter", "1/2 banana", "1 cup almond milk"),
            cals = 320, protein = 30, carbs = 25, fats = 12, fiber = 4, score = 90f
        ),
        createMeal(
            name = "Berry Maca Hormone Balancer", imageIndex = 20,
            filterIds = listOf("goal_4", "meal_time_5", "time_needed_1"),
            ingredients = listOf("1 cup mixed berries", "1 tsp maca powder", "1 tbsp flax seeds", "1 cup oat milk"),
            cals = 210, protein = 4, carbs = 35, fats = 6, fiber = 9, score = 88f
        )
    )

    /**
     * Uploads all filters and sample meals to Firestore in a single atomic batch.
     */
    suspend fun seedDatabaseToFirestore(): Result<Unit> {
        return try {
            val filtersCollection = firestore.collection("meal_filters")
            val mealsCollection = firestore.collection("meals")

            val batch = firestore.batch()

            // Queue up the Filters
            sampleFilters.forEach { filter ->
                val docRef = filtersCollection.document(filter.id)
                batch.set(docRef, filter)
            }

            // Queue up the Meals
            sampleMeals.forEach { meal ->
                val docRef = mealsCollection.document(meal.id)
                batch.set(docRef, meal)
            }

            // Commit everything together
            batch.commit()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}