package com.creativehazio.auth.presentation.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.email_already_taken
import girlfit.feature.auth.generated.resources.empty_name
import girlfit.feature.auth.generated.resources.invalid_email
import girlfit.feature.auth.generated.resources.password_too_short
import girlfit.feature.auth.generated.resources.password_too_simple
import kotlinx.coroutines.launch

private const val NAME = "name"
private const val EMAIL = "email"

data class SignUpState(
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
    data class ShowError(val error: SignUpError) : SignUpEffect
    data class ShowSuccess(val message: String) : SignUpEffect
}

enum class SignUpError : Error {
    EMPTY_NAME,
    PASSWORD_TOO_SHORT,
    PASSWORD_TOO_SIMPLE,
    INVALID_EMAIL,
    EMAIL_ALREADY_TAKEN;

    fun message() : UiText {
        return when(this) {
            EMPTY_NAME -> UiText.Resource(Res.string.empty_name)
            PASSWORD_TOO_SHORT -> UiText.Resource(Res.string.password_too_short)
            PASSWORD_TOO_SIMPLE -> UiText.Resource(Res.string.password_too_simple)
            INVALID_EMAIL -> UiText.Resource(Res.string.invalid_email)
            EMAIL_ALREADY_TAKEN -> UiText.Resource(Res.string.email_already_taken)
        }
    }
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
        when (event) {
            is SignUpEvent.OnEmailChanged -> onEmailChanged(event.email)
            is SignUpEvent.OnNameChanged -> onNameChanged(event.name)
            SignUpEvent.OnSignUpClicked -> onSignUpClicked()
        }
    }

    private fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL] = email
        updateState { copy(email = email) }
    }

    private fun onNameChanged(name: String) {
        savedStateHandle[NAME] = name
        updateState { copy(name = name) }
    }

    private fun onSignUpClicked() {
        if (verifyInput() != null) return

        updateState { copy(isLoading = true) }

        viewModelScope.launch {

        }
    }

    private fun verifyInput(): SignUpError? {
        val EMAIL_REGEX = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        val PASSWORD_COMPLEXITY_REGEX = Regex(
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+\$"
        )

        return when {
            currentState.name.isBlank() -> {
                SignUpError.EMPTY_NAME
                // Send Effects here
            }

            currentState.email.isBlank()
                    || !EMAIL_REGEX.matches(currentState.email) -> {
                SignUpError.INVALID_EMAIL
            }

            currentState.password.isBlank() || currentState.password.length < 8 -> {
                SignUpError.PASSWORD_TOO_SHORT
            }

            PASSWORD_COMPLEXITY_REGEX.matches(currentState.password) -> {
                SignUpError.PASSWORD_TOO_SIMPLE
            }

            else -> null
        }
    }
}