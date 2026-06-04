package com.creativehazio.data.meal.domain

data class Meal(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val totalCalories: Int = 0,
    val mealNutrients: List<MealNutrient> = emptyList()
)

data class MealNutrient(
    val id: String = "",
    val name: String = "",
    val gramTotal: Int = 0,
)