package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.auth.data.di.authDataModule
import com.zanoapps.auth.presentation.di.authPresentationModule
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.favourites.data.di.favouritesDataModule
import com.zanoapps.favourites.presentation.di.favouritesPresentationModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.profile.data.di.profileDataModule
import com.zanoapps.profile.presentation.di.profilePresentationModule
import com.zanoapps.property_details.presentation.di.propertyDetailsPresentationModule
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
                // App module
                appModule,

                // Onboarding
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
                propertyDetailsPresentationModule
            )
        }
    }
}