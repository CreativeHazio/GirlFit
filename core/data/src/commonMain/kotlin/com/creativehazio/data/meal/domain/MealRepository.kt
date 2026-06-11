package com.creativehazio.data.meal.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun getMeals(filters: List<String>) : Flow<PagingData<Meal>>
    suspend fun getMeal(mealId: String) : Meal
    suspend fun getMealFilters() : List<MealFilter>
}