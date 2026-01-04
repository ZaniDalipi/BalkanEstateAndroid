package com.zanoapps.media.data.di

import com.zanoapps.media.data.repository.MediaRepositoryImpl
import com.zanoapps.media.domain.repository.MediaRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mediaDataModule = module {
    singleOf(::MediaRepositoryImpl).bind<MediaRepository>()
}
