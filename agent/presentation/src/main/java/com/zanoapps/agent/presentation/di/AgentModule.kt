package com.zanoapps.agent.presentation.di

import com.zanoapps.agent.presentation.agents.AgentsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agentModule = module {
    viewModelOf(::AgentsViewModel)
}
