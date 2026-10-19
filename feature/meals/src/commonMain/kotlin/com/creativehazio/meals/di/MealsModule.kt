package com.creativehazio.meals.di

import com.creativehazio.meals.presentation.meal.MealsViewModel
import com.creativehazio.meals.presentation.mealdetail.MealDetailViewModel
import com.creativehazio.meals.presentation.mealscan.MealScanViewModel
import com.creativehazio.meals.presentation.mealscandetails.MealScanDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val mealsModule = module {
    viewModel { (mealId: String) -> MealDetailViewModel(mealId, get()) }
    viewModelOf(::MealsViewModel)
    viewModelOf(::MealScanViewModel)
    viewModelOf(::MealScanDetailViewModel)
}