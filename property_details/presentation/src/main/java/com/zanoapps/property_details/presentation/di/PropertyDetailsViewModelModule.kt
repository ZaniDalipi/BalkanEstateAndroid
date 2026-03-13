package com.zanoapps.property_details.presentation.di

import com.zanoapps.property_details.presentation.create.CreateListingViewModel
import com.zanoapps.property_details.presentation.detail.PropertyDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val propertyDetailsViewModelModule = module {
    viewModelOf(::PropertyDetailViewModel)
    viewModelOf(::CreateListingViewModel)
}
