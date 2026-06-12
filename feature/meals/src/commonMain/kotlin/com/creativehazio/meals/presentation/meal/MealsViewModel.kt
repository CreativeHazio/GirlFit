package com.creativehazio.meals.presentation.meal

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.meal.data.MealSeeder
import com.creativehazio.data.meal.domain.FilterItem
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.data.meal.domain.MealRepository
import com.creativehazio.meals.presentation.meal.MealsEffect.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

data class MealsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val meals: Flow<PagingData<Meal>> = flowOf(PagingData.empty()),
    val mealFilters: List<MealFilter> = emptyList(),
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

class MealsViewModel(
    private val mealRepository: MealRepository
) : BaseViewModel<MealsState, MealsEvent, MealsEffect>(
    MealsState()
) {

    init {
        getMeals(emptyList())
        getMealFilters()

//        viewModelScope.launch {
//            val seeder = MealSeeder()
//            seeder.seedDatabaseToFirestore()
//        }
    }
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

    private fun getMealFilters() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val mealFilters = mealRepository.getMealFilters()
            updateState { copy(isLoading = false, mealFilters = mealFilters) }
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