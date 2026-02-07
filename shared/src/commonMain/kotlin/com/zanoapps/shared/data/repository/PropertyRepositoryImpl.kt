package com.zanoapps.shared.data.repository

import com.zanoapps.shared.data.network.ApiClient
import com.zanoapps.shared.domain.model.Property
import com.zanoapps.shared.domain.model.PropertyFilter
import com.zanoapps.shared.domain.model.PropertyType
import com.zanoapps.shared.domain.repository.PropertyRepository
import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PropertyRepositoryImpl(
    private val apiClient: ApiClient
) : PropertyRepository {

    private val propertiesCache = MutableStateFlow<List<Property>>(emptyList())
    private val favoritesCache = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun getProperties(filter: PropertyFilter): Result<List<Property>, DataError> {
        return try {
            // For now, return mock data - replace with actual API call
            val properties = getMockProperties().filter { property ->
                val matchesType = filter.propertyType == null || property.propertyType == filter.propertyType
                val matchesCountry = filter.country == null || property.country.equals(filter.country, ignoreCase = true)
                val matchesCity = filter.city == null || property.city.equals(filter.city, ignoreCase = true)
                val matchesPriceMin = filter.minPrice == null || property.price >= filter.minPrice!!
                val matchesPriceMax = filter.maxPrice == null || property.price <= filter.maxPrice!!
                val matchesBedrooms = filter.bedrooms == null || property.bedrooms >= filter.bedrooms!!
                val matchesBathrooms = filter.bathrooms == null || property.bathrooms >= filter.bathrooms!!

                matchesType && matchesCountry && matchesCity && matchesPriceMin &&
                matchesPriceMax && matchesBedrooms && matchesBathrooms
            }
            propertiesCache.value = properties
            Result.Success(properties)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getPropertyById(id: String): Result<Property, DataError> {
        return try {
            val property = getMockProperties().find { it.id == id }
            if (property != null) {
                Result.Success(property)
            } else {
                Result.Error(DataError.Network.NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getFeaturedProperties(): Result<List<Property>, DataError> {
        return try {
            val featured = getMockProperties().filter { it.isFeatured }
            Result.Success(featured)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getNewProperties(): Result<List<Property>, DataError> {
        return try {
            val newProperties = getMockProperties().filter { it.isNew }
            Result.Success(newProperties)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun searchProperties(query: String): Result<List<Property>, DataError> {
        return try {
            val results = getMockProperties().filter { property ->
                property.title.contains(query, ignoreCase = true) ||
                property.description.contains(query, ignoreCase = true) ||
                property.city.contains(query, ignoreCase = true) ||
                property.country.contains(query, ignoreCase = true)
            }
            Result.Success(results)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override fun observeProperties(): Flow<List<Property>> {
        return propertiesCache
    }

    override suspend fun toggleFavorite(propertyId: String): Result<Boolean, DataError> {
        return try {
            val currentFavorites = favoritesCache.value.toMutableSet()
            val isFavorite = if (currentFavorites.contains(propertyId)) {
                currentFavorites.remove(propertyId)
                false
            } else {
                currentFavorites.add(propertyId)
                true
            }
            favoritesCache.value = currentFavorites
            Result.Success(isFavorite)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun getFavoriteProperties(): Result<List<Property>, DataError> {
        return try {
            val favoriteIds = favoritesCache.value
            val favorites = getMockProperties().filter { it.id in favoriteIds }
            Result.Success(favorites)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override fun observeFavorites(): Flow<Set<String>> {
        return favoritesCache
    }

    private fun getMockProperties(): List<Property> {
        return listOf(
            Property(
                id = "1",
                title = "Modern Apartment in Tirana Center",
                description = "Beautiful modern apartment with stunning city views. Fully renovated with high-end finishes.",
                price = 150000.0,
                currency = "EUR",
                propertyType = PropertyType.APARTMENT,
                bedrooms = 3,
                bathrooms = 2,
                area = 120.0,
                areaUnit = "sqm",
                address = "Rruga Myslym Shyri",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3275,
                longitude = 19.8187,
                images = listOf("https://example.com/image1.jpg"),
                amenities = listOf("Parking", "Elevator", "Balcony", "Central Heating"),
                isFeatured = true,
                isNew = true,
                agentId = "agent1",
                agentName = "John Doe",
                agentPhone = "+355 69 123 4567",
                createdAt = "2024-01-15",
                updatedAt = "2024-01-15"
            ),
            Property(
                id = "2",
                title = "Luxury Villa with Sea View",
                description = "Stunning villa overlooking the Adriatic Sea. Private pool and garden.",
                price = 450000.0,
                currency = "EUR",
                propertyType = PropertyType.VILLA,
                bedrooms = 5,
                bathrooms = 4,
                area = 350.0,
                areaUnit = "sqm",
                address = "Rruga e Plazhit",
                city = "Durrës",
                country = "Albania",
                latitude = 41.3246,
                longitude = 19.4565,
                images = listOf("https://example.com/image2.jpg"),
                amenities = listOf("Pool", "Garden", "Sea View", "Garage", "Security"),
                isFeatured = true,
                isNew = false,
                agentId = "agent2",
                agentName = "Maria Smith",
                agentPhone = "+355 69 987 6543",
                createdAt = "2024-01-10",
                updatedAt = "2024-01-12"
            ),
            Property(
                id = "3",
                title = "Cozy Studio in Belgrade",
                description = "Perfect starter home or investment property in the heart of Belgrade.",
                price = 75000.0,
                currency = "EUR",
                propertyType = PropertyType.APARTMENT,
                bedrooms = 1,
                bathrooms = 1,
                area = 45.0,
                areaUnit = "sqm",
                address = "Knez Mihailova",
                city = "Belgrade",
                country = "Serbia",
                latitude = 44.8176,
                longitude = 20.4633,
                images = listOf("https://example.com/image3.jpg"),
                amenities = listOf("Furnished", "Central Location", "Public Transport"),
                isFeatured = false,
                isNew = true,
                agentId = "agent3",
                agentName = "Stefan Jovic",
                agentPhone = "+381 63 123 4567",
                createdAt = "2024-01-14",
                updatedAt = "2024-01-14"
            ),
            Property(
                id = "4",
                title = "Family House in Sofia",
                description = "Spacious family home with large garden. Quiet neighborhood.",
                price = 280000.0,
                currency = "EUR",
                propertyType = PropertyType.HOUSE,
                bedrooms = 4,
                bathrooms = 3,
                area = 220.0,
                areaUnit = "sqm",
                address = "Vitosha Boulevard",
                city = "Sofia",
                country = "Bulgaria",
                latitude = 42.6977,
                longitude = 23.3219,
                images = listOf("https://example.com/image4.jpg"),
                amenities = listOf("Garden", "Garage", "Basement", "Fireplace"),
                isFeatured = true,
                isNew = false,
                agentId = "agent4",
                agentName = "Elena Petrova",
                agentPhone = "+359 88 123 4567",
                createdAt = "2024-01-08",
                updatedAt = "2024-01-11"
            ),
            Property(
                id = "5",
                title = "Commercial Space in Skopje",
                description = "Prime commercial location. Ideal for retail or office use.",
                price = 320000.0,
                currency = "EUR",
                propertyType = PropertyType.COMMERCIAL,
                bedrooms = 0,
                bathrooms = 2,
                area = 180.0,
                areaUnit = "sqm",
                address = "Macedonia Square",
                city = "Skopje",
                country = "North Macedonia",
                latitude = 41.9981,
                longitude = 21.4254,
                images = listOf("https://example.com/image5.jpg"),
                amenities = listOf("Storefront", "Storage", "Parking", "Air Conditioning"),
                isFeatured = false,
                isNew = true,
                agentId = "agent5",
                agentName = "Marko Trajkovski",
                agentPhone = "+389 70 123 456",
                createdAt = "2024-01-13",
                updatedAt = "2024-01-13"
            )
        )
    }
}
