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

    @Query("DELETE FROM meals")
    suspend fun clearAllMeals()

    @Query("DELETE FROM meal_nutrients")
    suspend fun clearAllMealNutrients()

    @Transaction
    @RawQuery(observedEntities = [MealEntity::class])
    fun getMealsRawQuery(query: RoomRawQuery): PagingSource<Int, MealWithNutrients>

    fun getMeals(filters: List<String>, searchQuery: String): PagingSource<Int, MealWithNutrients> {
        val queryString = StringBuilder("SELECT * FROM meals")
        val conditions = mutableListOf<String>()
        val search = searchQuery.trim()

        if (filters.isNotEmpty()) {
            val filterConditions = filters.map {
                "',' || filterIds || ',' LIKE ?"
            }

            conditions += "(${filterConditions.joinToString(" OR ")})"
        }

        if (search.isNotEmpty()) {
            conditions += "name LIKE ? COLLATE NOCASE"
        }

        if (conditions.isNotEmpty()) {
            queryString.append(" WHERE ")
            queryString.append(conditions.joinToString(" AND "))
        }

        queryString.append(" ORDER BY createdAt DESC")

        val query = RoomRawQuery(
            sql = queryString.toString(),
            onBindStatement = { statement ->
                var index = 1

                filters.forEach { filterId ->
                    statement.bindText(index++, "%,$filterId,%")
                }

                if (search.isNotEmpty()) {
                    statement.bindText(index, "%$search%")
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
    suspend fun getMealById(mealId: String): MealWithNutrients
}