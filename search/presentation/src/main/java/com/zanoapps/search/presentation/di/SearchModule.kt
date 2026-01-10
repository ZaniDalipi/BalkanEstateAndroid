package com.zanoapps.search.presentation.di

import com.zanoapps.search.presentation.filters.FiltersViewModel
import com.zanoapps.search.presentation.savedsearches.SavedSearchesViewModel
import com.zanoapps.search.presentation.search.SearchPropertyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchPropertyViewModel)
    viewModelOf(::SavedSearchesViewModel)
    viewModelOf(::FiltersViewModel)
}
