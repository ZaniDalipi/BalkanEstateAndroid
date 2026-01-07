package com.zanoapps.property_details.presentation.listing_details.di

import com.zanoapps.property_details.presentation.listing_details.ListingDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val listingDetailsModule = module {
    viewModelOf(::ListingDetailsViewModel)
}
