package com.creativehazio.data.meal.data.remote

import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore

private const val MEALS = "meals"
private const val MEAL_FILTERS = "mealFilters"
private const val CREATED_AT = "createdAt"

class MealDataSource(
    private val firestore: FirebaseFirestore
) {

    suspend fun getMeals(
        limit: Int,
        lastTimestamp: Long? = null
    ): List<MealDto> {

        var query = firestore.collection(MEALS)
            .orderBy(CREATED_AT, Direction.DESCENDING)
            .limit(limit.toLong())

        if (lastTimestamp != null) {
            query = query.startAfterFieldValues { arrayOf<Any?>(lastTimestamp).forEach { add(it) } }
        }

        return query.get().documents.map { it.data() }

    }

    suspend fun getMealFilters() : List<MealFilterDto> {
        val query = firestore.collection(MEAL_FILTERS)
            .orderBy(CREATED_AT, Direction.DESCENDING)

        return query.get().documents.map { it.data() }
    }

}