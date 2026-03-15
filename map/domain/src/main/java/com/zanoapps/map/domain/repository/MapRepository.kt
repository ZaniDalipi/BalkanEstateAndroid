package com.zanoapps.map.domain.repository

import com.zanoapps.map.domain.model.MapProperty
import com.zanoapps.map.domain.model.MapRegion
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun getPropertiesInRegion(region: MapRegion): Flow<List<MapProperty>>
    suspend fun getPropertyById(id: String): MapProperty?
}
