package com.zanoapps.messaging.presentation.di

import com.zanoapps.messaging.presentation.chat.ChatViewModel
import com.zanoapps.messaging.presentation.conversations.ConversationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val messagingPresentationModule = module {
    viewModelOf(::ConversationsViewModel)
    viewModelOf(::ChatViewModel)
}
