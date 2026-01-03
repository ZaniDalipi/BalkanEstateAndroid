package com.zanoapps.messaging.data.di

import com.zanoapps.messaging.data.repository.MessagingRepositoryImpl
import com.zanoapps.messaging.domain.repository.MessagingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val messagingDataModule = module {
    singleOf(::MessagingRepositoryImpl).bind<MessagingRepository>()
}
