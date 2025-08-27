package com.carly.vehicles.data.converter

import com.carly.vehicles.data.database.converter.FuelTypeConverter
import com.carly.vehicles.domain.model.FuelType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for Room TypeConverters testing database serialization.
 * 
 * Tests cover:
 * - FuelType enum to String conversion
 * - String to FuelType enum conversion
 * - Bidirectional conversion consistency
 * - Edge cases and unknown values
 * - All supported fuel types
 */
class TypeConvertersTest {

    private lateinit var fuelTypeConverter: FuelTypeConverter

    @Before
    fun setUp() {
        fuelTypeConverter = FuelTypeConverter()
    }

    @Test
    fun `fromFuelType should convert Diesel to string`() {
        // When
        val result = fuelTypeConverter.fromFuelType(FuelType.Diesel)

        // Then
        assertEquals("Diesel", result)
    }

    @Test
    fun `fromFuelType should convert Gasoline to string`() {
        // When
        val result = fuelTypeConverter.fromFuelType(FuelType.Gasoline)

        // Then
        assertEquals("Gasoline", result)
    }

    @Test
    fun `fromFuelType should convert Hybrid to string`() {
        // When
        val result = fuelTypeConverter.fromFuelType(FuelType.Hybrid)

        // Then
        assertEquals("Hybrid", result)
    }

    @Test
    fun `fromFuelType should convert Electric to string`() {
        // When
        val result = fuelTypeConverter.fromFuelType(FuelType.Electric)

        // Then
        assertEquals("Electric", result)
    }

    @Test
    fun `fromFuelType should convert Other to string`() {
        // When
        val result = fuelTypeConverter.fromFuelType(FuelType.Other)

        // Then
        assertEquals("Other", result)
    }

    @Test
    fun `toFuelType should convert Diesel string to enum`() {
        // When
        val result = fuelTypeConverter.toFuelType("Diesel")

        // Then
        assertEquals(FuelType.Diesel, result)
    }

    @Test
    fun `toFuelType should convert Gasoline string to enum`() {
        // When
        val result = fuelTypeConverter.toFuelType("Gasoline")

        // Then
        assertEquals(FuelType.Gasoline, result)
    }

    @Test
    fun `toFuelType should convert Hybrid string to enum`() {
        // When
        val result = fuelTypeConverter.toFuelType("Hybrid")

        // Then
        assertEquals(FuelType.Hybrid, result)
    }

    @Test
    fun `toFuelType should convert Electric string to enum`() {
        // When
        val result = fuelTypeConverter.toFuelType("Electric")

        // Then
        assertEquals(FuelType.Electric, result)
    }

    @Test
    fun `toFuelType should convert Other string to enum`() {
        // When
        val result = fuelTypeConverter.toFuelType("Other")

        // Then
        assertEquals(FuelType.Other, result)
    }

    @Test
    fun `toFuelType should handle unknown string as Other`() {
        // When
        val result = fuelTypeConverter.toFuelType("UnknownFuelType")

        // Then
        assertEquals(FuelType.Other, result)
    }

    @Test
    fun `toFuelType should handle empty string as Other`() {
        // When
        val result = fuelTypeConverter.toFuelType("")

        // Then
        assertEquals(FuelType.Other, result)
    }

    @Test
    fun `toFuelType should handle null-like string as Other`() {
        // When
        val result = fuelTypeConverter.toFuelType("null")

        // Then
        assertEquals(FuelType.Other, result)
    }

    @Test
    fun `conversion should be bidirectional for all fuel types`() {
        // Given
        val allFuelTypes = listOf(
            FuelType.Diesel,
            FuelType.Gasoline,
            FuelType.Hybrid,
            FuelType.Electric,
            FuelType.Other
        )

        // When & Then
        allFuelTypes.forEach { originalFuelType ->
            val stringValue = fuelTypeConverter.fromFuelType(originalFuelType)
            val convertedBack = fuelTypeConverter.toFuelType(stringValue)
            
            assertEquals("Conversion should be bidirectional for $originalFuelType",
                         originalFuelType, convertedBack)
        }
    }

    @Test
    fun `fromFuelType should use fuel type name property`() {
        // Given
        val allFuelTypes = listOf(
            FuelType.Diesel,
            FuelType.Gasoline,
            FuelType.Hybrid,
            FuelType.Electric,
            FuelType.Other
        )

        // When & Then
        allFuelTypes.forEach { fuelType ->
            val result = fuelTypeConverter.fromFuelType(fuelType)
            assertEquals("Should use fuel type name property", fuelType.name, result)
        }
    }

    @Test
    fun `toFuelType should handle case sensitivity correctly`() {
        // Given
        val testCases = mapOf(
            "diesel" to FuelType.Other,  // lowercase should default to Other
            "DIESEL" to FuelType.Other,  // uppercase should default to Other
            "DiEsEl" to FuelType.Other   // mixed case should default to Other
        )

        // When & Then
        testCases.forEach { (input, expected) ->
            val result = fuelTypeConverter.toFuelType(input)
            assertEquals("Case sensitivity test for '$input'", expected, result)
        }
    }

    @Test
    fun `conversion should handle repeated operations consistently`() {
        // Given
        val fuelType = FuelType.Hybrid

        // When
        val string1 = fuelTypeConverter.fromFuelType(fuelType)
        val string2 = fuelTypeConverter.fromFuelType(fuelType)
        val back1 = fuelTypeConverter.toFuelType(string1)
        val back2 = fuelTypeConverter.toFuelType(string2)

        // Then
        assertEquals("String conversions should be consistent", string1, string2)
        assertEquals("Back conversions should be consistent", back1, back2)
        assertEquals("Should equal original", fuelType, back1)
        assertEquals("Should equal original", fuelType, back2)
    }

    @Test
    fun `converter should handle edge case strings gracefully`() {
        // Given
        val edgeCases = listOf(
            "   ",      // whitespace
            "\t\n",     // tabs and newlines
            "123",      // numbers
            "!@#$",     // special characters
            "Dieselxxx" // partial match
        )

        // When & Then
        edgeCases.forEach { edgeCase ->
            val result = fuelTypeConverter.toFuelType(edgeCase)
            assertEquals("Edge case '$edgeCase' should default to Other", 
                         FuelType.Other, result)
        }
    }
}