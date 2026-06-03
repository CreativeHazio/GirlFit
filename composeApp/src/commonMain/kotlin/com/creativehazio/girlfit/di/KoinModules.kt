package com.creativehazio.girlfit.di

import com.creativehazio.auth.di.authModule
import com.creativehazio.common.di.commonModule
import com.creativehazio.data.di.dataModule
import com.creativehazio.fitnessbuddy.di.fitnessBuddyModule
import com.creativehazio.home.di.homeModule
import com.creativehazio.me.di.meModule
import com.creativehazio.meals.di.mealsModule
import com.creativehazio.progress.di.progressModule
import com.creativehazio.startup.di.startUpModule
import com.creativehazio.workout.di.workoutModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModules: Module

val sharedModules = listOf(
    commonModule,
    startUpModule,
    authModule,
    dataModule,
    homeModule,
    workoutModule,
    progressModule,
    mealsModule,
    meModule,
    fitnessBuddyModule
)