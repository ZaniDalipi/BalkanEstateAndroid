package com.zanoapps.favourites.presentation.di

import com.zanoapps.favourites.presentation.FavouritesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favouritesModule = module {
    viewModelOf(::FavouritesViewModel)
}
