package com.carly.vehicles.data.dto

import com.carly.vehicles.data.datasource.dto.BrandDto
import com.carly.vehicles.data.datasource.dto.SeriesDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Data Transfer Objects (DTOs) testing serialization models.
 * 
 * Tests cover:
 * - DTO creation and property validation
 * - Data class equality and consistency
 * - Edge cases and data validation
 * - Serialization structure validation
 */
class DataTransferObjectsTest {

    @Test
    fun `BrandDto should be created with all required properties`() {
        // Given
        val series = listOf(
            SeriesDto("x5", "X5", 1999, 2024),
            SeriesDto("x3", "X3", 2003, 2024)
        )
        val features = listOf("Diagnostics", "Live Data", "Battery Check")

        // When
        val brandDto = BrandDto(
            brandId = "bmw",
            brandName = "BMW",
            series = series,
            features = features
        )

        // Then
        assertEquals("bmw", brandDto.brandId)
        assertEquals("BMW", brandDto.brandName)
        assertEquals(series, brandDto.series)
        assertEquals(features, brandDto.features)
    }

    @Test
    fun `SeriesDto should be created with all required properties`() {
        // When
        val seriesDto = SeriesDto(
            id = "a4",
            name = "A4",
            minYear = 1994,
            maxYear = 2024
        )

        // Then
        assertEquals("a4", seriesDto.id)
        assertEquals("A4", seriesDto.name)
        assertEquals(1994, seriesDto.minYear)
        assertEquals(2024, seriesDto.maxYear)
    }

    @Test
    fun `BrandDto equality should work correctly`() {
        // Given
        val series1 = listOf(SeriesDto("x5", "X5", 1999, 2024))
        val series2 = listOf(SeriesDto("x5", "X5", 1999, 2024))
        val features = listOf("Diagnostics")

        val brandDto1 = BrandDto("bmw", "BMW", series1, features)
        val brandDto2 = BrandDto("bmw", "BMW", series2, features)
        val brandDto3 = BrandDto("audi", "Audi", series1, features)

        // Then
        assertEquals(brandDto1, brandDto2)
        assertEquals(brandDto1.hashCode(), brandDto2.hashCode())
        assertNotEquals(brandDto1, brandDto3)
    }

    @Test
    fun `SeriesDto equality should work correctly`() {
        // Given
        val seriesDto1 = SeriesDto("a4", "A4", 1994, 2024)
        val seriesDto2 = SeriesDto("a4", "A4", 1994, 2024)
        val seriesDto3 = SeriesDto("a6", "A6", 1994, 2024)

        // Then
        assertEquals(seriesDto1, seriesDto2)
        assertEquals(seriesDto1.hashCode(), seriesDto2.hashCode())
        assertNotEquals(seriesDto1, seriesDto3)
    }

    @Test
    fun `BrandDto copy should work correctly`() {
        // Given
        val original = BrandDto(
            brandId = "bmw",
            brandName = "BMW",
            series = listOf(SeriesDto("x5", "X5", 1999, 2024)),
            features = listOf("Diagnostics")
        )

        // When
        val copied = original.copy(brandName = "BMW Group")

        // Then
        assertEquals("bmw", copied.brandId)
        assertEquals("BMW Group", copied.brandName) // Changed
        assertEquals(original.series, copied.series) // Unchanged
        assertEquals(original.features, copied.features) // Unchanged
    }

    @Test
    fun `SeriesDto copy should work correctly`() {
        // Given
        val original = SeriesDto("x5", "X5", 1999, 2024)

        // When
        val copied = original.copy(maxYear = 2025)

        // Then
        assertEquals("x5", copied.id)
        assertEquals("X5", copied.name)
        assertEquals(1999, copied.minYear)
        assertEquals(2025, copied.maxYear) // Changed
    }

    @Test
    fun `BrandDto should handle empty collections`() {
        // When
        val brandDto = BrandDto(
            brandId = "empty",
            brandName = "Empty Brand",
            series = emptyList(),
            features = emptyList()
        )

        // Then
        assertTrue("Series should be empty", brandDto.series.isEmpty())
        assertTrue("Features should be empty", brandDto.features.isEmpty())
    }

    @Test
    fun `BrandDto should handle large collections`() {
        // Given
        val largeSeries = (1..100).map { i ->
            SeriesDto("series$i", "Series $i", 2000 + i, 2024)
        }
        val largeFeatures = (1..50).map { "Feature $it" }

        // When
        val brandDto = BrandDto(
            brandId = "large",
            brandName = "Large Brand",
            series = largeSeries,
            features = largeFeatures
        )

        // Then
        assertEquals(100, brandDto.series.size)
        assertEquals(50, brandDto.features.size)
    }

    @Test
    fun `SeriesDto should handle edge case years`() {
        // Given
        val edgeCases = listOf(
            SeriesDto("old", "Old Series", 1900, 1950),
            SeriesDto("current", "Current Series", 2020, 2024),
            SeriesDto("future", "Future Series", 2025, 2030)
        )

        // When & Then
        edgeCases.forEach { series ->
            assertTrue("Min year should be reasonable", series.minYear >= 1900)
            assertTrue("Max year should be reasonable", series.maxYear <= 2100)
            assertTrue("Min year should be less than max year", 
                       series.minYear < series.maxYear)
        }
    }

    @Test
    fun `BrandDto toString should be readable`() {
        // Given
        val brandDto = BrandDto(
            brandId = "bmw",
            brandName = "BMW",
            series = listOf(SeriesDto("x5", "X5", 1999, 2024)),
            features = listOf("Diagnostics")
        )

        // When
        val toString = brandDto.toString()

        // Then
        assertTrue("Should contain brand ID", toString.contains("bmw"))
        assertTrue("Should contain brand name", toString.contains("BMW"))
        assertTrue("Should be readable", toString.contains("BrandDto"))
    }

    @Test
    fun `SeriesDto should validate year ranges`() {
        // Given
        val validSeries = SeriesDto("valid", "Valid Series", 2000, 2024)
        val edgeCase = SeriesDto("edge", "Edge Case", 1980, 1981)

        // Then
        assertTrue("Valid series should have reasonable range",
                   validSeries.maxYear - validSeries.minYear >= 0)
        assertTrue("Edge case should be valid",
                   edgeCase.maxYear - edgeCase.minYear >= 0)
    }

    @Test
    fun `DTOs should support nested data correctly`() {
        // Given
        val nestedSeries = SeriesDto("nested", "Nested Series", 2020, 2024)
        val brandWithNested = BrandDto(
            brandId = "nested-brand",
            brandName = "Nested Brand",
            series = listOf(nestedSeries),
            features = listOf("Feature1", "Feature2")
        )

        // When
        val extractedSeries = brandWithNested.series.first()

        // Then
        assertEquals(nestedSeries, extractedSeries)
        assertEquals("nested", extractedSeries.id)
        assertEquals(2020, extractedSeries.minYear)
    }

    @Test
    fun `DTOs should handle special characters in strings`() {
        // Given
        val specialBrand = BrandDto(
            brandId = "special-chars",
            brandName = "Special & Co. (Ümlaut)",
            series = listOf(SeriesDto("sp-1", "Special-1 (New)", 2020, 2024)),
            features = listOf("Feature@1", "Feature#2")
        )

        // Then
        assertTrue("Should handle special chars in name", 
                   specialBrand.brandName.contains("&"))
        assertTrue("Should handle special chars in series",
                   specialBrand.series.first().name.contains("("))
        assertTrue("Should handle special chars in features",
                   specialBrand.features.any { it.contains("@") })
    }
}