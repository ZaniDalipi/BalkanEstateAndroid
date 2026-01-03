package com.zanoapps.property_details.presentation.di

import com.zanoapps.property_details.presentation.property_detail.PropertyDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val propertyDetailsPresentationModule = module {
    viewModelOf(::PropertyDetailViewModel)
}
