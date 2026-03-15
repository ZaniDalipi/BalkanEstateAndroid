package com.zanoapps.map.data.di

import com.zanoapps.map.data.repository.MapRepositoryImpl
import com.zanoapps.map.domain.repository.MapRepository
import org.koin.dsl.module

val mapDataModule = module {
    single<MapRepository> { MapRepositoryImpl() }
}
