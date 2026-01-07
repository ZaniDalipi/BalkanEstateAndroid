package com.zanoapps.core.domain.validator

/**
 * Validator for saved search form.
 */
object SavedSearchValidator {

    fun validate(
        name: String,
        minPrice: String = "",
        maxPrice: String = "",
        minArea: String = "",
        maxArea: String = ""
    ): SavedSearchValidationState {
        val nameResult = FormValidator.validateRequiredField(name, "Search name")
            .takeIf { it.isError } ?: FormValidator.validateMinLength(name, 2, "Search name")
            .takeIf { it.isError } ?: FormValidator.validateMaxLength(name, 50, "Search name")

        // Validate filters if provided
        val filtersValidation = SearchFiltersValidator.validate(
            minPrice = minPrice,
            maxPrice = maxPrice,
            minArea = minArea,
            maxArea = maxArea,
            bedrooms = "",
            bathrooms = ""
        )

        return SavedSearchValidationState(
            nameError = (nameResult as? ValidationResult.Error)?.getDisplayMessage(),
            filtersValidation = filtersValidation,
            isValid = nameResult.isValid && filtersValidation.isValid
        )
    }
}

data class SavedSearchValidationState(
    val nameError: String? = null,
    val filtersValidation: SearchFiltersValidationState = SearchFiltersValidationState(),
    val isValid: Boolean = false
)
