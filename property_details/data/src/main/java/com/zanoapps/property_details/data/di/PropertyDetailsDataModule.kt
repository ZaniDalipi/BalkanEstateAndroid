package com.zanoapps.property_details.data.di

import com.zanoapps.property_details.data.repository.PropertyDetailRepositoryImpl
import com.zanoapps.property_details.domain.repository.PropertyDetailRepository
import org.koin.dsl.module

val propertyDetailsDataModule = module {
    single<PropertyDetailRepository> { PropertyDetailRepositoryImpl(get()) }
}
