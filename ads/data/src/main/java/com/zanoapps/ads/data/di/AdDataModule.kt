package com.zanoapps.ads.data.di

import com.zanoapps.ads.data.repository.AdRepositoryImpl
import com.zanoapps.ads.domain.repository.AdRepository
import org.koin.dsl.module

val adDataModule = module {
    single<AdRepository> { AdRepositoryImpl() }
}
