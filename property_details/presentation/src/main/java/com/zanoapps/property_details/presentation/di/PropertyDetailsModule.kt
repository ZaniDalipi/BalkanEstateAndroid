package com.zanoapps.property_details.presentation.di

import com.zanoapps.property_details.presentation.PropertyDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val propertyDetailsModule = module {
    viewModelOf(::PropertyDetailsViewModel)
}
