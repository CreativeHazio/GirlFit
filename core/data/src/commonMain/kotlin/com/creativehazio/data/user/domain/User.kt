package com.creativehazio.data.user.domain

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val cyclePhase: CyclePhase = CyclePhase.OVULATION
)

enum class CyclePhase {
    NONE, MENSTRUAL, FOLLICULAR, OVULATION, LUTEAL
}