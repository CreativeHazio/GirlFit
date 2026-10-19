package com.creativehazio.data.meal.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.creativehazio.data.localdb.GirlFitDatabase
import com.creativehazio.data.meal.data.remote.FilterItemDto
import com.creativehazio.data.meal.data.remote.MealDataSource
import com.creativehazio.data.meal.data.remote.MealDto
import com.creativehazio.data.meal.data.remote.MealFilterDto
import com.creativehazio.data.meal.data.remote.MealNutrientDto
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealRepository
import com.creativehazio.data.meal.mapper.toFilterItemEntity
import com.creativehazio.data.meal.mapper.toMeal
import com.creativehazio.data.meal.mapper.toMealFilter
import com.creativehazio.data.meal.mapper.toMealFilterEntity
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class MealRepositoryImpl(
    private val girlFitDatabase: GirlFitDatabase,
    private val mealDataSource: MealDataSource
) : MealRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMeals(filters: List<String>, searchQuery: String): Flow<PagingData<Meal>> {
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
                girlFitDatabase.mealDao().getMeals(filters, searchQuery)
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

}