package com.zanoapps.notification.presentation.di

import com.zanoapps.notification.presentation.notifications.NotificationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationPresentationModule = module {
    viewModelOf(::NotificationsViewModel)
}
