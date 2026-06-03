package com.creativehazio.startup.di

import androidx.lifecycle.ViewModel
import com.creativehazio.startup.onboarding.OnboardingViewModel
import com.creativehazio.startup.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val startUpModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
}