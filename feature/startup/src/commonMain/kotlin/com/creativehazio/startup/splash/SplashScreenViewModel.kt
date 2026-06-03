package com.creativehazio.startup.splash

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.util.SecureStorage
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SplashState(
    val startAnimation: Boolean = false,
) : State

sealed interface SplashEvent : Event {
    data object StartSplashTimer : SplashEvent
}

sealed interface SplashEffect : Effect {
    data object NavigateToOnboarding : SplashEffect
    data object NavigateToAuth : SplashEffect
    data object NavigateToMain : SplashEffect
}

class SplashViewModel(
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>(
    SplashState()
) {

    init {
        onEvent(SplashEvent.StartSplashTimer)
    }

    // TODO: Add datastore value for hasBeenOnboarded
    override fun onEvent(event: SplashEvent) {
        when(event) {
            SplashEvent.StartSplashTimer -> {
                viewModelScope.launch {
                    updateState { copy(startAnimation = true) }

                    val user = Firebase.auth.currentUser
                    val isOnboarded = SecureStorage.isOnboarded()

                    delay(2000)

                    if (user != null) {
                        if (user.isEmailVerified) {
                            sendEffect(SplashEffect.NavigateToMain)
                        } else {
                            sendEffect(SplashEffect.NavigateToAuth)
                        }
                    } else {
                        if (!isOnboarded) {
                            sendEffect(SplashEffect.NavigateToOnboarding)
                        } else {
                            sendEffect(SplashEffect.NavigateToAuth)
                        }
                    }
                }
            }
        }
    }
}