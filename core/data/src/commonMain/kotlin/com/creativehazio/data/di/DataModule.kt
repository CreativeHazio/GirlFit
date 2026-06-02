package com.creativehazio.data.di

import com.creativehazio.data.workout.data.WorkoutRepositoryImpl
import com.creativehazio.data.workout.data.remote.WorkoutDataSource
import com.creativehazio.data.workout.domain.WorkoutRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(dataPlatformModule())

    singleOf(::WorkoutRepositoryImpl) bind WorkoutRepository::class
    singleOf(::WorkoutDataSource)
    single {
        Firebase.firestore
    }
}

expect fun dataPlatformModule() : Module