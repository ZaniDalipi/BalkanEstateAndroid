package com.zanoapps.balkanestateandroid

import android.app.Application
import com.zanoapps.balkanestateandroid.di.appModule
import com.zanoapps.notification.data.di.notificationDataModule
import com.zanoapps.notification.data.push.NotificationChannelManager
import com.zanoapps.notification.presentation.di.notificationViewModelModule
import com.zanoapps.onboarding.presentation.di.onBoardingViewModelModule
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

        // Initialize notification channels
        NotificationChannelManager.createNotificationChannels(this)

        startKoin {
            androidLogger()
            androidContext(this@BalkanEstateApp)
            modules(
                onBoardingViewModelModule,
                appModule,
                notificationDataModule,
                notificationViewModelModule
            )
        }
    }
}