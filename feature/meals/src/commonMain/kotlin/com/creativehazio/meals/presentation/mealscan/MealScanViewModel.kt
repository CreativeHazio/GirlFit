package com.creativehazio.meals.presentation.mealscan

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State

data class MealScanState(
    val isLoading: Boolean = false
) : State

sealed interface MealScanEvent : Event {}

sealed interface MealScanEffect : Effect {}

class MealScanViewModel(
) : BaseViewModel<MealScanState, MealScanEvent, MealScanEffect>(
    MealScanState()
) {
    override fun onEvent(event: MealScanEvent) {

    }
}