package com.zanoapps.shared.di

import com.zanoapps.shared.data.network.ApiClient
import com.zanoapps.shared.data.network.createHttpClient
import com.zanoapps.shared.data.repository.AuthRepositoryImpl
import com.zanoapps.shared.data.repository.MessageRepositoryImpl
import com.zanoapps.shared.data.repository.PropertyRepositoryImpl
import com.zanoapps.shared.domain.repository.AuthRepository
import com.zanoapps.shared.domain.repository.MessageRepository
import com.zanoapps.shared.domain.repository.PropertyRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Shared Koin module providing platform-agnostic dependencies
 */
val sharedModule: Module = module {
    // Network
    single { createHttpClient() }
    single { ApiClient(get()) }

    // Repositories
    single<PropertyRepository> { PropertyRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<MessageRepository> { MessageRepositoryImpl() }
}

/**
 * Initialize Koin with shared module
 * Call this from platform-specific entry points
 */
fun initKoin(): org.koin.core.KoinApplication {
    return org.koin.core.context.startKoin {
        modules(sharedModule)
    }
}
