package com.zanoapps.favourites.data.di

import com.zanoapps.favourites.data.repository.FavouritesRepositoryImpl
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import org.koin.dsl.module

val favouritesDataModule = module {
    single<FavouritesRepository> { FavouritesRepositoryImpl(get(), get()) }
}
