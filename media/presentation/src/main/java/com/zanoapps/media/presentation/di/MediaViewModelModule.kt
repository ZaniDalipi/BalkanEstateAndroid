package com.zanoapps.media.presentation.di

import com.zanoapps.media.presentation.gallery.MediaGalleryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mediaViewModelModule = module {
    viewModelOf(::MediaGalleryViewModel)
}
