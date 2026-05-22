package com.creativehazio.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey


@Serializable
data object Login : Route

@Serializable
data object SignUp : Route

@Serializable
data class EmailVerification(val email: String) : Route

@Serializable
data object Main : Route

@Serializable
data object Home : Route

@Serializable
data object Workout : Route

@Serializable
data class WorkoutChallengeCalender(val id: String) : Route

@Serializable
data class WorkoutDetail(val id: String) : Route

@Serializable
data object Progress : Route

@Serializable
data object Meals : Route

@Serializable
data object Me : Route