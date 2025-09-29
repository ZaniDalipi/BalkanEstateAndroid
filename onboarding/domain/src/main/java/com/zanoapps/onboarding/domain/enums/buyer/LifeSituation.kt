package com.zanoapps.onboarding.domain.enums.buyer

enum class LifeSituation(
    val title: String,
    val description: String,
) {
    YOUNG_PROFESSIONAL("Young Professional", "Starting career, looking for convenient location"),
    GROWING_FAMILY("Growing Family", "Need space and family-friendly amenities"),
    ESTABLISHED_FAMILY("Established Family", "Looking for quality schools and community"),
    EMPTY_NESTERS("Empty Nesters", "Ready to downsize and enjoy convenience"),
    RETIREES("Retirees", "Seeking comfort, accessibility, and healthcare proximity")
}