package com.zanoapps.notification.presentation.di

import com.zanoapps.notification.presentation.notifications.NotificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationViewModelModule = module {
    viewModelOf(::NotificationViewModel)
}
