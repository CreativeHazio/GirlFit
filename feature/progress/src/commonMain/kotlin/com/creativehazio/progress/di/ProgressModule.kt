package com.creativehazio.progress.di

import com.creativehazio.progress.presentation.ProgressViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val progressModule = module {
    viewModelOf(::ProgressViewModel)
}