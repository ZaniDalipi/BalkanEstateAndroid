package com.zanoapps.media.presentation.di

import com.zanoapps.media.presentation.gallery.ImageGalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaPresentationModule = module {
    viewModel { ImageGalleryViewModel(get(), get()) }
}
