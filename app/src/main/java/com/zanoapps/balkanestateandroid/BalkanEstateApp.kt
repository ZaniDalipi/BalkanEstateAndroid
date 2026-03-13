package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.agent.presentation.di.agentViewModelModule
import com.zanoapps.auth.presentation.di.authViewModelModule
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.favourites.presentation.di.favouritesViewModelModule
import com.zanoapps.messaging.presentation.di.messagingViewModelModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.profile.presentation.di.profileViewModelModule
import com.zanoapps.property_details.presentation.di.propertyDetailsViewModelModule
import com.zanoapps.search.presentation.di.searchViewModelModule
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
                appModule,
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
    }
}
