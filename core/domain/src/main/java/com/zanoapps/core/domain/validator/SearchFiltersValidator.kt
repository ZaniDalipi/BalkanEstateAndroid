package com.zanoapps.core.domain.validator

/**
 * Validator for search filters form.
 */
object SearchFiltersValidator {

    fun validate(
        minPrice: String,
        maxPrice: String,
        minArea: String,
        maxArea: String,
        bedrooms: String,
        bathrooms: String
    ): SearchFiltersValidationState {
        val minPriceResult = FormValidator.validatePrice(minPrice)
        val maxPriceResult = FormValidator.validatePrice(maxPrice)
        val priceRangeResult = FormValidator.validatePriceRange(minPrice, maxPrice)

        val minAreaResult = FormValidator.validatePrice(minArea)
        val maxAreaResult = FormValidator.validatePrice(maxArea)
        val areaRangeResult = FormValidator.validateAreaRange(minArea, maxArea)

        val bedroomsResult = FormValidator.validatePositiveInteger(bedrooms, "Bedrooms")
        val bathroomsResult = FormValidator.validatePositiveInteger(bathrooms, "Bathrooms")

        val priceError = when {
            minPriceResult.isError -> (minPriceResult as ValidationResult.Error).getDisplayMessage()
            maxPriceResult.isError -> (maxPriceResult as ValidationResult.Error).getDisplayMessage()
            priceRangeResult.isError -> (priceRangeResult as ValidationResult.Error).getDisplayMessage()
            else -> null
        }

        val areaError = when {
            minAreaResult.isError -> (minAreaResult as ValidationResult.Error).getDisplayMessage()
            maxAreaResult.isError -> (maxAreaResult as ValidationResult.Error).getDisplayMessage()
            areaRangeResult.isError -> (areaRangeResult as ValidationResult.Error).getDisplayMessage()
            else -> null
        }

        return SearchFiltersValidationState(
            minPriceError = if (minPriceResult.isError) (minPriceResult as ValidationResult.Error).getDisplayMessage() else null,
            maxPriceError = if (maxPriceResult.isError) (maxPriceResult as ValidationResult.Error).getDisplayMessage() else null,
            priceRangeError = if (priceRangeResult.isError) (priceRangeResult as ValidationResult.Error).getDisplayMessage() else null,
            minAreaError = if (minAreaResult.isError) (minAreaResult as ValidationResult.Error).getDisplayMessage() else null,
            maxAreaError = if (maxAreaResult.isError) (maxAreaResult as ValidationResult.Error).getDisplayMessage() else null,
            areaRangeError = if (areaRangeResult.isError) (areaRangeResult as ValidationResult.Error).getDisplayMessage() else null,
            bedroomsError = (bedroomsResult as? ValidationResult.Error)?.getDisplayMessage(),
            bathroomsError = (bathroomsResult as? ValidationResult.Error)?.getDisplayMessage(),
            isValid = minPriceResult.isValid && maxPriceResult.isValid && priceRangeResult.isValid &&
                    minAreaResult.isValid && maxAreaResult.isValid && areaRangeResult.isValid &&
                    bedroomsResult.isValid && bathroomsResult.isValid
        )
    }
}

data class SearchFiltersValidationState(
    val minPriceError: String? = null,
    val maxPriceError: String? = null,
    val priceRangeError: String? = null,
    val minAreaError: String? = null,
    val maxAreaError: String? = null,
    val areaRangeError: String? = null,
    val bedroomsError: String? = null,
    val bathroomsError: String? = null,
    val isValid: Boolean = true
) {
    val hasErrors: Boolean
        get() = !isValid
}
