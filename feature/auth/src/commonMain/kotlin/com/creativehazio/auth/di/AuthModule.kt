package com.creativehazio.auth.di

import androidx.lifecycle.ViewModel
import com.creativehazio.auth.presentation.login.LoginViewModel
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
}