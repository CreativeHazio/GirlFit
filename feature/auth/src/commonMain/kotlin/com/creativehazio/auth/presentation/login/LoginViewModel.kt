package com.creativehazio.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.empty_password
import girlfit.feature.auth.generated.resources.invalid_credentials
import girlfit.feature.auth.generated.resources.invalid_email
import kotlinx.coroutines.launch

private const val EMAIL = "email"

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String? = "",
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
) : State

sealed interface LoginEvent : Event {
    data class OnEmailChanged(val email: String) : LoginEvent
    data class OnPasswordChanged(val password: String) : LoginEvent
    data object OnLoginClicked : LoginEvent
    data object OnSignUpClicked : LoginEvent
}

sealed interface LoginEffect : Effect {
    data object NavigateToHome : LoginEffect
    data object NavigateToSignUp : LoginEffect
    data class ShowError(val error: LoginError) : LoginEffect
    data class ShowSuccess(val message: String) : LoginEffect
}

enum class LoginError : Error {
    INVALID_EMAIL,
    EMPTY_PASSWORD,
    INVALID_CREDENTIALS;

    fun message() : UiText {
        return when(this) {
            INVALID_EMAIL -> UiText.Resource(Res.string.invalid_email)
            EMPTY_PASSWORD -> UiText.Resource(Res.string.empty_password)
            INVALID_CREDENTIALS -> UiText.Resource(Res.string.invalid_credentials)
        }
    }
}

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(
    LoginState(
        email = savedStateHandle[EMAIL] ?: "",
    )
) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> onEmailChanged(event.email)
            is LoginEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            LoginEvent.OnLoginClicked -> onLoginClicked()
            LoginEvent.OnSignUpClicked -> {
                sendEffect(LoginEffect.NavigateToSignUp)
            }
        }
    }

    private fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL] = email
        updateState { copy(email = email, emailError = null) }
    }

    private fun onPasswordChanged(password: String) {
        updateState { copy(password = password, passwordError = null) }
    }

    private fun onLoginClicked() {
        if (!verifyInput()) return

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            // TODO: Call your AuthRepository here
            // If it fails with wrong password/email, you can emit:
            // sendEffect(LoginEffect.ShowError(LoginError.INVALID_CREDENTIALS))
            // updateState { copy(isLoading = false) }
        }
    }

    private fun verifyInput(): Boolean {
        val EMAIL_REGEX = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        val emailError = when {
            currentState.email.isBlank() || !EMAIL_REGEX.matches(currentState.email) -> {
                LoginError.INVALID_EMAIL.message()
            }
            else -> null
        }

        // For login, we only check if it's empty. Let the backend decide if it's wrong.
        val passwordError = if (currentState.password.isBlank()) {
            LoginError.EMPTY_PASSWORD.message()
        } else null

        updateState {
            copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        return emailError == null && passwordError == null
    }
}