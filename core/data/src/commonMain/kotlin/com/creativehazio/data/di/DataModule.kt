package com.creativehazio.data.di

import com.creativehazio.data.workout.data.WorkoutRepositoryImpl
import com.creativehazio.data.workout.domain.WorkoutRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    dataPlatformModule()

    singleOf(::WorkoutRepositoryImpl) bind WorkoutRepository::class
}

expect fun dataPlatformModule() : Module