package com.creativehazio.auth.presentation.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import kotlinx.coroutines.launch

private const val NAME = "name"
private const val EMAIL = "email"

data class SignUpState (
    val isLoading: Boolean = false,
    val errorMessage: String? = "",
    val name: String = "",
    val email: String = "",
    val password: String = ""
) : State

sealed interface SignUpEvent : Event {
    data class OnNameChanged(val name: String) : SignUpEvent
    data class OnEmailChanged(val email: String) : SignUpEvent
    data object OnSignUpClicked : SignUpEvent
}

sealed interface SignUpEffect : Effect {
    data object NavigateToHome : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
    data class ShowSuccess(val message: String) : SignUpEffect
}

class SignUpViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<SignUpState, SignUpEvent, SignUpEffect>(
    SignUpState(
        name = savedStateHandle[NAME] ?: "",
        email = savedStateHandle[EMAIL] ?: "",
    )
) {


    override fun onEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.OnEmailChanged -> onEmailChanged(event.email)
            is SignUpEvent.OnNameChanged -> onNameChanged(event.name)
            SignUpEvent.OnSignUpClicked -> onSignUpClicked()
        }
    }

    private fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL] = email
        updateState { copy(email = email) }
    }

    private fun onNameChanged(name: String){
        savedStateHandle[NAME] = name
        updateState { copy(name = name) }
    }

    private fun onSignUpClicked() {
        updateState { copy(isLoading = true)}

        viewModelScope.launch {

        }
    }
}