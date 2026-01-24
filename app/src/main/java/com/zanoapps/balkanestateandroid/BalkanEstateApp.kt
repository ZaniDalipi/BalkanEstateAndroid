package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
import com.zanoapps.property_details.presentation.di.propertyDetailsModule
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
                onBoardingViewModelModule,
                propertyDetailsModule,
                appModule
            )
        }
    }
}