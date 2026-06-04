package com.creativehazio.meals.di

import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.meals.presentation.meal.MealsViewModel
import com.creativehazio.meals.presentation.mealdetail.MealDetailViewModel
import com.creativehazio.meals.presentation.mealscan.MealScanViewModel
import com.creativehazio.meals.presentation.mealscandetails.MealScanDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mealsModule = module {
    viewModelOf(::MealsViewModel)
    viewModelOf(::MealDetailViewModel)
    viewModelOf(::MealScanViewModel)
    viewModelOf(::MealScanDetailViewModel)
}