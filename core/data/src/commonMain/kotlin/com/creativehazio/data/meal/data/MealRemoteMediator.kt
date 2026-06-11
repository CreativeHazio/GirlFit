package com.creativehazio.data.meal.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.meal.data.local.MealEntity
import com.creativehazio.data.meal.data.local.MealWithNutrients
import com.creativehazio.data.meal.data.remote.MealDataSource
import com.creativehazio.data.meal.mapper.toMealEntity
import com.creativehazio.data.meal.mapper.toMealNutrientEntity

@OptIn(ExperimentalPagingApi::class)
class MealRemoteMediator(
    private val girlFitDatabase: GirlFitDatabase,
    private val mealDataSource: MealDataSource
) : RemoteMediator<Int, MealWithNutrients>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MealWithNutrients>
    ): MediatorResult {
        return try {

            val lastTimestamp: Long? = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = girlFitDatabase.mealDao().getLastMeal()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    lastItem.meal.createdAt
                }
            }

            val firestoreMeals = mealDataSource.getMeals(
                limit = state.config.pageSize,
                lastTimestamp = lastTimestamp
            )

            girlFitDatabase.useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    try {
                        if (loadType == LoadType.REFRESH) {
                            girlFitDatabase.mealDao().clearAllMeals()
                            girlFitDatabase.mealDao().clearAllMealNutrients()
                        }

                        val mealEntities = firestoreMeals.map { it.toMealEntity() }

                        val mealNutrientEntities = firestoreMeals.flatMap { mealDto ->
                            mealDto.mealNutrients.map { mealNutrientDto ->
                                mealNutrientDto.toMealNutrientEntity(
                                    mealId = mealDto.id
                                )
                            }
                        }

                        girlFitDatabase.mealDao().insertMeals(mealEntities)
                        girlFitDatabase.mealDao().insertMealNutrients(mealNutrientEntities)
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = firestoreMeals.isEmpty()
            )

        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

}