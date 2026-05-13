package com.creativehazio.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.creativehazio.auth.presentation.emailverification.EmailVerificationError
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.common.resulthandler.UiText.*
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.auth
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.empty_password
import girlfit.feature.auth.generated.resources.invalid_credentials
import girlfit.feature.auth.generated.resources.invalid_email
import girlfit.feature.auth.generated.resources.network_error
import girlfit.feature.auth.generated.resources.unknown_error
import girlfit.feature.auth.generated.resources.user_disabled
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
    data object NavigateToEmailVerification : LoginEffect
    data class ShowError(val error: LoginError) : LoginEffect
    data class ShowSuccess(val message: String) : LoginEffect
}

enum class LoginError : Error {
    INVALID_EMAIL,
    EMPTY_PASSWORD,
    USER_DISABLED,
    NETWORK_ERROR,
    UNKNOWN_ERROR,
    INVALID_CREDENTIALS;

    fun message() : UiText {
        return when(this) {
            INVALID_EMAIL -> Resource(Res.string.invalid_email)
            EMPTY_PASSWORD -> Resource(Res.string.empty_password)
            INVALID_CREDENTIALS -> Resource(Res.string.invalid_credentials)
            USER_DISABLED -> Resource(Res.string.user_disabled)
            NETWORK_ERROR -> Resource(Res.string.network_error)
            UNKNOWN_ERROR -> Resource(Res.string.unknown_error)
        }
    }

    companion object {
        fun fromMessage(message: String?): LoginError {
            return when {
                message?.contains("invalid-credential", ignoreCase = true) == true ->
                    INVALID_CREDENTIALS
                message?.contains("user-disabled", ignoreCase = true) == true ->
                    USER_DISABLED
                message?.contains("network-request-failed", ignoreCase = true) == true ->
                    NETWORK_ERROR
                else ->
                    UNKNOWN_ERROR
            }
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
            try {

                val authResult = Firebase.auth.signInWithEmailAndPassword(
                    email = currentState.email,
                    password = currentState.password
                )

                if (authResult.user?.isEmailVerified == true) {
                    updateState { copy(isLoading = false) }
                    sendEffect(LoginEffect.NavigateToHome)
                } else {
                    updateState { copy(isLoading = false) }
                    sendEffect(LoginEffect.NavigateToEmailVerification)
                }

            } catch (e: FirebaseAuthException) {
                updateState { copy(isLoading = false) }

                val errorEnum = LoginError.fromMessage(e.message)
                sendEffect(LoginEffect.ShowError(errorEnum))

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