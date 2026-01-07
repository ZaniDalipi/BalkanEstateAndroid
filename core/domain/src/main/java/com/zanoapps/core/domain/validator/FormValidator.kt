package com.zanoapps.core.domain.validator

/**
 * Comprehensive form validation utilities for BalkanEstate app.
 */
object FormValidator {

    // Email validation
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    // Phone validation for Balkan countries
    private val PHONE_REGEX = "^\\+?[0-9]{8,15}$".toRegex()
    private val BALKAN_PHONE_PREFIXES = listOf(
        "+355", // Albania
        "+387", // Bosnia and Herzegovina
        "+359", // Bulgaria
        "+385", // Croatia
        "+30",  // Greece
        "+383", // Kosovo
        "+389", // North Macedonia
        "+382", // Montenegro
        "+381", // Serbia
        "+386", // Slovenia
        "+90"   // Turkey
    )

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(ValidationError.REQUIRED_FIELD)
            !email.matches(EMAIL_REGEX) -> ValidationResult.Error(ValidationError.INVALID_EMAIL)
            else -> ValidationResult.Success
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        val cleanPhone = phone.replace(" ", "").replace("-", "")
        return when {
            phone.isBlank() -> ValidationResult.Success // Phone is optional
            !cleanPhone.matches(PHONE_REGEX) -> ValidationResult.Error(ValidationError.INVALID_PHONE)
            else -> ValidationResult.Success
        }
    }

    fun validateRequiredField(value: String, fieldName: String = "Field"): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Error(ValidationError.REQUIRED_FIELD, fieldName)
            else -> ValidationResult.Success
        }
    }

    fun validateMinLength(value: String, minLength: Int, fieldName: String = "Field"): ValidationResult {
        return when {
            value.length < minLength -> ValidationResult.Error(ValidationError.MIN_LENGTH, "$fieldName must be at least $minLength characters")
            else -> ValidationResult.Success
        }
    }

    fun validateMaxLength(value: String, maxLength: Int, fieldName: String = "Field"): ValidationResult {
        return when {
            value.length > maxLength -> ValidationResult.Error(ValidationError.MAX_LENGTH, "$fieldName must be at most $maxLength characters")
            else -> ValidationResult.Success
        }
    }

    fun validatePrice(price: String, allowZero: Boolean = false): ValidationResult {
        return when {
            price.isBlank() -> ValidationResult.Success // Price can be empty for filters
            else -> {
                val numericPrice = price.replace(",", "").replace(".", "").toDoubleOrNull()
                when {
                    numericPrice == null -> ValidationResult.Error(ValidationError.INVALID_NUMBER)
                    !allowZero && numericPrice <= 0 -> ValidationResult.Error(ValidationError.MUST_BE_POSITIVE)
                    numericPrice < 0 -> ValidationResult.Error(ValidationError.MUST_BE_NON_NEGATIVE)
                    else -> ValidationResult.Success
                }
            }
        }
    }

    fun validatePriceRange(minPrice: String, maxPrice: String): ValidationResult {
        if (minPrice.isBlank() || maxPrice.isBlank()) return ValidationResult.Success

        val min = minPrice.replace(",", "").replace(".", "").toDoubleOrNull()
        val max = maxPrice.replace(",", "").replace(".", "").toDoubleOrNull()

        return when {
            min == null || max == null -> ValidationResult.Error(ValidationError.INVALID_NUMBER)
            min > max -> ValidationResult.Error(ValidationError.MIN_GREATER_THAN_MAX, "Minimum price cannot be greater than maximum price")
            else -> ValidationResult.Success
        }
    }

    fun validateAreaRange(minArea: String, maxArea: String): ValidationResult {
        if (minArea.isBlank() || maxArea.isBlank()) return ValidationResult.Success

        val min = minArea.toDoubleOrNull()
        val max = maxArea.toDoubleOrNull()

        return when {
            min == null || max == null -> ValidationResult.Error(ValidationError.INVALID_NUMBER)
            min > max -> ValidationResult.Error(ValidationError.MIN_GREATER_THAN_MAX, "Minimum area cannot be greater than maximum area")
            else -> ValidationResult.Success
        }
    }

    fun validatePositiveInteger(value: String, fieldName: String = "Field"): ValidationResult {
        if (value.isBlank()) return ValidationResult.Success

        val number = value.toIntOrNull()
        return when {
            number == null -> ValidationResult.Error(ValidationError.INVALID_NUMBER)
            number < 0 -> ValidationResult.Error(ValidationError.MUST_BE_NON_NEGATIVE, "$fieldName must be 0 or greater")
            else -> ValidationResult.Success
        }
    }

    fun validateSearchQuery(query: String): ValidationResult {
        return when {
            query.length > 200 -> ValidationResult.Error(ValidationError.MAX_LENGTH, "Search query is too long")
            query.any { it in "<>{}[]|\\^" } -> ValidationResult.Error(ValidationError.INVALID_CHARACTERS, "Query contains invalid characters")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val error: ValidationError, val message: String? = null) : ValidationResult()

    val isValid: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

enum class ValidationError {
    REQUIRED_FIELD,
    INVALID_EMAIL,
    INVALID_PHONE,
    INVALID_NUMBER,
    MIN_LENGTH,
    MAX_LENGTH,
    MUST_BE_POSITIVE,
    MUST_BE_NON_NEGATIVE,
    MIN_GREATER_THAN_MAX,
    INVALID_CHARACTERS
}

/**
 * Extension function to get a user-friendly error message
 */
fun ValidationResult.Error.getDisplayMessage(): String {
    return message ?: when (error) {
        ValidationError.REQUIRED_FIELD -> "This field is required"
        ValidationError.INVALID_EMAIL -> "Please enter a valid email address"
        ValidationError.INVALID_PHONE -> "Please enter a valid phone number"
        ValidationError.INVALID_NUMBER -> "Please enter a valid number"
        ValidationError.MIN_LENGTH -> "Input is too short"
        ValidationError.MAX_LENGTH -> "Input is too long"
        ValidationError.MUST_BE_POSITIVE -> "Value must be greater than zero"
        ValidationError.MUST_BE_NON_NEGATIVE -> "Value cannot be negative"
        ValidationError.MIN_GREATER_THAN_MAX -> "Minimum cannot be greater than maximum"
        ValidationError.INVALID_CHARACTERS -> "Input contains invalid characters"
    }
}
