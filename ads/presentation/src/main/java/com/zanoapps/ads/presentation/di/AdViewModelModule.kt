package com.zanoapps.ads.presentation.di

import com.zanoapps.ads.presentation.ads.AdViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val adViewModelModule = module {
    viewModelOf(::AdViewModel)
}
