package com.creativehazio.data.user.data.remote

import com.creativehazio.data.user.domain.CyclePhase
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val cyclePhase: String = ""
)
