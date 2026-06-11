package com.creativehazio.data.meal.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import androidx.room.Transaction

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealNutrients(mealNutrients: List<MealNutrientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFilters(filters: List<MealFilterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterItems(items: List<FilterItemEntity>)

    @Query("DELETE FROM meals")
    suspend fun clearAllMeals()

    @Query("DELETE FROM meal_nutrients")
    suspend fun clearAllMealNutrients()

    @Transaction
    @RawQuery(observedEntities = [MealEntity::class])
    fun getMealsRawQuery(query: RoomRawQuery): PagingSource<Int, MealWithNutrients>

    fun getMeals(filters: List<String>): PagingSource<Int, MealWithNutrients> {
        var queryString = "SELECT * FROM meals"

        if (filters.isNotEmpty()) {
            val conditions = filters.map { "filters LIKE ?" }

            queryString += " WHERE " + conditions.joinToString(" OR ")
        }

        queryString += " ORDER BY createdAt DESC"

        val query = RoomRawQuery(
            sql = queryString,
            onBindStatement = { statement ->
                filters.forEachIndexed { index, filterId ->
                    statement.bindText(index + 1, "%$filterId%")
                }
            }
        )

        return getMealsRawQuery(query)
    }

    @Transaction
    @Query("SELECT * FROM meals ORDER BY createdAt ASC LIMIT 1")
    suspend fun getLastMeal() : MealWithNutrients?

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :mealId")
    fun getMealById(mealId: String): MealWithNutrients

    @Transaction
    @Query("SELECT * FROM meal_filters ORDER BY createdAt DESC")
    suspend fun getMealFilters(): List<MealFilterWithItem>
}