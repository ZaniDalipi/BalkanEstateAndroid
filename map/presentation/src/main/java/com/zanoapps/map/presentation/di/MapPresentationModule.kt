package com.zanoapps.map.presentation.di

import com.zanoapps.map.presentation.map.MapViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapPresentationModule = module {
    viewModelOf(::MapViewModel)
}
