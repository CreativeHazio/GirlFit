package com.creativehazio.startup.splash

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.util.AppPreferences
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    private val appPreferences: AppPreferences
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>(
    SplashState()
) {

    init {
        onEvent(SplashEvent.StartSplashTimer)
    }

    override fun onEvent(event: SplashEvent) {
        when(event) {
            SplashEvent.StartSplashTimer -> {
                viewModelScope.launch {
                    updateState { copy(startAnimation = true) }

                    val user = Firebase.auth.currentUser

                    delay(1500)

                    if (user != null) {
                        if (user.isEmailVerified) {
                            sendEffect(SplashEffect.NavigateToMain)
                        } else {
                            sendEffect(SplashEffect.NavigateToAuth)
                        }
                    } else {
                        val isOnboarded = appPreferences.isOnboarded().first()

                        if (isOnboarded) {
                            sendEffect(SplashEffect.NavigateToAuth)
                        } else {
                            sendEffect(SplashEffect.NavigateToOnboarding)
                        }
                    }
                }
            }
        }
    }
}