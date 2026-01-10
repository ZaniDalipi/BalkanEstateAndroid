package com.zanoapps.property_details.presentation.di

import com.zanoapps.property_details.presentation.create.CreateListingViewModel
import com.zanoapps.property_details.presentation.details.PropertyDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val propertyDetailsModule = module {
    viewModelOf(::PropertyDetailsViewModel)
    viewModelOf(::CreateListingViewModel)
}
