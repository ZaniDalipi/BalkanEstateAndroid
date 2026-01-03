package com.zanoapps.profile.presentation.di

import com.zanoapps.profile.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profilePresentationModule = module {
    viewModelOf(::ProfileViewModel)
}
