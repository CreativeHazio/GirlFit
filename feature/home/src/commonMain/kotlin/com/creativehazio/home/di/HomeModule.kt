package com.creativehazio.home.di

import com.creativehazio.home.presentation.HomeViewModel
import com.creativehazio.home.presentation.components.cycleCalender.CycleCalenderViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::CycleCalenderViewModel)
}