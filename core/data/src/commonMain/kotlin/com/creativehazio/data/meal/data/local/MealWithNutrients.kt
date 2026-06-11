package com.creativehazio.data.meal.data.local

import androidx.room.Embedded
import androidx.room.Relation

private const val ID = "id"
private const val MEAL_ID = "mealId"

data class MealWithNutrients(
    @Embedded
    val meal: MealEntity,

    @Relation(
        parentColumn = ID,
        entityColumn = MEAL_ID
    )
    val nutrients : List<MealNutrientEntity>,
)