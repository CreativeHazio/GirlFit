package com.creativehazio.data.meal.data.remote

import com.creativehazio.data.meal.domain.FilterItem
import com.creativehazio.data.meal.domain.MealNutrient
import kotlinx.serialization.Serializable

@Serializable
data class MealDto(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val imageUrl: String = "",
    val filterIds: List<String> = emptyList(),
    val ingredients: List<String> = emptyList(),
    val totalCalories: Int = 0,
    val mealNutrients: List<MealNutrientDto> = emptyList(),
    val score: Float = 0f
)

@Serializable
data class MealNutrientDto(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val gramTotal: Int = 0,
)

@Serializable
data class MealFilterDto(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
    val items: List<FilterItemDto> = emptyList()
)

@Serializable
data class FilterItemDto(
    val id: String = "",
    val createdAt: Long = 0L,
    val name: String = "",
)
