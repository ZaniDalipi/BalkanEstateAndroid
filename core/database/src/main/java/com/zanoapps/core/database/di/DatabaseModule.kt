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
            "balkan_estate.db"
        ).fallbackToDestructiveMigration(false).build()
    }
    single { get<BalkanEstateDatabase>().propertyDao() }
    single { get<BalkanEstateDatabase>().favoriteDao() }
    single { get<BalkanEstateDatabase>().savedSearchDao() }
    single { get<BalkanEstateDatabase>().conversationDao() }
    single { get<BalkanEstateDatabase>().messageDao() }
    single { get<BalkanEstateDatabase>().userDao() }
    single { get<BalkanEstateDatabase>().agentDao() }
    single { get<BalkanEstateDatabase>().agencyDao() }
}
