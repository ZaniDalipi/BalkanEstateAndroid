package com.zanoapps.profile.data.di

import com.zanoapps.profile.data.repository.ProfileRepositoryImpl
import com.zanoapps.profile.domain.repository.ProfileRepository
import org.koin.dsl.module

val profileDataModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}
