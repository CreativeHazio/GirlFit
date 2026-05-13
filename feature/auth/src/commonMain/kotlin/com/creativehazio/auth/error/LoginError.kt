package com.creativehazio.auth.error

import com.creativehazio.common.resulthandler.Error
import com.creativehazio.common.resulthandler.UiText
import com.creativehazio.common.resulthandler.UiText.Resource
import girlfit.feature.auth.generated.resources.Res
import girlfit.feature.auth.generated.resources.empty_password
import girlfit.feature.auth.generated.resources.invalid_credentials
import girlfit.feature.auth.generated.resources.invalid_email
import girlfit.feature.auth.generated.resources.network_error
import girlfit.feature.auth.generated.resources.unknown_error
import girlfit.feature.auth.generated.resources.user_disabled
import girlfit.feature.auth.generated.resources.verify_email_body

enum class LoginError : Error {
    INVALID_EMAIL,
    EMPTY_PASSWORD,
    USER_DISABLED,
    USER_NOT_VERIFIED,
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
            USER_NOT_VERIFIED -> Resource(Res.string.verify_email_body, "you")
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