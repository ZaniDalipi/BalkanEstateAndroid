package com.zanoapps.search.data.di

import com.zanoapps.search.data.repository.SavedSearchRepositoryImpl
import com.zanoapps.search.data.repository.SearchRepositoryImpl
import com.zanoapps.search.domain.repository.SavedSearchRepository
import com.zanoapps.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchDataModule = module {
    
    single<SearchRepository> {
        SearchRepositoryImpl(
            httpClient = get()
        )
    }
    
    single<SavedSearchRepository> {
        SavedSearchRepositoryImpl()
    }
}
