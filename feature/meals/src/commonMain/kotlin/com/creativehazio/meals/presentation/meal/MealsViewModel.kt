package com.creativehazio.meals.presentation.meal

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.domain.Meal
import kotlinx.coroutines.launch

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
    ),
    val mealFilters: List<MealFilter> = getDummyFilters(),
    val filterItemIds: List<String> = emptyList()
) : State

sealed interface MealsEvent : Event {
    data class OnMealCardClicked(val mealId: String) : MealsEvent
    data class OnSearchQueryChanged(val searchQuery: String) : MealsEvent
    data object OnSearchPressed : MealsEvent
    data class OnFilterItemClicked(val filterItemId: String) : MealsEvent
    data object ApplyFilters : MealsEvent
}

sealed interface MealsEffect : Effect {
    data class NavigateToMealsDetail(val mealId: String) : MealsEffect
}

class MealsViewModel(
) : BaseViewModel<MealsState, MealsEvent, MealsEffect>(
    MealsState()
) {
    override fun onEvent(event: MealsEvent) {
        when(event) {
            is MealsEvent.OnMealCardClicked -> {}
            MealsEvent.OnSearchPressed -> {}
            is MealsEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.searchQuery) }
            }
            is MealsEvent.OnFilterItemClicked -> addFilterItem(event.filterItemId)
            MealsEvent.ApplyFilters -> applyFilters()
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {

        }
    }

    private fun addFilterItem(filterItemId: String) {
        val filterItemIds = uiState.value.filterItemIds
        if (filterItemIds.contains(filterItemId)) {
            updateState { copy(filterItemIds = filterItemIds - filterItemId) }
        } else {
            updateState { copy(filterItemIds = filterItemIds + filterItemId) }
        }
    }
}

data class MealFilter(
    val id: String,
    val name: String,
    val items: List<FilterItem>
)

data class FilterItem(
    val id: String,
    val name: String,
)

fun getDummyFilters() : List<MealFilter> {
    return listOf(
        MealFilter(
            id = "1",
            name = "Goal",
            items = listOf(
                FilterItem(id = "goal_1", name = "Fat loss"),
                FilterItem(id = "goal_2", name = "Muscle tone & strength"),
                FilterItem(id = "goal_3", name = "Energy & performance"),
                FilterItem(id = "goal_4", name = "Hormone & cycle balance"),
                FilterItem(id = "goal_5", name = "Gut health & digestion")
            )
        ),
        MealFilter(
            id = "2",
            name = "Meal time",
            items = listOf(
                FilterItem(id = "meal_time_1", name = "Breakfast"),
                FilterItem(id = "meal_time_2", name = "Lunch"),
                FilterItem(id = "meal_time_3", name = "Dinner"),
                FilterItem(id = "meal_time_4", name = "Snacks"),
                FilterItem(id = "meal_time_5", name = "Smoothies & Drinks")
            )
        ),
        MealFilter(
            id = "3",
            name = "Time needed",
            items = listOf(
                FilterItem(id = "time_needed_1", name = "Under 15 mins"),
                FilterItem(id = "time_needed_2", name = "15 - 30 mins"),
                FilterItem(id = "time_needed_3", name = "30 - 45 mins"),
                FilterItem(id = "time_needed_4", name = "45 - 60 mins"),
                FilterItem(id = "time_needed_5", name = "Over 60 mins")
            )
        )
    )
}