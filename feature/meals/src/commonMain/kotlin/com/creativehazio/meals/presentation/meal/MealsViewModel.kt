package com.creativehazio.meals.presentation.meal

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.data.remote.FilterItemDto
import com.creativehazio.data.meal.data.remote.MealFilterDto
import com.creativehazio.data.meal.domain.FilterItem
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealRepository
import com.creativehazio.meals.presentation.meal.MealsEffect.NavigateToMealDetail
import com.creativehazio.meals.presentation.meal.MealsEffect.NavigateToMealScan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init
import kotlin.time.Clock

data class MealsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val currentFilterItemIds: List<String> = emptyList(),
    val filterItemIds: List<String> = emptyList()
) : State

sealed interface MealsEvent : Event {
    data class OnMealCardClicked(val mealId: String) : MealsEvent
    data object OnMealScanCamClicked : MealsEvent
    data class OnSearchQueryChanged(val searchQuery: String) : MealsEvent
    data object OnSearchPressed : MealsEvent
    data class OnFilterItemClicked(val filterItemId: String) : MealsEvent
    data object ApplyFilters : MealsEvent
}

sealed interface MealsEffect : Effect {
    data class NavigateToMealDetail(val mealId: String) : MealsEffect
    data object NavigateToMealScan : MealsEffect
}

@OptIn(ExperimentalCoroutinesApi::class)
class MealsViewModel(
    private val mealRepository: MealRepository
) : BaseViewModel<MealsState, MealsEvent, MealsEffect>(
    MealsState()
) {

    private val _mealFilters = MutableStateFlow(emptyList<String>())
    val mealFilters = _mealFilters.asStateFlow()

    val meals = mealFilters
        .flatMapLatest { filters ->
            mealRepository.getMeals(filters)
        }.cachedIn(viewModelScope)

    override fun onEvent(event: MealsEvent) {
        when(event) {
            is MealsEvent.OnMealCardClicked -> {
                sendEffect(NavigateToMealDetail(event.mealId))
            }
            MealsEvent.OnSearchPressed -> {}
            is MealsEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.searchQuery) }
            }
            is MealsEvent.OnFilterItemClicked -> addFilterItem(event.filterItemId)
            MealsEvent.ApplyFilters -> applyFilters()
            MealsEvent.OnMealScanCamClicked -> {
                sendEffect(NavigateToMealScan)
            }
        }
    }

    private fun applyFilters() {
        getMeals(uiState.value.filterItemIds)
    }

    private fun getMeals(filters: List<String>) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val meals = mealRepository.getMeals(filters).cachedIn(viewModelScope)
            updateState { copy(isLoading = false, meals = meals) }
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

private val mealFiltersList = listOf(
    MealFilter(
        id = "1",
        name = "Goal",
        items = listOf(
            FilterItem(
                id = "goal_1",
                name = "Fat loss"
            ),
            FilterItem(
                id = "goal_2",
                name = "Muscle tone & strength"
            ),
            FilterItem(
                id = "goal_3",
                name = "Energy & performance"
            ),
            FilterItem(
                id = "goal_4",
                name = "Hormone & cycle balance"
            ),
            FilterItem(
                id = "goal_5",
                name = "Gut health & digestion"
            )
        )
    ),
    MealFilter(
        id = "2",
        name = "Meal time",
        items = listOf(
            FilterItem(
                id = "meal_time_1",
                name = "Breakfast"
            ),
            FilterItem(
                id = "meal_time_2",
                name = "Lunch"
            ),
            FilterItem(
                id = "meal_time_3",
                name = "Dinner"
            ),
            FilterItem(
                id = "meal_time_4",
                name = "Snacks"
            ),
            FilterItem(
                id = "meal_time_5",
                name = "Smoothies & Drinks"
            )
        )
    ),
    MealFilter(
        id = "3",
        name = "Time needed",
        items = listOf(
            FilterItem(
                id = "time_needed_1",
                name = "Under 15 mins"
            ),
            FilterItem(
                id = "time_needed_2",
                name = "15 - 30 mins"
            ),
            FilterItem(
                id = "time_needed_3",
                name = "30 - 45 mins"
            ),
            FilterItem(
                id = "time_needed_4",
                name = "45 - 60 mins"
            ),
            FilterItem(
                id = "time_needed_5",
                name = "Over 60 mins"
            )
        )
    )
)