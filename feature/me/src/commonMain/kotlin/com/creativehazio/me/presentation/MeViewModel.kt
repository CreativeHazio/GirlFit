package com.creativehazio.me.presentation

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import girlfit.feature.me.generated.resources.Res
import girlfit.feature.me.generated.resources.gold_trophy_win
import girlfit.feature.me.generated.resources.purple_trophy_win
import girlfit.feature.me.generated.resources.time_w_friend_win
import org.jetbrains.compose.resources.DrawableResource

data class MeState(
    val isLoading: Boolean = false,
    val userWins: List<UserWin> = getMockUserWins()
) : State

sealed interface MeEvent : Event {}

sealed interface MeEffect : Effect {}

class MeViewModel(
) : BaseViewModel<MeState, MeEvent, MeEffect>(
    MeState()
) {
    override fun onEvent(event: MeEvent) {
        TODO("Not yet implemented")
    }
}

data class UserWin(
    val id: String = "",
    val name: String = "",
    //TODO: Change to url
    val image: DrawableResource
)

data class Setting(
    val leadingIcon: DrawableResource,
    val name: String,
    val subtitle: String? = null,
    val trailingIcon: DrawableResource,
)

private fun getMockUserWins() : List<UserWin> {
    return listOf(
        UserWin(
            id = "1",
            name = "7 days streak",
            image = Res.drawable.purple_trophy_win
        ),
        UserWin(
            id = "2",
            name = "1-time with friend",
            image = Res.drawable.time_w_friend_win
        ),
        UserWin(
            id = "3",
            name = "1 challenge",
            image = Res.drawable.gold_trophy_win
        ),
    )
}