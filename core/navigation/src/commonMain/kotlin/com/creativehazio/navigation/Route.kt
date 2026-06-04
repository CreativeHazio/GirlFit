package com.creativehazio.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey

@Serializable
data object Splash : Route

@Serializable
data object Onboarding : Route

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
data class WorkoutDetail(val workoutId: String) : Route

@Serializable
data object Progress : Route

@Serializable
data object Meals : Route

@Serializable
data class MealDetail(val mealId: String) : Route

@Serializable
data object Me : Route