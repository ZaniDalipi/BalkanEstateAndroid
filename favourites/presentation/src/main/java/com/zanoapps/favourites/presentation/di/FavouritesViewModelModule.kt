package com.zanoapps.favourites.presentation.di

import com.zanoapps.favourites.presentation.compare.PropertyComparisonViewModel
import com.zanoapps.favourites.presentation.favourites.FavouritesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favouritesViewModelModule = module {
    viewModelOf(::FavouritesViewModel)
    viewModelOf(::PropertyComparisonViewModel)
}
