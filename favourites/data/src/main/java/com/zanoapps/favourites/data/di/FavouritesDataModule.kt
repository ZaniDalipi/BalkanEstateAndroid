package com.zanoapps.favourites.data.di

import com.zanoapps.favourites.data.repository.FavouritesRepositoryImpl
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val favouritesDataModule = module {
    singleOf(::FavouritesRepositoryImpl).bind<FavouritesRepository>()
}
