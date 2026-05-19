package com.creativehazio.home.presentation

import androidx.lifecycle.SavedStateHandle
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State

data class HomeState(
    val isLoading: Boolean = false
) : State

sealed interface HomeEvent : Event {

}

sealed interface HomeEffect : Effect {

}

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(initialState = HomeState()) {

    override fun onEvent(event: HomeEvent) {
        when(event) {
            else -> {}
        }
    }
}