package com.zanoapps.search.data.repository

import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PropertyRepositoryImpl(
    private val propertyDao: PropertyDao
) : PropertyRepository {

    override fun getProperties(): Flow<List<BalkanEstateProperty>> {
        return propertyDao.getAllProperties().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun searchProperties(
        query: String,
        filters: SearchFilters
    ): Result<List<BalkanEstateProperty>, DataError.Network> {
        return try {
            val results = if (query.isBlank()) {
                propertyDao.searchProperties("")
            } else {
                propertyDao.searchProperties(query)
            }
            var filtered = results.map { it.toDomain() }

            // Apply filters
            if (filters.minPrice != null) {
                filtered = filtered.filter { it.price >= filters.minPrice }
            }
            if (filters.maxPrice != null) {
                filtered = filtered.filter { it.price <= filters.maxPrice }
            }
            if (filters.bedrooms != null) {
                filtered = filtered.filter { it.bedrooms >= filters.bedrooms }
            }
            if (filters.bathrooms != null) {
                filtered = filtered.filter { it.bathrooms >= filters.bathrooms }
            }
            if (filters.propertyTypes.isNotEmpty()) {
                filtered = filtered.filter { prop ->
                    filters.propertyTypes.any { it.displayName.equals(prop.propertyType, ignoreCase = true) }
                }
            }
            if (filters.listingTypes.isNotEmpty()) {
                filtered = filtered.filter { prop ->
                    filters.listingTypes.any { it.displayName.equals(prop.listingType, ignoreCase = true) }
                }
            }
            Result.Success(filtered)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getPropertyById(id: String): Result<BalkanEstateProperty, DataError.Network> {
        val entity = propertyDao.getPropertyById(id)
        return if (entity != null) {
            Result.Success(entity.toDomain())
        } else {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getFeaturedProperties(): Result<List<BalkanEstateProperty>, DataError.Network> {
        return try {
            val featured = propertyDao.getFeaturedProperties().map { it.toDomain() }
            Result.Success(featured)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

fun PropertyEntity.toDomain(): BalkanEstateProperty {
    return BalkanEstateProperty(
        id = id,
        title = title,
        price = price,
        currency = currency,
        imageUrl = imageUrl,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        squareFootage = squareFootage,
        address = address,
        city = city,
        country = country,
        latitude = latitude,
        longitude = longitude,
        propertyType = propertyType,
        listingType = listingType,
        agentName = agentName,
        isFeatured = isFeatured,
        isUrgent = isUrgent,
        description = description,
        yearBuilt = yearBuilt,
        floorNumber = floorNumber,
        totalFloors = totalFloors,
        furnished = furnished,
        parking = parking,
        agentPhone = agentPhone,
        agentEmail = agentEmail,
        agentAvatarUrl = agentAvatarUrl,
        agentId = agentId
    )
}

fun BalkanEstateProperty.toEntity(): PropertyEntity {
    return PropertyEntity(
        id = id,
        title = title,
        price = price,
        currency = currency,
        imageUrl = imageUrl,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        squareFootage = squareFootage,
        address = address,
        city = city,
        country = country,
        latitude = latitude,
        longitude = longitude,
        propertyType = propertyType,
        listingType = listingType,
        agentName = agentName,
        isFeatured = isFeatured,
        isUrgent = isUrgent,
        description = description,
        yearBuilt = yearBuilt,
        floorNumber = floorNumber,
        totalFloors = totalFloors,
        furnished = furnished,
        parking = parking,
        agentPhone = agentPhone,
        agentEmail = agentEmail,
        agentAvatarUrl = agentAvatarUrl,
        agentId = agentId
    )
}
