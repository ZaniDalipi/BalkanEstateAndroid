package com.zanoapps.search.data.di

import com.zanoapps.search.data.repository.SearchRepositoryImpl
import com.zanoapps.search.domain.repository.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
}
