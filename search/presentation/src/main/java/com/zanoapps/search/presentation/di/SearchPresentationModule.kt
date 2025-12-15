package com.zanoapps.search.presentation.di

import com.zanoapps.search.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    
    viewModel { (userId: String?) ->
        SearchViewModel(
            searchRepository = get(),
            savedSearchRepository = get(),
            userId = userId
        )
    }
}
