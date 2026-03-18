package com.zanoapps.map.data.repository

import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.map.domain.model.MapProperty
import com.zanoapps.map.domain.model.MapRegion
import com.zanoapps.map.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MapRepositoryImpl(
    private val propertyDao: PropertyDao
) : MapRepository {

    override fun getPropertiesInRegion(region: MapRegion): Flow<List<MapProperty>> {
        return propertyDao.getAllProperties().map { entities ->
            entities.filter { entity ->
                entity.latitude in (region.centerLatitude - region.latitudeDelta / 2)..(region.centerLatitude + region.latitudeDelta / 2) &&
                entity.longitude in (region.centerLongitude - region.longitudeDelta / 2)..(region.centerLongitude + region.longitudeDelta / 2)
            }.map { entity ->
                MapProperty(
                    id = entity.id,
                    title = entity.title,
                    price = entity.price,
                    currency = entity.currency,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    propertyType = entity.propertyType,
                    imageUrl = entity.imageUrl,
                    bedrooms = entity.bedrooms,
                    bathrooms = entity.bathrooms,
                    area = entity.squareFootage.toDouble()
                )
            }
        }
    }

    override suspend fun getPropertyById(id: String): MapProperty? {
        return propertyDao.getPropertyById(id)?.let { entity ->
            MapProperty(
                id = entity.id,
                title = entity.title,
                price = entity.price,
                currency = entity.currency,
                latitude = entity.latitude,
                longitude = entity.longitude,
                propertyType = entity.propertyType,
                imageUrl = entity.imageUrl,
                bedrooms = entity.bedrooms,
                bathrooms = entity.bathrooms,
                area = entity.squareFootage.toDouble()
            )
        }
    }
}
