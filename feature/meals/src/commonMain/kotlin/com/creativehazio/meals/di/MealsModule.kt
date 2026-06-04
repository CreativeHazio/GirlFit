package com.creativehazio.meals.di

import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.meals.presentation.meals.MealsViewModel
import com.creativehazio.meals.presentation.mealsdetail.MealDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mealsModule = module {
    viewModelOf(::MealsViewModel)
    viewModelOf(::MealDetailViewModel)
}