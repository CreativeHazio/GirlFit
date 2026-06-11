package com.creativehazio.data.meal.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.meal.data.remote.MealDataSource
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealRepository
import com.creativehazio.data.meal.mapper.toFilterItemEntity
import com.creativehazio.data.meal.mapper.toMeal
import com.creativehazio.data.meal.mapper.toMealFilter
import com.creativehazio.data.meal.mapper.toMealFilterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepositoryImpl(
    private val girlFitDatabase: GirlFitDatabase,
    private val mealDataSource: MealDataSource
) : MealRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMeals(filters: List<String>): Flow<PagingData<Meal>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = MealRemoteMediator(
                girlFitDatabase = girlFitDatabase,
                mealDataSource = mealDataSource
            ),
            pagingSourceFactory = {
                girlFitDatabase.mealDao().getMeals(filters)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMeal() }
        }
    }

    override suspend fun getMeal(mealId: String): Meal {
        return try {
            girlFitDatabase.mealDao().getMealById(mealId).toMeal()
        } catch (e: Exception) {
            e.printStackTrace()
            Meal()
        }
    }

    override suspend fun getMealFilters(): List<MealFilter> {
        return try {
            val localFilters = girlFitDatabase.mealDao().getMealFilters()

            if (localFilters.isNotEmpty()) {
                localFilters.map { it.toMealFilter() }
            }

            val remoteFilters = mealDataSource.getMealFilters()

            girlFitDatabase.useWriterConnection { transactor ->
                transactor.immediateTransaction {
                    try {

                        val mealFilterEntities = remoteFilters.map { mealFilterDto ->
                            mealFilterDto.toMealFilterEntity()
                        }

                        val filterItemEntities = remoteFilters.flatMap { mealFilterDto ->
                            mealFilterDto.items.map { filterItemDto ->
                                filterItemDto.toFilterItemEntity(mealFilterDto.id)
                            }
                        }

                        girlFitDatabase.mealDao().insertMealFilters(mealFilterEntities)
                        girlFitDatabase.mealDao().insertFilterItems(filterItemEntities)

                    } catch (e : Exception) {
                        throw e
                    }
                }
            }

            remoteFilters.map { it.toMealFilter() }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}