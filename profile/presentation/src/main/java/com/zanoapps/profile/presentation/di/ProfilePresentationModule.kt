package com.zanoapps.profile.presentation.di

import com.zanoapps.profile.presentation.edit_profile.EditProfileViewModel
import com.zanoapps.profile.presentation.profile.ProfileViewModel
import com.zanoapps.profile.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profilePresentationModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::SettingsViewModel)
}
