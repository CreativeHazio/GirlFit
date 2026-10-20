package com.creativehazio.workout.di

import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.workout.presentation.exercise.ExerciseViewModel
import com.creativehazio.workout.presentation.workout.WorkoutViewModel
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderViewModel
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val workoutModule = module {
    viewModel { (workoutId: String) -> WorkoutDetailViewModel(workoutId, get()) }
    viewModelOf(::WorkoutDetailViewModel)
    viewModelOf(::WorkoutViewModel)

    viewModel { (workoutId: String) ->
        WorkoutChallengeCalenderViewModel(workoutId, get())
    }

    viewModel { (workoutId: String) ->
        ExerciseViewModel(workoutId, get())
    }
}