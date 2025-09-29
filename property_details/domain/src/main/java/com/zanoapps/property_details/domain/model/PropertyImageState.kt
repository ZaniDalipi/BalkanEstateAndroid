package com.zanoapps.property_details.domain.model

data class PropertyImageState(
    val id: String,
    val url: String,
    val caption: String?,
    val order: Int,
    val isMain: Boolean = false
)
