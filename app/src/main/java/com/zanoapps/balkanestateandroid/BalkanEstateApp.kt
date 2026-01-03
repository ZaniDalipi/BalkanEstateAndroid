package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.auth.data.di.authDataModule
import com.zanoapps.auth.presentation.di.authPresentationModule
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.core.data.di.coreDataModule
import com.zanoapps.core.database.di.databaseModule
import com.zanoapps.favourites.data.di.favouritesDataModule
import com.zanoapps.favourites.presentation.di.favouritesPresentationModule
import com.zanoapps.onboarding.data.di.onboardingDataModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.profile.data.di.profileDataModule
import com.zanoapps.profile.presentation.di.profilePresentationModule
import com.zanoapps.property_details.presentation.di.propertyDetailsPresentationModule
import com.zanoapps.map.presentation.di.mapPresentationModule
import com.zanoapps.messaging.presentation.di.messagingPresentationModule
import com.zanoapps.notification.presentation.di.notificationPresentationModule
import com.zanoapps.search.data.di.searchDataModule
import com.zanoapps.search.presentation.di.searchPresentationModule
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
                // Core - Database (Room) and Network (Single Source of Truth)
                databaseModule,
                coreDataModule,

                // App module
                appModule,

                // Onboarding
                onboardingDataModule,
                onBoardingViewModelModule,

                // Auth
                authDataModule,
                authPresentationModule,

                // Profile
                profileDataModule,
                profilePresentationModule,

                // Favourites
                favouritesDataModule,
                favouritesPresentationModule,

                // Property Details
                propertyDetailsPresentationModule,

                // Map
                mapPresentationModule,

                // Messaging (uses core data SSOT repository)
                messagingPresentationModule,

                // Notifications (uses core data SSOT repository)
                notificationPresentationModule,

                // Search (uses core data SSOT repository for properties)
                searchDataModule,
                searchPresentationModule
            )
        }
    }
}