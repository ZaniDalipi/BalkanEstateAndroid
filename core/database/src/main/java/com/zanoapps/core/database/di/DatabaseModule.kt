package com.zanoapps.core.database.di

import androidx.room.Room
import com.zanoapps.core.database.BalkanEstateDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BalkanEstateDatabase::class.java,
            BalkanEstateDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<BalkanEstateDatabase>().propertyDao() }
    single { get<BalkanEstateDatabase>().userDao() }
    single { get<BalkanEstateDatabase>().favoritePropertyDao() }
    single { get<BalkanEstateDatabase>().conversationDao() }
    single { get<BalkanEstateDatabase>().messageDao() }
    single { get<BalkanEstateDatabase>().notificationDao() }
    single { get<BalkanEstateDatabase>().onboardingPreferencesDao() }
}
