package com.zanoapps.search.presentation.di

import com.zanoapps.search.presentation.filter.FilterViewModel
import com.zanoapps.search.presentation.saved.SavedSearchesViewModel
import com.zanoapps.search.presentation.search.SearchPropertyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModelOf(::SearchPropertyViewModel)
    viewModelOf(::FilterViewModel)
    viewModelOf(::SavedSearchesViewModel)
}
