package com.zanoapps.agent.presentation.di

import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.agent.presentation.listings.AgentListingsViewModel
import com.zanoapps.agent.presentation.profile.AgentProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val agentPresentationModule = module {
    viewModel { AgentListingsViewModel(get()) }
    viewModel { (agentId: String) -> AgentProfileViewModel(
        agentRepository = agentId,
        get(),
    ) }
}
