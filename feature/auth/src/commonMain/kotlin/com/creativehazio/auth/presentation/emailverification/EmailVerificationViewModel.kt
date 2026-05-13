package com.creativehazio.auth.presentation.emailverification

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.auth
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.network_error
import girlfit.feature.auth.generated.resources.resend_email
import girlfit.feature.auth.generated.resources.resend_in
import girlfit.feature.auth.generated.resources.sent
import girlfit.feature.auth.generated.resources.too_many_requests
import girlfit.feature.auth.generated.resources.unknown_error
import girlfit.feature.auth.generated.resources.user_not_found
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class EmailVerificationState(
    val isLoading: Boolean = false,
    val isResendEnabled: Boolean = true,
    val timerText: UiText = UiText.Resource(Res.string.resend_email)
) : State

sealed interface EmailVerificationEvent : Event {
    data object ResendEmailVerification : EmailVerificationEvent
    data object OnBackToLoginClicked : EmailVerificationEvent
}

sealed interface EmailVerificationEffect : Effect {

    data object NavigateToLogin : EmailVerificationEffect
    data class ShowSuccess(val message: UiText) : EmailVerificationEffect
    data class ShowError(val message: UiText) : EmailVerificationEffect
}

enum class EmailVerificationError : Error {
    TOO_MANY_REQUESTS,
    USER_NOT_FOUND,
    NETWORK_ERROR,
    UNKNOWN_ERROR;

    fun message(): UiText {
        return when (this) {
            TOO_MANY_REQUESTS -> UiText.Resource(Res.string.too_many_requests)
            USER_NOT_FOUND -> UiText.Resource(Res.string.user_not_found)
            NETWORK_ERROR -> UiText.Resource(Res.string.network_error)
            UNKNOWN_ERROR -> UiText.Resource(Res.string.unknown_error)
        }
    }

    companion object {
        fun fromMessage(message: String?): EmailVerificationError {
            return when {
                message?.contains("too-many-requests", ignoreCase = true) == true ->
                    TOO_MANY_REQUESTS

                message?.contains("user-not-found", ignoreCase = true) == true ->
                    USER_NOT_FOUND

                message?.contains("network-request-failed", ignoreCase = true) == true ->
                    NETWORK_ERROR

                else ->
                    UNKNOWN_ERROR
            }
        }
    }
}

class EmailVerificationViewModel :
    BaseViewModel<EmailVerificationState, EmailVerificationEvent, EmailVerificationEffect>(
        EmailVerificationState()
    ) {

    override fun onEvent(event: EmailVerificationEvent) {
        when (event) {
            EmailVerificationEvent.ResendEmailVerification -> resendVerification()
            EmailVerificationEvent.OnBackToLoginClicked -> {
                sendEffect(EmailVerificationEffect.NavigateToLogin)
            }
        }
    }

    private fun resendVerification() {
        if (!currentState.isResendEnabled || currentState.isLoading) return

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            try {
                Firebase.auth.currentUser?.sendEmailVerification()

                updateState { copy(isLoading = false) }
                sendEffect(
                    EmailVerificationEffect.ShowSuccess(
                        UiText.Resource(Res.string.sent)
                    )
                )

                startCooldownTimer()

            } catch (e: FirebaseAuthException) {
                updateState { copy(isLoading = false) }

                val errorEnum = EmailVerificationError.fromMessage(e.message)

                val finalUiText =
                    if (errorEnum == EmailVerificationError.UNKNOWN_ERROR && e.message != null) {
                        UiText.DynamicString(e.message!!)
                    } else {
                        errorEnum.message()
                    }

                sendEffect(EmailVerificationEffect.ShowError(finalUiText))
            }
        }
    }

    private fun startCooldownTimer() {
        viewModelScope.launch {
            updateState { copy(isResendEnabled = false) }

            for (i in 30 downTo 1) {
                updateState { copy(timerText = UiText.Resource(Res.string.resend_in, "${i}s")) }
                delay(1000L)
            }

            updateState {
                copy(
                    isResendEnabled = true,
                    timerText = UiText.Resource(Res.string.resend_email)
                )
            }
        }
    }
}