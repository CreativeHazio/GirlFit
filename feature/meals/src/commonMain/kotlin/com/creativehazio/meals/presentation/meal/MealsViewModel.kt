package com.creativehazio.meals.presentation.meal

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.domain.Meal

data class MealsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val meals: List<Meal> = listOf(
        Meal(
            id = "1",
            imageUrl = "",
        ),
        Meal(
            id = "2",
            imageUrl = "",
        ),
        Meal(
            id = "3",
            imageUrl = "",
        ),
        Meal(
            id = "4",
            imageUrl = "",
        ),
        Meal(
            id = "5",
            imageUrl = "",
        ),
        Meal(
            id = "6",
            imageUrl = "",
        ),
        Meal(
            id = "7",
            imageUrl = "",
        ),
        Meal(
            id = "8",
            imageUrl = "",
        ),
    )
) : State

sealed interface MealsEvent : Event {
    data class OnMealCardClicked(val mealId: String) : MealsEvent
}

sealed interface MealsEffect : Effect {
    data class NavigateToMealsDetail(val mealId: String) : MealsEffect
}

class MealsViewModel(
) : BaseViewModel<MealsState, MealsEvent, MealsEffect>(
    MealsState()
) {
    override fun onEvent(event: MealsEvent) {

    }
}