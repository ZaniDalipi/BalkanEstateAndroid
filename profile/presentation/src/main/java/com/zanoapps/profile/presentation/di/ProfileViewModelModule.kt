package com.zanoapps.profile.presentation.di

import com.zanoapps.profile.presentation.profile.ProfileViewModel
import com.zanoapps.profile.presentation.subscription.SubscriptionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileViewModelModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SubscriptionViewModel)
}
