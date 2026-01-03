package com.zanoapps.core.data.remote

import com.zanoapps.core.data.networking.constructRoute
import com.zanoapps.core.data.networking.safeCall
import com.zanoapps.core.data.remote.dto.PropertiesResponse
import com.zanoapps.core.data.remote.dto.PropertyResponse
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class PropertyApiService(
    private val httpClient: HttpClient
) {
    suspend fun getProperties(
        page: Int = 1,
        limit: Int = 20,
        listingType: String? = null,
        propertyType: String? = null,
        city: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minBedrooms: Int? = null
    ): Result<PropertiesResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/properties"))
                parameter("page", page)
                parameter("limit", limit)
                listingType?.let { parameter("listingType", it) }
                propertyType?.let { parameter("propertyType", it) }
                city?.let { parameter("city", it) }
                minPrice?.let { parameter("minPrice", it) }
                maxPrice?.let { parameter("maxPrice", it) }
                minBedrooms?.let { parameter("minBedrooms", it) }
            }
        }
    }

    suspend fun getPropertyById(propertyId: String): Result<PropertyResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/properties/$propertyId"))
            }
        }
    }

    suspend fun getFeaturedProperties(limit: Int = 10): Result<PropertiesResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/properties/featured"))
                parameter("limit", limit)
            }
        }
    }

    suspend fun getPropertiesByAgent(agentId: String): Result<PropertiesResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/properties/agent/$agentId"))
            }
        }
    }

    suspend fun searchProperties(query: String): Result<PropertiesResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/properties/search"))
                parameter("q", query)
            }
        }
    }
}
