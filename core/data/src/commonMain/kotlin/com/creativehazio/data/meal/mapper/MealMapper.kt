package com.creativehazio.data.meal.mapper

import com.creativehazio.data.meal.data.local.FilterItemEntity
import com.creativehazio.data.meal.data.local.MealEntity
import com.creativehazio.data.meal.data.local.MealFilterEntity
import com.creativehazio.data.meal.data.local.MealFilterWithItem
import com.creativehazio.data.meal.data.local.MealNutrientEntity
import com.creativehazio.data.meal.data.local.MealWithNutrients
import com.creativehazio.data.meal.data.remote.FilterItemDto
import com.creativehazio.data.meal.data.remote.MealDto
import com.creativehazio.data.meal.data.remote.MealFilterDto
import com.creativehazio.data.meal.data.remote.MealNutrientDto
import com.creativehazio.data.meal.domain.FilterItem
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealNutrient


fun MealDto.toMealEntity() : MealEntity {
    return MealEntity(
        id = id,
        createdAt = createdAt,
        name = name,
        imageUrl = imageUrl,
        ingredients = ingredients,
        filterIds = filterIds,
        totalCalories = totalCalories,
        score = score
    )
}

fun MealNutrientDto.toMealNutrientEntity(
    mealId: String
) : MealNutrientEntity {
    return MealNutrientEntity(
        id = id,
        mealId = mealId,
        createdAt = createdAt,
        name = name,
        gramTotal = gramTotal
    )
}

fun MealFilterDto.toMealFilterEntity() : MealFilterEntity {
    return MealFilterEntity(
        id = id,
        createdAt = createdAt,
        name = name
    )
}

fun MealFilterDto.toMealFilter() : MealFilter {
    return MealFilter(
        id = id,
        createdAt = createdAt,
        name = name,
        items = items.map { it.toFilterItem() }
    )
}

fun FilterItemDto.toFilterItemEntity(
    mealFilterId: String
) : FilterItemEntity {
    return FilterItemEntity(
        id = id,
        mealFilterId = mealFilterId,
        createdAt = createdAt,
        name = name
    )
}

fun FilterItemDto.toFilterItem() : FilterItem {
    return FilterItem(
        id = id,
        createdAt = createdAt,
        name = name
    )
}

fun MealWithNutrients.toMeal() : Meal {
    return Meal(
        id = meal.id,
        createdAt = meal.createdAt,
        name = meal.name,
        imageUrl = meal.imageUrl,
        ingredients = meal.ingredients,
        totalCalories = meal.totalCalories,
        mealNutrients = nutrients.map { it.toMealNutrient() },
        filterIds = meal.filterIds,
        score = meal.score
    )
}

fun MealNutrientEntity.toMealNutrient() : MealNutrient {
    return MealNutrient(
        id = id,
        createdAt = createdAt,
        name = name,
        gramTotal = gramTotal
    )
}

fun MealFilterWithItem.toMealFilter() : MealFilter {
    return MealFilter(
        id = mealFilter.id,
        createdAt = mealFilter.createdAt,
        name = mealFilter.name,
        items = filterItems.map { it.toFilterItem() }
    )
}

fun FilterItemEntity.toFilterItem() : FilterItem {
    return FilterItem(
        id = id,
        createdAt = createdAt,
        name = name
    )
}