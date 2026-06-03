package com.creativehazio.common.di

import com.creativehazio.common.platform.dataStoreModule
import com.creativehazio.common.util.AppPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    includes(dataStoreModule())

    singleOf(::AppPreferences)
}