package com.zanoapps.agent.presentation.di

import com.zanoapps.agent.presentation.listings.AgentListingsViewModel
import com.zanoapps.agent.presentation.profile.AgentProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agentPresentationModule = module {
    viewModelOf(::AgentListingsViewModel)
    viewModelOf(::AgentProfileViewModel)
}
