package com.creativehazio.meals.presentation.mealdetail

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealNutrient
import kotlinx.coroutines.launch

data class MealDetailState(
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
        )
    ),
) : State

sealed interface MealDetailEvent : Event {
    data object OnBackClicked : MealDetailEvent
    data class GetMealDetailById(val mealId: String) : MealDetailEvent
}

sealed interface MealDetailEffect : Effect {
    data object NavigateBack : MealDetailEffect
}

class MealDetailViewModel(
) : BaseViewModel<MealDetailState, MealDetailEvent, MealDetailEffect>(
    MealDetailState()
) {
    override fun onEvent(event: MealDetailEvent) {
        when (event) {
            MealDetailEvent.OnBackClicked -> sendEffect(MealDetailEffect.NavigateBack)
            is MealDetailEvent.GetMealDetailById -> getMealDetailById(event.mealId)
        }
    }

    private fun getMealDetailById(mealId: String) {
        viewModelScope.launch {

        }
    }
}