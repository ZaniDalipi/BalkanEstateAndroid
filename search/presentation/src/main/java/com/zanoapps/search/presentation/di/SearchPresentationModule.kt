package com.zanoapps.search.presentation.di

import com.zanoapps.search.presentation.search.SearchPropertyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModel { SearchPropertyViewModel() }
}
