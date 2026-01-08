package com.zanoapps.notification.domain.model

/**
 * Represents the different types of notifications in the BalkanEstate app.
 */
enum class NotificationType {
    /** Price drop on a saved or viewed property */
    PRICE_DROP,

    /** New listing matching saved search criteria */
    NEW_LISTING_MATCH,

    /** Property has been saved by user */
    PROPERTY_SAVED,

    /** New message from agent or buyer */
    MESSAGE,

    /** Property viewing scheduled */
    VIEWING_SCHEDULED,

    /** Property viewing reminder */
    VIEWING_REMINDER,

    /** Property status changed (sold, rented, etc.) */
    PROPERTY_STATUS_CHANGE,

    /** Favorite property has been updated */
    FAVORITE_UPDATE,

    /** System announcement or promotional */
    SYSTEM,

    /** Agent replied to inquiry */
    AGENT_RESPONSE,

    /** Saved search alert */
    SAVED_SEARCH_ALERT,

    /** General promotional notification */
    PROMOTIONAL
}
