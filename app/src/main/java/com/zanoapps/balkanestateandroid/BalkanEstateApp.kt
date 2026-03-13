package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.agent.data.di.agentDataModule
import com.zanoapps.agent.presentation.di.agentViewModelModule
import com.zanoapps.auth.data.di.authDataModule
import com.zanoapps.auth.presentation.di.authViewModelModule
import com.zanoapps.balkanestateandroid.data.DataSeeder
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.core.database.di.databaseModule
import com.zanoapps.favourites.data.di.favouritesDataModule
import com.zanoapps.favourites.presentation.di.favouritesViewModelModule
import com.zanoapps.messaging.data.di.messagingDataModule
import com.zanoapps.messaging.presentation.di.messagingViewModelModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.profile.data.di.profileDataModule
import com.zanoapps.profile.presentation.di.profileViewModelModule
import com.zanoapps.property_details.data.di.propertyDetailsDataModule
import com.zanoapps.property_details.presentation.di.propertyDetailsViewModelModule
import com.zanoapps.search.data.di.searchDataModule
import com.zanoapps.search.presentation.di.searchViewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BalkanEstateApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@BalkanEstateApp)
            modules(
                // Core
                databaseModule,
                appModule,
                // Data
                searchDataModule,
                agentDataModule,
                profileDataModule,
                messagingDataModule,
                favouritesDataModule,
                propertyDetailsDataModule,
                authDataModule,
                // Presentation
                onBoardingViewModelModule,
                searchViewModelModule,
                agentViewModelModule,
                profileViewModelModule,
                messagingViewModelModule,
                favouritesViewModelModule,
                propertyDetailsViewModelModule,
                authViewModelModule
            )
        }

        // Seed database with initial data on first launch
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val seeder: DataSeeder = get()
                seeder.seedIfEmpty()
            } catch (e: Exception) {
                Timber.e(e, "Failed to seed database")
            }
        }
    }
}
