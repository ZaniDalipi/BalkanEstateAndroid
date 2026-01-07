package com.zanoapps.core.domain.validator

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactFormValidatorTest {

    @Test
    fun `valid form should be valid`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "+355 69 123 4567",
            message = "I am interested in this property. Please contact me."
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
        assertNull(result.emailError)
        assertNull(result.phoneError)
        assertNull(result.messageError)
    }

    @Test
    fun `empty name should show error`() {
        val result = ContactFormValidator.validate(
            name = "",
            email = "john@example.com",
            phone = "",
            message = "I am interested in this property."
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
    }

    @Test
    fun `short name should show error`() {
        val result = ContactFormValidator.validate(
            name = "J",
            email = "john@example.com",
            phone = "",
            message = "I am interested in this property."
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
    }

    @Test
    fun `invalid email should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "invalid-email",
            phone = "",
            message = "I am interested in this property."
        )
        assertFalse(result.isValid)
        assertTrue(result.emailError != null)
    }

    @Test
    fun `empty email should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "",
            phone = "",
            message = "I am interested in this property."
        )
        assertFalse(result.isValid)
        assertTrue(result.emailError != null)
    }

    @Test
    fun `invalid phone should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "invalid-phone-abc",
            message = "I am interested in this property."
        )
        assertFalse(result.isValid)
        assertTrue(result.phoneError != null)
    }

    @Test
    fun `empty phone should be valid (optional field)`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "",
            message = "I am interested in this property."
        )
        assertNull(result.phoneError)
    }

    @Test
    fun `empty message should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "",
            message = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.messageError != null)
    }

    @Test
    fun `short message should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "",
            message = "Hi"
        )
        assertFalse(result.isValid)
        assertTrue(result.messageError != null)
    }

    @Test
    fun `very long message should show error`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "",
            message = "a".repeat(1001)
        )
        assertFalse(result.isValid)
        assertTrue(result.messageError != null)
    }

    @Test
    fun `multiple errors should all be present`() {
        val result = ContactFormValidator.validate(
            name = "",
            email = "invalid",
            phone = "abc",
            message = "Hi"
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
        assertTrue(result.emailError != null)
        assertTrue(result.phoneError != null)
        assertTrue(result.messageError != null)
    }

    @Test
    fun `valid Albanian phone number should be valid`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "+35569123456",
            message = "I am interested in this property."
        )
        assertTrue(result.isValid)
        assertNull(result.phoneError)
    }

    @Test
    fun `phone with dashes should be valid`() {
        val result = ContactFormValidator.validate(
            name = "John Doe",
            email = "john@example.com",
            phone = "+355-69-123-4567",
            message = "I am interested in this property."
        )
        assertTrue(result.isValid)
        assertNull(result.phoneError)
    }
}
