package com.creativehazio.meals.presentation.mealscandetails

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealNutrient

data class MealScanDetailState(
    val isLoading: Boolean = false,
    val meal: Meal = Meal(
        id = "",
        name = "",
        imageUrl = "",
        ingredients = listOf(
            "Grilled skinless chicken thighs",
            "Rice",
            "Green peas",
            "Red ball peppers",
            "Lemon slices",
        ),
        totalCalories = 1540,
        mealNutrients = listOf(
            MealNutrient(
                id = "1",
                name = "Carbohydrate",
                gramTotal = 100
            ),
            MealNutrient(
                id = "2",
                name = "Fat",
                gramTotal = 25
            ),
            MealNutrient(
                id = "3",
                name = "Protein",
                gramTotal = 100
            ),
            MealNutrient(
                id = "4",
                name = "Fiber",
                gramTotal = 15
            ),
        ),
        score = 40f
    ),
) : State

sealed interface MealScanDetailEvent : Event {

}

sealed interface MealScanDetailEffect : Effect {}

class MealScanDetailViewModel(
) : BaseViewModel<MealScanDetailState, MealScanDetailEvent, MealScanDetailEffect>(
    MealScanDetailState()
) {
    override fun onEvent(event: MealScanDetailEvent) {

    }
}