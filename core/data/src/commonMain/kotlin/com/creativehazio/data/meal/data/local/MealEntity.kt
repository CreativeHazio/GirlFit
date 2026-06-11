package com.creativehazio.data.meal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val MEAL = "meals"
private const val MEAL_NUTRIENT = "meal_nutrients"
private const val MEAL_FILTER = "meal_filters"
private const val FILTER_ITEM = "filter_items"

@Entity(tableName = MEAL)
data class MealEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val name: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val filterIds: List<String>,
    val totalCalories: Int,
    val score: Float
)

@Entity(tableName = MEAL_NUTRIENT)
data class MealNutrientEntity(
    @PrimaryKey val id: String,
    val mealId: String,
    val createdAt: Long,
    val name: String,
    val gramTotal: Int,
)

@Entity(tableName = MEAL_FILTER)
data class MealFilterEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val name: String,
)

@Entity(tableName = FILTER_ITEM)
data class FilterItemEntity(
    @PrimaryKey val id: String,
    val mealFilterId: String,
    val createdAt: Long,
    val name: String,
)