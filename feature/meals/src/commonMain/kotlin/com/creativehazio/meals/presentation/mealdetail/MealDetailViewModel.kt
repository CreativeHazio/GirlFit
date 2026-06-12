package com.creativehazio.meals.presentation.mealdetail

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealNutrient
import com.creativehazio.data.meal.domain.MealRepository
import kotlinx.coroutines.launch

data class MealDetailState(
    val isLoading: Boolean = false,
    val meal: Meal = Meal(),
) : State

sealed interface MealDetailEvent : Event {
    data object OnBackClicked : MealDetailEvent
    data class GetMealDetailById(val mealId: String) : MealDetailEvent
}

sealed interface MealDetailEffect : Effect {
    data object NavigateBack : MealDetailEffect
}

class MealDetailViewModel(
    private val mealRepository: MealRepository
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
            val meal = mealRepository.getMeal(mealId)
            updateState { copy(meal = meal) }
        }
    }
}