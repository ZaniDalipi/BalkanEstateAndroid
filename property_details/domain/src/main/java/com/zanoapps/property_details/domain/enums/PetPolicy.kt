package com.zanoapps.property_details.domain.enums

enum class PetPolicy(val key: String) {
    ALLOWED("allowed"),
    CATS_ONLY("cats_only"),
    DOGS_ONLY("dogs_only"),
    SMALL_PETS("small_pets"),
    NOT_ALLOWED("not_allowed"),
    NEGOTIABLE("negotiable")
}