package com.zanoapps.core.data.mappers

import com.zanoapps.core.data.remote.dto.PropertyDto
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.domain.model.BalkanEstateProperty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun PropertyDto.toEntity(): PropertyEntity {
    return PropertyEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        currency = currency,
        imageUrl = imageUrl,
        images = Json.encodeToString(images),
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
        agentId = agentId,
        agentName = agentName,
        agentPhone = agentPhone,
        agentEmail = agentEmail,
        agentImageUrl = agentImageUrl,
        isFeatured = isFeatured,
        isUrgent = isUrgent,
        amenities = amenities?.let { Json.encodeToString(it) },
        yearBuilt = yearBuilt,
        parkingSpaces = parkingSpaces,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
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
        isUrgent = isUrgent
    )
}

fun List<PropertyDto>.toEntities(): List<PropertyEntity> = map { it.toEntity() }
fun List<PropertyEntity>.toDomainList(): List<BalkanEstateProperty> = map { it.toDomain() }
