package com.zanoapps.agent.data.di

import com.zanoapps.agent.data.repository.AgentRepositoryImpl
import com.zanoapps.agent.domain.repository.AgentRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agentDataModule = module {
    singleOf(::AgentRepositoryImpl).bind<AgentRepository>()
}
