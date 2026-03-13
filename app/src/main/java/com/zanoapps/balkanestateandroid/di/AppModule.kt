package com.zanoapps.balkanestateandroid.di

import com.zanoapps.balkanestateandroid.data.DataSeeder
import org.koin.dsl.module

val appModule = module {
    single { DataSeeder(get(), get(), get(), get(), get(), get()) }
}
