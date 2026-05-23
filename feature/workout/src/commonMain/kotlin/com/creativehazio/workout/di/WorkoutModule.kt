package com.creativehazio.workout.di

import androidx.compose.ui.input.key.Key.Companion.W
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val workoutModule = module {
    viewModelOf(::WorkoutDetailViewModel)
}