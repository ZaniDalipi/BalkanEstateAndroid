package com.zanoapps.notification.data.di

import androidx.room.Room
import com.zanoapps.notification.data.local.database.NotificationDatabase
import com.zanoapps.notification.data.local.preferences.NotificationPreferencesDataStore
import com.zanoapps.notification.data.repository.NotificationRepositoryImpl
import com.zanoapps.notification.domain.repository.NotificationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for notification data layer dependencies.
 */
val notificationDataModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            NotificationDatabase::class.java,
            NotificationDatabase.DATABASE_NAME
        ).build()
    }

    // DAO
    single { get<NotificationDatabase>().notificationDao }

    // Preferences DataStore
    single { NotificationPreferencesDataStore(androidContext()) }

    // Repository
    single<NotificationRepository> {
        NotificationRepositoryImpl(
            notificationDao = get(),
            preferencesDataStore = get()
        )
    }
}
