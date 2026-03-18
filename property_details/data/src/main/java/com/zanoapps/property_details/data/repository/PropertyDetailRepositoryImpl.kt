package com.zanoapps.property_details.data.repository

import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.property_details.domain.model.CreatePropertyForm
import com.zanoapps.property_details.domain.model.PropertyState
import com.zanoapps.property_details.domain.repository.PropertyDetailRepository
import java.util.UUID

class PropertyDetailRepositoryImpl(
    private val propertyDao: PropertyDao
) : PropertyDetailRepository {

    override suspend fun getPropertyDetail(id: String): Result<PropertyState, DataError.Network> {
        val entity = propertyDao.getPropertyById(id)
        return if (entity != null) {
            Result.Success(
                PropertyState(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    price = entity.price,
                    currency = entity.currency,
                    propertyType = entity.propertyType,
                    listingType = entity.listingType,
                    address = entity.address,
                    city = entity.city,
                    country = entity.country,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    bedrooms = entity.bedrooms,
                    bathrooms = entity.bathrooms,
                    squareFootage = entity.squareFootage,
                    yearBuilt = entity.yearBuilt,
                    floorNumber = entity.floorNumber,
                    totalFloors = entity.totalFloors,
                    furnished = entity.furnished,
                    parking = entity.parking,
                    agentName = entity.agentName,
                    agentPhone = entity.agentPhone,
                    agentEmail = entity.agentEmail
                )
            )
        } else {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getSimilarProperties(propertyId: String): Result<List<PropertyState>, DataError.Network> {
        val property = propertyDao.getPropertyById(propertyId)
        return if (property != null) {
            val similar = propertyDao.getSimilarProperties(property.city, propertyId)
            Result.Success(similar.map { entity ->
                PropertyState(
                    id = entity.id, title = entity.title,
                    price = entity.price, currency = entity.currency,
                    propertyType = entity.propertyType, listingType = entity.listingType,
                    city = entity.city, country = entity.country,
                    bedrooms = entity.bedrooms, bathrooms = entity.bathrooms,
                    squareFootage = entity.squareFootage, address = entity.address,
                    latitude = entity.latitude, longitude = entity.longitude,
                    agentName = entity.agentName
                )
            })
        } else {
            Result.Success(emptyList())
        }
    }

    override suspend fun createListing(form: CreatePropertyForm): Result<String, DataError.Network> {
        return try {
            val id = UUID.randomUUID().toString()
            val entity = com.zanoapps.core.database.entity.PropertyEntity(
                id = id,
                title = form.title,
                description = form.description,
                price = form.price.toDoubleOrNull() ?: 0.0,
                currency = form.currency,
                imageUrl = "",
                bedrooms = form.bedrooms,
                bathrooms = form.bathrooms,
                squareFootage = form.squareFootage.toIntOrNull() ?: 0,
                address = form.address,
                city = form.city,
                country = form.country,
                latitude = 0.0,
                longitude = 0.0,
                propertyType = form.propertyType?.name ?: "",
                listingType = form.listingType?.name ?: "",
                agentName = form.agentName,
                agentPhone = form.agentPhone,
                agentEmail = form.agentEmail,
                yearBuilt = form.yearBuilt.toIntOrNull() ?: 0,
                furnished = form.furnished.name,
                parking = form.parkingType?.name ?: "",
                isFeatured = form.featured,
                isUrgent = form.urgent
            )
            propertyDao.insert(entity)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun contactAgent(
        propertyId: String, name: String, email: String,
        phone: String, message: String
    ): EmptyResult<DataError.Network> {
        // In a real app, this would POST to an API
        return Result.Success(Unit)
    }
}
