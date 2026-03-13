package com.zanoapps.search.data.di

import com.zanoapps.search.data.repository.PropertyRepositoryImpl
import com.zanoapps.search.data.repository.SavedSearchRepositoryImpl
import com.zanoapps.search.domain.repository.PropertyRepository
import com.zanoapps.search.domain.repository.SavedSearchRepository
import org.koin.dsl.module

val searchDataModule = module {
    single<PropertyRepository> { PropertyRepositoryImpl(get()) }
    single<SavedSearchRepository> { SavedSearchRepositoryImpl(get()) }
}
