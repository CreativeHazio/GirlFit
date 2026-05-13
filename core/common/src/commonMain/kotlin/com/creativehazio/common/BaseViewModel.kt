package com.creativehazio.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface Event

interface State

interface Effect

abstract class BaseViewModel<S: State, E: Event, F: Effect>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = Channel<F>(Channel.BUFFERED)
    val effect: Flow<F> = _effect.receiveAsFlow()


    protected val currentState: S
        get() = _uiState.value

    abstract fun onEvent(event: E)

    protected fun updateState(reduce: S.() -> S) {
        _uiState.update { it.reduce() }
    }

    protected fun sendEffect(effect: F) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

}