package com.zanoapps.core.domain.validator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormValidatorTest {

    // Email validation tests
    @Test
    fun `valid email should return Success`() {
        val result = FormValidator.validateEmail("test@example.com")
        assertTrue(result.isValid)
    }

    @Test
    fun `empty email should return REQUIRED_FIELD error`() {
        val result = FormValidator.validateEmail("")
        assertTrue(result.isError)
        assertEquals(ValidationError.REQUIRED_FIELD, (result as ValidationResult.Error).error)
    }

    @Test
    fun `blank email should return REQUIRED_FIELD error`() {
        val result = FormValidator.validateEmail("   ")
        assertTrue(result.isError)
        assertEquals(ValidationError.REQUIRED_FIELD, (result as ValidationResult.Error).error)
    }

    @Test
    fun `invalid email without @ should return INVALID_EMAIL error`() {
        val result = FormValidator.validateEmail("testexample.com")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_EMAIL, (result as ValidationResult.Error).error)
    }

    @Test
    fun `invalid email without domain should return INVALID_EMAIL error`() {
        val result = FormValidator.validateEmail("test@")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_EMAIL, (result as ValidationResult.Error).error)
    }

    @Test
    fun `email with subdomain should be valid`() {
        val result = FormValidator.validateEmail("test@mail.example.com")
        assertTrue(result.isValid)
    }

    // Phone validation tests
    @Test
    fun `valid phone number should return Success`() {
        val result = FormValidator.validatePhone("+355691234567")
        assertTrue(result.isValid)
    }

    @Test
    fun `empty phone should return Success (optional field)`() {
        val result = FormValidator.validatePhone("")
        assertTrue(result.isValid)
    }

    @Test
    fun `phone with spaces should be valid`() {
        val result = FormValidator.validatePhone("+355 69 123 4567")
        assertTrue(result.isValid)
    }

    @Test
    fun `invalid phone with letters should return INVALID_PHONE error`() {
        val result = FormValidator.validatePhone("+355abc123")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_PHONE, (result as ValidationResult.Error).error)
    }

    // Required field tests
    @Test
    fun `non-empty field should return Success`() {
        val result = FormValidator.validateRequiredField("test")
        assertTrue(result.isValid)
    }

    @Test
    fun `empty field should return REQUIRED_FIELD error`() {
        val result = FormValidator.validateRequiredField("")
        assertTrue(result.isError)
        assertEquals(ValidationError.REQUIRED_FIELD, (result as ValidationResult.Error).error)
    }

    // Min length tests
    @Test
    fun `string meeting min length should return Success`() {
        val result = FormValidator.validateMinLength("test", 4)
        assertTrue(result.isValid)
    }

    @Test
    fun `string below min length should return MIN_LENGTH error`() {
        val result = FormValidator.validateMinLength("te", 4)
        assertTrue(result.isError)
        assertEquals(ValidationError.MIN_LENGTH, (result as ValidationResult.Error).error)
    }

    // Max length tests
    @Test
    fun `string within max length should return Success`() {
        val result = FormValidator.validateMaxLength("test", 10)
        assertTrue(result.isValid)
    }

    @Test
    fun `string exceeding max length should return MAX_LENGTH error`() {
        val result = FormValidator.validateMaxLength("testing this long string", 10)
        assertTrue(result.isError)
        assertEquals(ValidationError.MAX_LENGTH, (result as ValidationResult.Error).error)
    }

    // Price validation tests
    @Test
    fun `valid price should return Success`() {
        val result = FormValidator.validatePrice("100000")
        assertTrue(result.isValid)
    }

    @Test
    fun `empty price should return Success (optional)`() {
        val result = FormValidator.validatePrice("")
        assertTrue(result.isValid)
    }

    @Test
    fun `price with commas should be valid`() {
        val result = FormValidator.validatePrice("100,000")
        assertTrue(result.isValid)
    }

    @Test
    fun `invalid price with letters should return INVALID_NUMBER error`() {
        val result = FormValidator.validatePrice("abc")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_NUMBER, (result as ValidationResult.Error).error)
    }

    // Price range tests
    @Test
    fun `valid price range should return Success`() {
        val result = FormValidator.validatePriceRange("10000", "50000")
        assertTrue(result.isValid)
    }

    @Test
    fun `price range with empty values should return Success`() {
        val result = FormValidator.validatePriceRange("", "50000")
        assertTrue(result.isValid)
    }

    @Test
    fun `price range with min greater than max should return error`() {
        val result = FormValidator.validatePriceRange("100000", "50000")
        assertTrue(result.isError)
        assertEquals(ValidationError.MIN_GREATER_THAN_MAX, (result as ValidationResult.Error).error)
    }

    // Positive integer tests
    @Test
    fun `valid positive integer should return Success`() {
        val result = FormValidator.validatePositiveInteger("5")
        assertTrue(result.isValid)
    }

    @Test
    fun `zero should return Success`() {
        val result = FormValidator.validatePositiveInteger("0")
        assertTrue(result.isValid)
    }

    @Test
    fun `negative integer should return error`() {
        val result = FormValidator.validatePositiveInteger("-5")
        assertTrue(result.isError)
        assertEquals(ValidationError.MUST_BE_NON_NEGATIVE, (result as ValidationResult.Error).error)
    }

    @Test
    fun `non-numeric string should return INVALID_NUMBER error`() {
        val result = FormValidator.validatePositiveInteger("abc")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_NUMBER, (result as ValidationResult.Error).error)
    }

    // Search query tests
    @Test
    fun `valid search query should return Success`() {
        val result = FormValidator.validateSearchQuery("apartments in Tirana")
        assertTrue(result.isValid)
    }

    @Test
    fun `empty search query should return Success`() {
        val result = FormValidator.validateSearchQuery("")
        assertTrue(result.isValid)
    }

    @Test
    fun `search query with invalid characters should return error`() {
        val result = FormValidator.validateSearchQuery("test<script>")
        assertTrue(result.isError)
        assertEquals(ValidationError.INVALID_CHARACTERS, (result as ValidationResult.Error).error)
    }

    @Test
    fun `very long search query should return MAX_LENGTH error`() {
        val longQuery = "a".repeat(250)
        val result = FormValidator.validateSearchQuery(longQuery)
        assertTrue(result.isError)
        assertEquals(ValidationError.MAX_LENGTH, (result as ValidationResult.Error).error)
    }
}
