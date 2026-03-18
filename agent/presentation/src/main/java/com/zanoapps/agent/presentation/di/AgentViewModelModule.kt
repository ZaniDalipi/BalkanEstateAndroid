package com.zanoapps.agent.presentation.di

import com.zanoapps.agent.presentation.agencies.AgenciesViewModel
import com.zanoapps.agent.presentation.agents.AgentsViewModel
import com.zanoapps.agent.presentation.detail.AgentDetailViewModel
import com.zanoapps.agent.presentation.detail.AgencyDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agentViewModelModule = module {
    viewModelOf(::AgentsViewModel)
    viewModelOf(::AgenciesViewModel)
    viewModelOf(::AgentDetailViewModel)
    viewModelOf(::AgencyDetailViewModel)
}
