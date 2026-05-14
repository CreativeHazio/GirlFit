package com.creativehazio.auth.presentation.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.auth.data.AuthService
import com.creativehazio.auth.error.SignUpError
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.common.resulthandler.Result
import kotlinx.coroutines.launch

private const val NAME = "name"
private const val EMAIL = "email"

data class SignUpState(
    val isLoading: Boolean = false,
    val errorMessage: String? = "",
    val name: String = "",
    val nameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
) : State

sealed interface SignUpEvent : Event {
    data class OnNameChanged(val name: String) : SignUpEvent
    data class OnEmailChanged(val email: String) : SignUpEvent
    data class OnPasswordChanged(val password: String) : SignUpEvent
    data object OnSignUpClicked : SignUpEvent
    data object OnLoginClicked : SignUpEvent
}

sealed interface SignUpEffect : Effect {
    data object NavigateToLogin : SignUpEffect

    data class NavigateToEmailVerification(val email: String) : SignUpEffect
    data class ShowError(val error: SignUpError) : SignUpEffect
}

//enum class SignUpError : Error {
//    EMPTY_NAME,
//    PASSWORD_TOO_SHORT,
//    PASSWORD_TOO_SIMPLE,
//    INVALID_EMAIL,
//    EMAIL_ALREADY_TAKEN;
//
//    fun message() : UiText {
//        return when(this) {
//            EMPTY_NAME -> UiText.Resource(Res.string.empty_name)
//            PASSWORD_TOO_SHORT -> UiText.Resource(Res.string.password_too_short)
//            PASSWORD_TOO_SIMPLE -> UiText.Resource(Res.string.password_too_simple)
//            INVALID_EMAIL -> UiText.Resource(Res.string.invalid_email)
//            EMAIL_ALREADY_TAKEN -> UiText.Resource(Res.string.email_already_taken)
//        }
//    }
//}

class SignUpViewModel(
    private val authService: AuthService,
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
            is SignUpEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            SignUpEvent.OnLoginClicked -> {
                sendEffect(SignUpEffect.NavigateToLogin)
            }
        }
    }

    private fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL] = email
        updateState { copy(email = email, emailError = null) }
    }

    private fun onNameChanged(name: String) {
        savedStateHandle[NAME] = name
        updateState { copy(name = name, nameError = null) }
    }

    private fun onPasswordChanged(password: String) {
        updateState { copy(password = password, passwordError = null) }
    }

    private fun onSignUpClicked() {
        if (!verifyInput()) return

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            val result = authService.signUpWithEmailAndPassword(
                name = currentState.name,
                email = currentState.email,
                password = currentState.password
            )

            updateState { copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    sendEffect(SignUpEffect.NavigateToEmailVerification(currentState.email))
                }
                is Result.Error -> {
                    when (result.error) {
                        SignUpError.EMAIL_ALREADY_TAKEN,
                        SignUpError.INVALID_EMAIL -> {
                            updateState { copy(emailError = result.error.message()) }
                        }
                        SignUpError.PASSWORD_TOO_SIMPLE,
                        SignUpError.PASSWORD_TOO_SHORT -> {
                            updateState { copy(passwordError = result.error.message()) }
                        }
                        else -> {
                            sendEffect(SignUpEffect.ShowError(result.error))
                        }
                    }
                }
            }
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

        val PASSWORD_COMPLEXITY_REGEX = Regex(
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+\$"
        )

        val nameError = if (currentState.name.isBlank()) {
            SignUpError.EMPTY_NAME.message()
        } else null

        val emailError = when {
            currentState.email.isBlank() || !EMAIL_REGEX.matches(currentState.email) -> {
                SignUpError.INVALID_EMAIL.message()
            }
            else -> null
        }

        val passwordError = when {
            currentState.password.isBlank() || currentState.password.length < 8 -> {
                SignUpError.PASSWORD_TOO_SHORT.message()
            }
            !PASSWORD_COMPLEXITY_REGEX.matches(currentState.password) -> {
                SignUpError.PASSWORD_TOO_SIMPLE.message()
            }
            else -> null
        }

        updateState {
            copy(
                nameError = nameError,
                emailError = emailError,
                passwordError = passwordError
            )
        }

        return nameError == null && emailError == null && passwordError == null
    }
}