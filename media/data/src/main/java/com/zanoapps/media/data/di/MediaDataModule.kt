package com.zanoapps.media.data.di

import com.zanoapps.media.data.repository.MediaRepositoryImpl
import com.zanoapps.media.domain.repository.MediaRepository
import org.koin.dsl.module

val mediaDataModule = module {
    single<MediaRepository> { MediaRepositoryImpl() }
}
