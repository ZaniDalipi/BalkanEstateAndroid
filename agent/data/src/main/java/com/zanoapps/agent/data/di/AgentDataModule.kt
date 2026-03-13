package com.zanoapps.agent.data.di

import com.zanoapps.agent.data.repository.AgentRepositoryImpl
import com.zanoapps.agent.domain.repository.AgentRepository
import org.koin.dsl.module

val agentDataModule = module {
    single<AgentRepository> { AgentRepositoryImpl(get(), get()) }
}
