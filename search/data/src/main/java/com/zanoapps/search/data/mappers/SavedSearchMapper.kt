package com.zanoapps.search.data.mappers

import com.zanoapps.core.database.entity.SavedSearchEntity
import com.zanoapps.search.domain.model.SavedSearch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SavedSearchEntity.toDomain(): SavedSearch {
    return SavedSearch(
        id = id,
        userId = userId,
        name = name,
        country = country,
        city = city,
        listingType = listingType,
        propertyTypes = propertyTypes?.let {
            try { Json.decodeFromString<List<String>>(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        minPrice = minPrice,
        maxPrice = maxPrice,
        minArea = minArea,
        maxArea = maxArea,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        amenities = amenities?.let {
            try { Json.decodeFromString<List<String>>(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        notificationsEnabled = notificationsEnabled,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun SavedSearch.toEntity(): SavedSearchEntity {
    return SavedSearchEntity(
        id = id,
        userId = userId,
        name = name,
        country = country,
        city = city,
        listingType = listingType,
        propertyTypes = if (propertyTypes.isNotEmpty()) Json.encodeToString(propertyTypes) else null,
        minPrice = minPrice,
        maxPrice = maxPrice,
        minArea = minArea,
        maxArea = maxArea,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        amenities = if (amenities.isNotEmpty()) Json.encodeToString(amenities) else null,
        notificationsEnabled = notificationsEnabled,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun List<SavedSearchEntity>.toDomainList(): List<SavedSearch> = map { it.toDomain() }
