package com.zanoapps.core.domain.enums

enum class PetPolicy(val key: String, val displayName: String) {
    ALLOWED("allowed", "Pets Allowed"),
    CATS_ONLY("cats_only", "Cats Only"),
    DOGS_ONLY("dogs_only", "Dogs Only"),
    SMALL_PETS("small_pets", "Small Pets Only"),
    NOT_ALLOWED("not_allowed", "No Pets"),
    NEGOTIABLE("negotiable", "Negotiable")
}