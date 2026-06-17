package com.creativehazio.data.meal.data.local

import androidx.room.Embedded
import androidx.room.Relation

private const val ID = "id"
private const val MEAL_FILTER_ID = "mealFilterId"

data class MealFilterWithItem(
    @Embedded
    val mealFilter: MealFilterEntity,

    @Relation(
        parentColumn = ID,
        entityColumn = MEAL_FILTER_ID
    )
    val filterItems : List<FilterItemEntity>,
)