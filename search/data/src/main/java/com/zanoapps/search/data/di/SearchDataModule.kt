package com.zanoapps.search.data.di

import com.zanoapps.search.data.repository.AIChatRepositoryImpl
import com.zanoapps.search.data.repository.SavedSearchRepositoryImpl
import com.zanoapps.search.data.repository.SearchHistoryRepositoryImpl
import com.zanoapps.search.data.repository.SearchRepositoryImpl
import com.zanoapps.search.domain.repository.AIChatRepository
import com.zanoapps.search.domain.repository.SavedSearchRepository
import com.zanoapps.search.domain.repository.SearchHistoryRepository
import com.zanoapps.search.domain.repository.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
    singleOf(::SavedSearchRepositoryImpl).bind<SavedSearchRepository>()
    singleOf(::SearchHistoryRepositoryImpl).bind<SearchHistoryRepository>()
    singleOf(::AIChatRepositoryImpl).bind<AIChatRepository>()
}
