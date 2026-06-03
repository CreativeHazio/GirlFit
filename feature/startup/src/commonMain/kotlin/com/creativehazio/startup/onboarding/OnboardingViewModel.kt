package com.creativehazio.startup.onboarding

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.common.resulthandler.UiText
import girlfit.feature.startup.generated.resources.Res
import girlfit.feature.startup.generated.resources.during_workouts
import girlfit.feature.startup.generated.resources.gentle_motivation
import girlfit.feature.startup.generated.resources.get_gentle_motivations
import girlfit.feature.startup.generated.resources.get_meals_suggestions
import girlfit.feature.startup.generated.resources.goals_based
import girlfit.feature.startup.generated.resources.meals_suggestions
import girlfit.feature.startup.generated.resources.mood_workout
import girlfit.feature.startup.generated.resources.personal_workout
import girlfit.feature.startup.generated.resources.routine_based
import girlfit.feature.startup.generated.resources.routine_workout
import girlfit.feature.startup.generated.resources.stay_healthy
import girlfit.feature.startup.generated.resources.stay_healty
import girlfit.feature.startup.generated.resources.with_friends
import girlfit.feature.startup.generated.resources.without_pressure
import girlfit.feature.startup.generated.resources.workout
import girlfit.feature.startup.generated.resources.workout_based_on
import girlfit.feature.startup.generated.resources.workout_with_friends
import girlfit.feature.startup.generated.resources.your_mood
import org.jetbrains.compose.resources.DrawableResource

data class OnboardingState(
    val isLoading: Boolean = false,
    val onboardingItems: List<OnboardingItem> = getAllOnboardingItems()
) : State

sealed interface OnboardingEvent : Event {
    data object OnSkipOrContinuePressed : OnboardingEvent
}

sealed interface OnboardingEffect : Effect {
    data object NavigateToAuth : OnboardingEffect
}

class OnboardingViewModel(
) : BaseViewModel<OnboardingState, OnboardingEvent, OnboardingEffect>(
    OnboardingState()
) {
    override fun onEvent(event: OnboardingEvent) {
        when(event) {
            OnboardingEvent.OnSkipOrContinuePressed -> {
                sendEffect(OnboardingEffect.NavigateToAuth)
            }
        }
    }
}

data class OnboardingItem(
    val image: DrawableResource,
    val topText: UiText,
    val topTextPadding: Dp,
    val bottomText: UiText,
    val bottomTextPadding: Dp
)

internal fun getAllOnboardingItems() : List<OnboardingItem> {
    return listOf(
        OnboardingItem(
            image = Res.drawable.stay_healty,
            topText = UiText.Resource(Res.string.stay_healthy),
            topTextPadding = 32.dp,
            bottomText = UiText.Resource(Res.string.without_pressure),
            bottomTextPadding = 32.dp
        ),
        OnboardingItem(
            image = Res.drawable.gentle_motivation,
            topText = UiText.Resource(Res.string.get_gentle_motivations),
            bottomText = UiText.Resource(Res.string.during_workouts),
            topTextPadding = 32.dp,
            bottomTextPadding = 32.dp,
        ),
        OnboardingItem(
            image = Res.drawable.workout_with_friends,
            topText = UiText.Resource(Res.string.workout),
            bottomText = UiText.Resource(Res.string.with_friends),
            topTextPadding = 64.dp,
            bottomTextPadding = 64.dp,
        ),
        OnboardingItem(
            image = Res.drawable.routine_workout,
            topText = UiText.Resource(Res.string.personal_workout),
            bottomText = UiText.Resource(Res.string.routine_based),
            topTextPadding = 12.dp,
            bottomTextPadding = 12.dp,
        ),
        OnboardingItem(
            image = Res.drawable.mood_workout,
            topText = UiText.Resource(Res.string.workout_based_on),
            bottomText = UiText.Resource(Res.string.your_mood),
            topTextPadding = 64.dp,
            bottomTextPadding = 64.dp,
        ),
        OnboardingItem(
            image = Res.drawable.meals_suggestions,
            topText = UiText.Resource(Res.string.get_meals_suggestions),
            bottomText = UiText.Resource(Res.string.goals_based),
            topTextPadding = 12.dp,
            bottomTextPadding = 12.dp,
        ),
    )
}