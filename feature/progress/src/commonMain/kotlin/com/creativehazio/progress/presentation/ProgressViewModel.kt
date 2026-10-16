package com.creativehazio.progress.presentation

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State

data class ProgressState(
    val isLoading: Boolean = false
) : State

sealed interface ProgressEvent : Event {}

sealed interface ProgressEffect : Effect {}

class ProgressViewModel(
) : BaseViewModel<ProgressState, ProgressEvent, ProgressEffect>(
    ProgressState()
) {
    override fun onEvent(event: ProgressEvent) {

    }
}