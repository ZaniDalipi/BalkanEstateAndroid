package com.zanoapps.messaging.data.di

import com.zanoapps.messaging.data.repository.MessagingRepositoryImpl
import com.zanoapps.messaging.domain.repository.MessagingRepository
import org.koin.dsl.module

val messagingDataModule = module {
    single<MessagingRepository> { MessagingRepositoryImpl(get(), get()) }
}
