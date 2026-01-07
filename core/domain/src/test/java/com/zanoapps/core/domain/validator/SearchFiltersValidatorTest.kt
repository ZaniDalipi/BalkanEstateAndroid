package com.zanoapps.core.domain.validator

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchFiltersValidatorTest {

    @Test
    fun `empty filters should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "",
            maxArea = "",
            bedrooms = "",
            bathrooms = ""
        )
        assertTrue(result.isValid)
        assertFalse(result.hasErrors)
    }

    @Test
    fun `valid price range should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "10000",
            maxPrice = "50000",
            minArea = "",
            maxArea = "",
            bedrooms = "",
            bathrooms = ""
        )
        assertTrue(result.isValid)
        assertNull(result.minPriceError)
        assertNull(result.maxPriceError)
        assertNull(result.priceRangeError)
    }

    @Test
    fun `invalid min price should show error`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "abc",
            maxPrice = "50000",
            minArea = "",
            maxArea = "",
            bedrooms = "",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.hasErrors)
        assertTrue(result.minPriceError != null)
    }

    @Test
    fun `min price greater than max price should show range error`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "100000",
            maxPrice = "50000",
            minArea = "",
            maxArea = "",
            bedrooms = "",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.hasErrors)
        assertTrue(result.priceRangeError != null)
    }

    @Test
    fun `valid area range should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "50",
            maxArea = "200",
            bedrooms = "",
            bathrooms = ""
        )
        assertTrue(result.isValid)
        assertNull(result.minAreaError)
        assertNull(result.maxAreaError)
        assertNull(result.areaRangeError)
    }

    @Test
    fun `min area greater than max area should show range error`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "300",
            maxArea = "100",
            bedrooms = "",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.areaRangeError != null)
    }

    @Test
    fun `valid bedrooms should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "",
            maxArea = "",
            bedrooms = "3",
            bathrooms = ""
        )
        assertTrue(result.isValid)
        assertNull(result.bedroomsError)
    }

    @Test
    fun `invalid bedrooms should show error`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "",
            maxArea = "",
            bedrooms = "abc",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.bedroomsError != null)
    }

    @Test
    fun `valid bathrooms should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "",
            maxArea = "",
            bedrooms = "",
            bathrooms = "2"
        )
        assertTrue(result.isValid)
        assertNull(result.bathroomsError)
    }

    @Test
    fun `negative bedrooms should show error`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "",
            maxPrice = "",
            minArea = "",
            maxArea = "",
            bedrooms = "-1",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.bedroomsError != null)
    }

    @Test
    fun `all valid filters should be valid`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "50000",
            maxPrice = "200000",
            minArea = "80",
            maxArea = "150",
            bedrooms = "2",
            bathrooms = "1"
        )
        assertTrue(result.isValid)
        assertFalse(result.hasErrors)
        assertNull(result.minPriceError)
        assertNull(result.maxPriceError)
        assertNull(result.priceRangeError)
        assertNull(result.minAreaError)
        assertNull(result.maxAreaError)
        assertNull(result.areaRangeError)
        assertNull(result.bedroomsError)
        assertNull(result.bathroomsError)
    }

    @Test
    fun `multiple errors should all be present`() {
        val result = SearchFiltersValidator.validate(
            minPrice = "abc",
            maxPrice = "xyz",
            minArea = "",
            maxArea = "",
            bedrooms = "negative",
            bathrooms = ""
        )
        assertFalse(result.isValid)
        assertTrue(result.hasErrors)
        assertTrue(result.minPriceError != null)
        assertTrue(result.maxPriceError != null)
        assertTrue(result.bedroomsError != null)
    }
}
