package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.agent.presentation.di.agentModule
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.favourites.presentation.di.favouritesModule
import com.zanoapps.messaging.presentation.di.messagingModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.profile.presentation.di.profileModule
import com.zanoapps.property_details.presentation.di.propertyDetailsModule
import com.zanoapps.search.presentation.di.searchModule
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
                searchModule,
                propertyDetailsModule,
                favouritesModule,
                profileModule,
                messagingModule,
                agentModule
            )
        }
    }
}
