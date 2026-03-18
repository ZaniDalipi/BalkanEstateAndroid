package com.zanoapps.balkanestateandroid.di

import com.zanoapps.balkanestateandroid.data.DataSeeder
import com.zanoapps.balkanestateandroid.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { DataSeeder(get(), get(), get(), get(), get(), get()) }
    viewModelOf(::HomeViewModel)
}
