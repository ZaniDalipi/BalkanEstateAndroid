package com.zanoapps.notification.data.di

import com.zanoapps.notification.data.repository.NotificationRepositoryImpl
import com.zanoapps.notification.domain.repository.NotificationRepository
import org.koin.dsl.module

val notificationDataModule = module {
    single<NotificationRepository> { NotificationRepositoryImpl() }
}
