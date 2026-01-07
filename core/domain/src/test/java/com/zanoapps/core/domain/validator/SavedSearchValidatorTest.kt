package com.zanoapps.core.domain.validator

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SavedSearchValidatorTest {

    @Test
    fun `valid saved search should be valid`() {
        val result = SavedSearchValidator.validate(
            name = "My Search"
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
    }

    @Test
    fun `empty name should show error`() {
        val result = SavedSearchValidator.validate(
            name = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
    }

    @Test
    fun `short name should show error`() {
        val result = SavedSearchValidator.validate(
            name = "A"
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
    }

    @Test
    fun `very long name should show error`() {
        val result = SavedSearchValidator.validate(
            name = "a".repeat(51)
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
    }

    @Test
    fun `valid name with valid filters should be valid`() {
        val result = SavedSearchValidator.validate(
            name = "Tirana Apartments",
            minPrice = "50000",
            maxPrice = "100000",
            minArea = "50",
            maxArea = "150"
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
        assertTrue(result.filtersValidation.isValid)
    }

    @Test
    fun `valid name with invalid price range should be invalid`() {
        val result = SavedSearchValidator.validate(
            name = "My Search",
            minPrice = "200000",
            maxPrice = "100000"
        )
        assertFalse(result.isValid)
        assertNull(result.nameError)
        assertFalse(result.filtersValidation.isValid)
        assertTrue(result.filtersValidation.priceRangeError != null)
    }

    @Test
    fun `valid name with invalid area range should be invalid`() {
        val result = SavedSearchValidator.validate(
            name = "My Search",
            minArea = "200",
            maxArea = "100"
        )
        assertFalse(result.isValid)
        assertNull(result.nameError)
        assertFalse(result.filtersValidation.isValid)
        assertTrue(result.filtersValidation.areaRangeError != null)
    }

    @Test
    fun `name with minimum valid length should be valid`() {
        val result = SavedSearchValidator.validate(
            name = "AB"
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
    }

    @Test
    fun `name with maximum valid length should be valid`() {
        val result = SavedSearchValidator.validate(
            name = "a".repeat(50)
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
    }

    @Test
    fun `name with special characters should be valid`() {
        val result = SavedSearchValidator.validate(
            name = "Tirana - 2BR Apartments"
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
    }

    @Test
    fun `both invalid name and filters should show both errors`() {
        val result = SavedSearchValidator.validate(
            name = "",
            minPrice = "abc"
        )
        assertFalse(result.isValid)
        assertTrue(result.nameError != null)
        assertFalse(result.filtersValidation.isValid)
        assertTrue(result.filtersValidation.minPriceError != null)
    }
}
