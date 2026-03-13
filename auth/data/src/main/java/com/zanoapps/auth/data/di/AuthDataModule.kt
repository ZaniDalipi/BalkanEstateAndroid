package com.zanoapps.auth.data.di

import com.zanoapps.auth.data.repository.AuthRepositoryImpl
import com.zanoapps.auth.data.session.InMemorySessionStorage
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.auth.domain.repository.SessionStorage
import org.koin.dsl.module

val authDataModule = module {
    single<SessionStorage> { InMemorySessionStorage() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}
