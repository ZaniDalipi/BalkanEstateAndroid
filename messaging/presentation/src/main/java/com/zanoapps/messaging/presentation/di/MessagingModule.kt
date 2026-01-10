package com.zanoapps.messaging.presentation.di

import com.zanoapps.messaging.presentation.inbox.InboxViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val messagingModule = module {
    viewModelOf(::InboxViewModel)
}
