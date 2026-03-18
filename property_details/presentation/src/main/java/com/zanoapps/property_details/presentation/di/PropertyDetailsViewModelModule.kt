package com.zanoapps.property_details.presentation.di

import com.zanoapps.property_details.presentation.calculator.MortgageCalculatorViewModel
import com.zanoapps.property_details.presentation.create.CreateListingViewModel
import com.zanoapps.property_details.presentation.detail.PropertyDetailViewModel
import com.zanoapps.property_details.presentation.listings.MyListingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val propertyDetailsViewModelModule = module {
    viewModelOf(::PropertyDetailViewModel)
    viewModelOf(::CreateListingViewModel)
    viewModelOf(::MyListingsViewModel)
    viewModelOf(::MortgageCalculatorViewModel)
}
