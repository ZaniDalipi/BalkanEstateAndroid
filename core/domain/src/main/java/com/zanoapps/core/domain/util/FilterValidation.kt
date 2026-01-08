package com.zanoapps.core.domain.util

/**
 * Utility object for validating filter inputs throughout the application.
 * Provides validation for price ranges, room counts, square footage, and text queries.
 */
object FilterValidation {

    // Price validation constants
    const val MIN_PRICE = 0.0
    const val MAX_PRICE = 100_000_000.0
    const val DEFAULT_MIN_PRICE = 0.0
    const val DEFAULT_MAX_PRICE = 10_000_000.0

    // Room validation constants
    const val MIN_ROOMS = 0
    const val MAX_ROOMS = 20

    // Square footage validation constants
    const val MIN_SQUARE_FOOTAGE = 0
    const val MAX_SQUARE_FOOTAGE = 100_000
    const val DEFAULT_MIN_SQUARE_FOOTAGE = 0
    const val DEFAULT_MAX_SQUARE_FOOTAGE = 10_000

    // Query validation constants
    const val MIN_QUERY_LENGTH = 0
    const val MAX_QUERY_LENGTH = 500

    /**
     * Validation result sealed class for type-safe error handling
     */
    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }

    /**
     * Validates a price value is within acceptable bounds
     */
    fun validatePrice(price: Double?): ValidationResult {
        return when {
            price == null -> ValidationResult.Valid
            price < MIN_PRICE -> ValidationResult.Invalid("Price cannot be negative")
            price > MAX_PRICE -> ValidationResult.Invalid("Price exceeds maximum allowed value")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Validates a price range (min <= max)
     */
    fun validatePriceRange(minPrice: Double?, maxPrice: Double?): ValidationResult {
        val minValidation = validatePrice(minPrice)
        if (minValidation is ValidationResult.Invalid) return minValidation

        val maxValidation = validatePrice(maxPrice)
        if (maxValidation is ValidationResult.Invalid) return maxValidation

        return when {
            minPrice != null && maxPrice != null && minPrice > maxPrice ->
                ValidationResult.Invalid("Minimum price cannot exceed maximum price")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Validates room count (bedrooms/bathrooms)
     */
    fun validateRoomCount(count: Int?): ValidationResult {
        return when {
            count == null -> ValidationResult.Valid
            count < MIN_ROOMS -> ValidationResult.Invalid("Room count cannot be negative")
            count > MAX_ROOMS -> ValidationResult.Invalid("Room count exceeds reasonable maximum")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Validates square footage value
     */
    fun validateSquareFootage(sqft: Int?): ValidationResult {
        return when {
            sqft == null -> ValidationResult.Valid
            sqft < MIN_SQUARE_FOOTAGE -> ValidationResult.Invalid("Square footage cannot be negative")
            sqft > MAX_SQUARE_FOOTAGE -> ValidationResult.Invalid("Square footage exceeds maximum")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Validates square footage range
     */
    fun validateSquareFootageRange(minSqft: Int?, maxSqft: Int?): ValidationResult {
        val minValidation = validateSquareFootage(minSqft)
        if (minValidation is ValidationResult.Invalid) return minValidation

        val maxValidation = validateSquareFootage(maxSqft)
        if (maxValidation is ValidationResult.Invalid) return maxValidation

        return when {
            minSqft != null && maxSqft != null && minSqft > maxSqft ->
                ValidationResult.Invalid("Minimum area cannot exceed maximum area")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Validates a search query string
     */
    fun validateSearchQuery(query: String?): ValidationResult {
        return when {
            query == null -> ValidationResult.Valid
            query.length > MAX_QUERY_LENGTH ->
                ValidationResult.Invalid("Search query is too long")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Sanitizes a search query by trimming and removing excessive whitespace
     */
    fun sanitizeQuery(query: String): String {
        return query.trim().replace(Regex("\\s+"), " ")
    }

    /**
     * Parses a price string to Double, handling common formats
     */
    fun parsePrice(priceString: String): Double? {
        if (priceString.isBlank()) return null

        val cleanedString = priceString
            .replace(",", "")
            .replace(" ", "")
            .replace("$", "")
            .replace("€", "")
            .replace("£", "")
            .trim()

        return cleanedString.toDoubleOrNull()
    }

    /**
     * Formats a price for display with proper grouping
     */
    fun formatPrice(price: Double, currency: String = "€"): String {
        return when {
            price >= 1_000_000 -> String.format("%s%.1fM", currency, price / 1_000_000)
            price >= 1_000 -> String.format("%s%.0fK", currency, price / 1_000)
            else -> String.format("%s%.0f", currency, price)
        }
    }

    /**
     * Formats a price range for display
     */
    fun formatPriceRange(minPrice: Double?, maxPrice: Double?, currency: String = "€"): String {
        return when {
            minPrice != null && maxPrice != null ->
                "${formatPrice(minPrice, currency)} - ${formatPrice(maxPrice, currency)}"
            minPrice != null -> "${formatPrice(minPrice, currency)}+"
            maxPrice != null -> "Up to ${formatPrice(maxPrice, currency)}"
            else -> "Any price"
        }
    }
}
