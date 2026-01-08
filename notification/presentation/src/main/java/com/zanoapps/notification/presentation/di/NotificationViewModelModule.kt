package com.zanoapps.notification.presentation.di

import com.zanoapps.notification.presentation.NotificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for notification presentation layer dependencies.
 */
val notificationViewModelModule = module {
    viewModelOf(::NotificationViewModel)
}
