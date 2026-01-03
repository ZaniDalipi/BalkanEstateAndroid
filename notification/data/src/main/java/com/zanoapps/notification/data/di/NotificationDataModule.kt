package com.zanoapps.notification.data.di

import com.zanoapps.notification.data.repository.NotificationRepositoryImpl
import com.zanoapps.notification.domain.repository.NotificationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notificationDataModule = module {
    singleOf(::NotificationRepositoryImpl).bind<NotificationRepository>()
}
