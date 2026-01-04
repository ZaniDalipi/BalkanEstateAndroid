package com.zanoapps.core.data.di

import com.zanoapps.core.data.networking.HttpClientFactory
import com.zanoapps.core.data.remote.MessagingApiService
import com.zanoapps.core.data.remote.NotificationApiService
import com.zanoapps.core.data.remote.ProfileApiService
import com.zanoapps.core.data.remote.PropertyApiService
import com.zanoapps.core.data.repository.MessagingRepositorySSOTImpl
import com.zanoapps.core.data.repository.NotificationRepositorySSOTImpl
import com.zanoapps.core.data.repository.PropertyRepositoryImpl
import com.zanoapps.messaging.domain.repository.MessagingRepository
import com.zanoapps.notification.domain.repository.NotificationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    // HttpClient for network requests
    single { HttpClientFactory.create() }

    // API Services
    single { PropertyApiService(get()) }
    single { MessagingApiService(get()) }
    single { NotificationApiService(get()) }
    single { ProfileApiService(get()) }

    // Repositories with Single Source of Truth pattern
    single { PropertyRepositoryImpl(get(), get()) }
    singleOf(::MessagingRepositorySSOTImpl).bind<MessagingRepository>()
    singleOf(::NotificationRepositorySSOTImpl).bind<NotificationRepository>()
}
