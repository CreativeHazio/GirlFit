package com.creativehazio.data.meal.domain

data class Meal(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val totalCalories: Int = 0,
    val mealNutrients: List<MealNutrient> = emptyList(),
    val filterIds: List<String> = emptyList(),
    val score: Float = 0f
)

data class MealNutrient(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val gramTotal: Int = 0,
)

data class MealFilter(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val items: List<FilterItem> = emptyList()
)

data class FilterItem(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
)