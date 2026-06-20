package com.creativehazio.me.di

import com.creativehazio.me.presentation.MeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val meModule = module {
    viewModelOf(::MeViewModel)
}