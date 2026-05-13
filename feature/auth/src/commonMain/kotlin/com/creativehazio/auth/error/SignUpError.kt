package com.creativehazio.auth.error

import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.common.resulthandler.UiText.Resource
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.email_already_taken
import girlfit.feature.auth.generated.resources.empty_name
import girlfit.feature.auth.generated.resources.invalid_email
import girlfit.feature.auth.generated.resources.network_error
import girlfit.feature.auth.generated.resources.password_too_short
import girlfit.feature.auth.generated.resources.password_too_simple
import girlfit.feature.auth.generated.resources.unknown_error

enum class SignUpError : Error {
    EMPTY_NAME,
    PASSWORD_TOO_SHORT,
    PASSWORD_TOO_SIMPLE,
    INVALID_EMAIL,
    NETWORK_ERROR,
    UNKNOWN_ERROR,
    EMAIL_ALREADY_TAKEN;

    fun message() : UiText {
        return when(this) {
            EMPTY_NAME -> Resource(Res.string.empty_name)
            PASSWORD_TOO_SHORT -> Resource(Res.string.password_too_short)
            PASSWORD_TOO_SIMPLE -> Resource(Res.string.password_too_simple)
            INVALID_EMAIL -> Resource(Res.string.invalid_email)
            NETWORK_ERROR -> Resource(Res.string.network_error)
            UNKNOWN_ERROR -> Resource(Res.string.unknown_error)
            EMAIL_ALREADY_TAKEN -> Resource(Res.string.email_already_taken)
        }
    }

    companion object {
        fun fromMessage(message: String?) : SignUpError {
            return when {
                message?.contains("email-already-in-use") == true ->
                   EMAIL_ALREADY_TAKEN
                message?.contains("invalid-email") == true ->
                    INVALID_EMAIL
                message?.contains("weak-password") == true ->
                    PASSWORD_TOO_SIMPLE
                else -> UNKNOWN_ERROR
            }
        }
    }
}