package com.zanoapps.auth.presentation.di

import com.zanoapps.auth.presentation.login.LoginViewModel
import com.zanoapps.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}
