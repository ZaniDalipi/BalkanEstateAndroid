package com.zanoapps.core.domain.validator

/**
 * Validator for the contact agent form.
 */
object ContactFormValidator {

    fun validate(
        name: String,
        email: String,
        phone: String,
        message: String
    ): ContactFormValidationState {
        val nameResult = FormValidator.validateRequiredField(name, "Name")
            .takeIf { it.isError } ?: FormValidator.validateMinLength(name, 2, "Name")

        val emailResult = FormValidator.validateEmail(email)

        val phoneResult = FormValidator.validatePhone(phone)

        val messageResult = FormValidator.validateRequiredField(message, "Message")
            .takeIf { it.isError } ?: FormValidator.validateMinLength(message, 10, "Message")
            .takeIf { it.isError } ?: FormValidator.validateMaxLength(message, 1000, "Message")

        return ContactFormValidationState(
            nameError = (nameResult as? ValidationResult.Error)?.getDisplayMessage(),
            emailError = (emailResult as? ValidationResult.Error)?.getDisplayMessage(),
            phoneError = (phoneResult as? ValidationResult.Error)?.getDisplayMessage(),
            messageError = (messageResult as? ValidationResult.Error)?.getDisplayMessage(),
            isValid = nameResult.isValid && emailResult.isValid && phoneResult.isValid && messageResult.isValid
        )
    }
}

data class ContactFormValidationState(
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val messageError: String? = null,
    val isValid: Boolean = false
)
