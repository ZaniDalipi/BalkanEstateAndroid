package com.zanoapps.property_details.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.property_details.domain.model.PropertyState
import com.zanoapps.property_details.domain.model.CreatePropertyForm

interface PropertyDetailRepository {
    suspend fun getPropertyDetail(id: String): Result<PropertyState, DataError.Network>
    suspend fun getSimilarProperties(propertyId: String): Result<List<PropertyState>, DataError.Network>
    suspend fun createListing(form: CreatePropertyForm): Result<String, DataError.Network>
    suspend fun contactAgent(propertyId: String, name: String, email: String, phone: String, message: String): EmptyResult<DataError.Network>
}
