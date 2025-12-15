package com.zanoapps.search.data.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.BoundingBox
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchRepositoryImpl(
    private val httpClient: HttpClient
) : SearchRepository {
    
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    
    override suspend fun searchProperties(
        filters: SearchFilters
    ): Result<List<BalkanEstateProperty>, DataError.Network> {
        return try {
            // Simulate network delay
            delay(500)
            
            // For now, return mock data filtered by the query
            val properties = MockData.getMockProperties().filter { property ->
                if (filters.query.isNotBlank()) {
                    property.title.contains(filters.query, ignoreCase = true) ||
                            property.city.contains(filters.query, ignoreCase = true) ||
                            property.address.contains(filters.query, ignoreCase = true)
                } else {
                    true
                }
            }.filter { property ->
                applyFilters(property, filters)
            }
            
            Result.Success(properties)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override suspend fun searchPropertiesInBounds(
        boundingBox: BoundingBox,
        filters: SearchFilters
    ): Result<List<BalkanEstateProperty>, DataError.Network> {
        return try {
            delay(300)
            
            val properties = MockData.getMockProperties().filter { property ->
                property.latitude >= boundingBox.southWestLat &&
                        property.latitude <= boundingBox.northEastLat &&
                        property.longitude >= boundingBox.southWestLng &&
                        property.longitude <= boundingBox.northEastLng
            }.filter { property ->
                applyFilters(property, filters)
            }
            
            Result.Success(properties)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override suspend fun getPropertyById(
        propertyId: String
    ): Result<BalkanEstateProperty, DataError.Network> {
        return try {
            delay(200)
            
            val property = MockData.getMockProperties().find { it.id == propertyId }
            if (property != null) {
                Result.Success(property)
            } else {
                Result.Error(DataError.Network.NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override fun getFavoritePropertyIds(userId: String): Flow<Set<String>> {
        return _favoriteIds.asStateFlow()
    }
    
    override suspend fun toggleFavorite(
        userId: String,
        propertyId: String
    ): Result<Boolean, DataError.Network> {
        return try {
            val currentFavorites = _favoriteIds.value.toMutableSet()
            val isFavorite = if (currentFavorites.contains(propertyId)) {
                currentFavorites.remove(propertyId)
                false
            } else {
                currentFavorites.add(propertyId)
                true
            }
            _favoriteIds.value = currentFavorites
            Result.Success(isFavorite)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override suspend fun subscribeToNewProperties(
        email: String
    ): Result<Unit, DataError.Network> {
        return try {
            delay(500)
            // TODO: Implement actual API call
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    private fun applyFilters(
        property: BalkanEstateProperty,
        filters: SearchFilters
    ): Boolean {
        // Price filter
        if (filters.minPrice != null && property.price < filters.minPrice) return false
        if (filters.maxPrice != null && property.price > filters.maxPrice) return false
        
        // Bedrooms filter
        if (filters.bedrooms != null && property.bedrooms < filters.bedrooms) return false
        
        // Bathrooms filter
        if (filters.bathrooms != null && property.bathrooms < filters.bathrooms) return false
        
        // Square footage filter
        if (filters.minSquareFootage != null && property.squareFootage < filters.minSquareFootage) return false
        if (filters.maxSquareFootage != null && property.squareFootage > filters.maxSquareFootage) return false
        
        // Property type filter
        if (filters.propertyTypes.isNotEmpty()) {
            val propertyTypeMatch = filters.propertyTypes.any { 
                it.displayName.equals(property.propertyType, ignoreCase = true) 
            }
            if (!propertyTypeMatch) return false
        }
        
        // Listing type filter
        if (filters.listingTypes.isNotEmpty()) {
            val listingTypeMatch = filters.listingTypes.any { 
                it.displayName.equals(property.listingType, ignoreCase = true) 
            }
            if (!listingTypeMatch) return false
        }
        
        return true
    }
}
