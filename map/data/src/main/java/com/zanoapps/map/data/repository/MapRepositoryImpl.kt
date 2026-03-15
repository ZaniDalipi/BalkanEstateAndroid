package com.zanoapps.map.data.repository

import com.zanoapps.map.domain.model.MapProperty
import com.zanoapps.map.domain.model.MapRegion
import com.zanoapps.map.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MapRepositoryImpl : MapRepository {

    private val mockProperties = listOf(
        MapProperty(id = "mp1", title = "Modern Apartment in Skopje", price = 85000.0, latitude = 41.9973, longitude = 21.4280, propertyType = "Apartment", bedrooms = 2, bathrooms = 1, area = 75.0),
        MapProperty(id = "mp2", title = "Lakeside Villa in Ohrid", price = 250000.0, latitude = 41.1231, longitude = 20.8016, propertyType = "Villa", bedrooms = 4, bathrooms = 3, area = 220.0),
        MapProperty(id = "mp3", title = "Studio in Belgrade Center", price = 120000.0, latitude = 44.8176, longitude = 20.4633, propertyType = "Studio", bedrooms = 1, bathrooms = 1, area = 45.0),
        MapProperty(id = "mp4", title = "Family House in Sarajevo", price = 180000.0, latitude = 43.8563, longitude = 18.4131, propertyType = "House", bedrooms = 3, bathrooms = 2, area = 150.0),
        MapProperty(id = "mp5", title = "Penthouse in Tirana", price = 200000.0, latitude = 41.3275, longitude = 19.8187, propertyType = "Penthouse", bedrooms = 3, bathrooms = 2, area = 130.0),
        MapProperty(id = "mp6", title = "Coastal Apartment in Durres", price = 95000.0, latitude = 41.3246, longitude = 19.4563, propertyType = "Apartment", bedrooms = 2, bathrooms = 1, area = 80.0),
        MapProperty(id = "mp7", title = "Mountain Cabin near Mavrovo", price = 65000.0, latitude = 41.6514, longitude = 20.7308, propertyType = "Cabin", bedrooms = 2, bathrooms = 1, area = 60.0),
        MapProperty(id = "mp8", title = "Luxury Flat in Zagreb", price = 310000.0, latitude = 45.8150, longitude = 15.9819, propertyType = "Apartment", bedrooms = 3, bathrooms = 2, area = 110.0)
    )

    override fun getPropertiesInRegion(region: MapRegion): Flow<List<MapProperty>> {
        val filtered = mockProperties.filter { property ->
            property.latitude in (region.centerLatitude - region.latitudeDelta / 2)..(region.centerLatitude + region.latitudeDelta / 2) &&
            property.longitude in (region.centerLongitude - region.longitudeDelta / 2)..(region.centerLongitude + region.longitudeDelta / 2)
        }
        return flowOf(filtered)
    }

    override suspend fun getPropertyById(id: String): MapProperty? {
        return mockProperties.find { it.id == id }
    }
}
