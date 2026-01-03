package com.zanoapps.profile.data.di

import com.zanoapps.profile.data.repository.ProfileRepositoryImpl
import com.zanoapps.profile.data.repository.SettingsRepositoryImpl
import com.zanoapps.profile.domain.repository.ProfileRepository
import com.zanoapps.profile.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDataModule = module {
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()
    single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }
}
