package com.carly.vehicles.domain.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for domain models testing core business objects.
 * 
 * Tests cover:
 * - Model creation and property validation
 * - Equality and hash code behavior
 * - Business rule validation (year ranges, etc.)
 * - FuelType sealed class behavior
 */
class DomainModelsTest {

    @Test
    fun `Vehicle should be created with all required properties`() {
        // Given
        val vehicle = Vehicle(
            id = 1L,
            brand = "BMW",
            series = "X5",
            year = 2020,
            fuel = FuelType.Diesel
        )

        // Then
        assertEquals(1L, vehicle.id)
        assertEquals("BMW", vehicle.brand)
        assertEquals("X5", vehicle.series)
        assertEquals(2020, vehicle.year)
        assertEquals(FuelType.Diesel, vehicle.fuel)
    }

    @Test
    fun `Vehicle equality should work correctly`() {
        // Given
        val vehicle1 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle2 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle3 = Vehicle(2L, "Audi", "A4", 2019, FuelType.Gasoline)

        // Then
        assertEquals(vehicle1, vehicle2)
        assertEquals(vehicle1.hashCode(), vehicle2.hashCode())
        assertNotEquals(vehicle1, vehicle3)
    }

    @Test
    fun `Vehicle copy should work correctly`() {
        // Given
        val original = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // When
        val copied = original.copy(year = 2021, fuel = FuelType.Hybrid)

        // Then
        assertEquals(1L, copied.id)
        assertEquals("BMW", copied.brand)
        assertEquals("X5", copied.series)
        assertEquals(2021, copied.year) // Changed
        assertEquals(FuelType.Hybrid, copied.fuel) // Changed
    }

    @Test
    fun `Brand should be created with id and name`() {
        // Given
        val brand = Brand(id = "bmw", name = "BMW")

        // Then
        assertEquals("bmw", brand.id)
        assertEquals("BMW", brand.name)
    }

    @Test
    fun `Brand equality should work correctly`() {
        // Given
        val brand1 = Brand("bmw", "BMW")
        val brand2 = Brand("bmw", "BMW")
        val brand3 = Brand("audi", "Audi")

        // Then
        assertEquals(brand1, brand2)
        assertEquals(brand1.hashCode(), brand2.hashCode())
        assertNotEquals(brand1, brand3)
    }

    @Test
    fun `Series should be created with all properties`() {
        // Given
        val series = Series(
            id = "x5",
            brandId = "bmw",
            name = "X5",
            minYear = 1999,
            maxYear = 2024
        )

        // Then
        assertEquals("x5", series.id)
        assertEquals("bmw", series.brandId)
        assertEquals("X5", series.name)
        assertEquals(1999, series.minYear)
        assertEquals(2024, series.maxYear)
    }

    @Test
    fun `Series should validate year range`() {
        // Given
        val series = Series("x5", "bmw", "X5", 1999, 2024)

        // Then
        assertTrue("Min year should be less than max year", series.minYear < series.maxYear)
        assertTrue("Year range should be reasonable", series.maxYear - series.minYear < 50)
    }

    @Test
    fun `Series equality should work correctly`() {
        // Given
        val series1 = Series("x5", "bmw", "X5", 1999, 2024)
        val series2 = Series("x5", "bmw", "X5", 1999, 2024)
        val series3 = Series("a4", "audi", "A4", 1994, 2024)

        // Then
        assertEquals(series1, series2)
        assertEquals(series1.hashCode(), series2.hashCode())
        assertNotEquals(series1, series3)
    }

    @Test
    fun `FuelType should have correct names`() {
        // Then
        assertEquals("Diesel", FuelType.Diesel.name)
        assertEquals("Gasoline", FuelType.Gasoline.name)
        assertEquals("Hybrid", FuelType.Hybrid.name)
        assertEquals("Electric", FuelType.Electric.name)
        assertEquals("Other", FuelType.Other.name)
    }

    @Test
    fun `FuelType should support equality comparison`() {
        // Given
        val diesel1 = FuelType.Diesel
        val diesel2 = FuelType.Diesel
        val gasoline = FuelType.Gasoline

        // Then
        assertEquals(diesel1, diesel2)
        assertNotEquals(diesel1, gasoline)
    }

    @Test
    fun `FuelType should be sealed class with finite options`() {
        // Given - All possible fuel types
        val allFuelTypes = listOf(
            FuelType.Diesel,
            FuelType.Gasoline,
            FuelType.Hybrid,
            FuelType.Electric,
            FuelType.Other
        )

        // Then - Verify we can pattern match on all types
        allFuelTypes.forEach { fuelType ->
            val result = when (fuelType) {
                is FuelType.Diesel -> "diesel"
                is FuelType.Gasoline -> "gasoline"
                is FuelType.Hybrid -> "hybrid"
                is FuelType.Electric -> "electric"
                is FuelType.Other -> "other"
            }
            assertNotNull("Should be able to match all fuel types", result)
        }
    }

    @Test
    fun `Vehicle toString should be readable`() {
        // Given
        val vehicle = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // When
        val toString = vehicle.toString()

        // Then
        assertTrue("Should contain brand", toString.contains("BMW"))
        assertTrue("Should contain series", toString.contains("X5"))
        assertTrue("Should contain year", toString.contains("2020"))
        assertTrue("Should contain fuel type", toString.contains("Diesel"))
    }

    @Test
    fun `Series should support realistic year ranges`() {
        // Given
        val modernSeries = Series("x5", "bmw", "X5", 1999, 2024)
        val classicSeries = Series("e30", "bmw", "3 Series E30", 1982, 1994)
        val futureSeries = Series("ix", "bmw", "iX", 2021, 2030)

        // Then
        assertTrue("Modern series should have reasonable range", 
                   modernSeries.maxYear - modernSeries.minYear in 10..50)
        assertTrue("Classic series should have historical range",
                   classicSeries.maxYear < 2000)
        assertTrue("Future series should have forward-looking range",
                   futureSeries.minYear > 2020)
    }

    @Test
    fun `Brand and Series should have consistent relationship`() {
        // Given
        val brand = Brand("bmw", "BMW")
        val series = Series("x5", "bmw", "X5", 1999, 2024)

        // Then
        assertEquals("Series brand ID should match Brand ID", 
                     brand.id, series.brandId)
    }
}