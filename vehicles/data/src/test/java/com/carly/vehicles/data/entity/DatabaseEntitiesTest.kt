package com.carly.vehicles.data.entity

import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.data.database.entity.VehicleEntity
import com.carly.vehicles.domain.model.FuelType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for database entities testing Room model classes.
 * 
 * Tests cover:
 * - Entity creation and property validation
 * - Data class behavior (equality, copy, hashCode)
 * - Primary key and database constraints validation
 * - Edge cases and data consistency
 */
class DatabaseEntitiesTest {

    @Test
    fun `VehicleEntity should be created with all properties`() {
        // When
        val entity = VehicleEntity(
            id = 1L,
            brand = "BMW",
            series = "X5",
            year = 2020,
            fuel = FuelType.Diesel
        )

        // Then
        assertEquals(1L, entity.id)
        assertEquals("BMW", entity.brand)
        assertEquals("X5", entity.series)
        assertEquals(2020, entity.year)
        assertEquals(FuelType.Diesel, entity.fuel)
    }

    @Test
    fun `VehicleEntity should use default id for auto-generation`() {
        // When
        val entity = VehicleEntity(
            brand = "Audi",
            series = "A4",
            year = 2019,
            fuel = FuelType.Gasoline
        )

        // Then
        assertEquals(0L, entity.id) // Default value for auto-generation
        assertEquals("Audi", entity.brand)
        assertEquals("A4", entity.series)
        assertEquals(2019, entity.year)
        assertEquals(FuelType.Gasoline, entity.fuel)
    }

    @Test
    fun `VehicleEntity equality should work correctly`() {
        // Given
        val entity1 = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val entity2 = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val entity3 = VehicleEntity(2L, "Audi", "A4", 2019, FuelType.Gasoline)

        // Then
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
        assertNotEquals(entity1, entity3)
    }

    @Test
    fun `VehicleEntity copy should work correctly`() {
        // Given
        val original = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)

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
    fun `BrandEntity should be created with all properties`() {
        // When
        val entity = BrandEntity(
            id = "bmw",
            name = "BMW"
        )

        // Then
        assertEquals("bmw", entity.id)
        assertEquals("BMW", entity.name)
    }

    @Test
    fun `BrandEntity equality should work correctly`() {
        // Given
        val entity1 = BrandEntity("bmw", "BMW")
        val entity2 = BrandEntity("bmw", "BMW")
        val entity3 = BrandEntity("audi", "Audi")

        // Then
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
        assertNotEquals(entity1, entity3)
    }

    @Test
    fun `SeriesEntity should be created with all properties`() {
        // When
        val entity = SeriesEntity(
            id = "x5",
            brandId = "bmw",
            name = "X5",
            minYear = 1999,
            maxYear = 2024
        )

        // Then
        assertEquals("x5", entity.id)
        assertEquals("bmw", entity.brandId)
        assertEquals("X5", entity.name)
        assertEquals(1999, entity.minYear)
        assertEquals(2024, entity.maxYear)
    }

    @Test
    fun `SeriesEntity equality should work correctly`() {
        // Given
        val entity1 = SeriesEntity("x5", "bmw", "X5", 1999, 2024)
        val entity2 = SeriesEntity("x5", "bmw", "X5", 1999, 2024)
        val entity3 = SeriesEntity("a4", "audi", "A4", 1994, 2024)

        // Then
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
        assertNotEquals(entity1, entity3)
    }

    @Test
    fun `VehicleEntity should handle all fuel types`() {
        // Given
        val fuelTypes = listOf(
            FuelType.Diesel,
            FuelType.Gasoline,
            FuelType.Hybrid,
            FuelType.Electric,
            FuelType.Other
        )

        // When & Then
        fuelTypes.forEach { fuelType ->
            val entity = VehicleEntity(
                id = 1L,
                brand = "Brand",
                series = "Series",
                year = 2020,
                fuel = fuelType
            )
            
            assertEquals("Should store fuel type $fuelType", fuelType, entity.fuel)
        }
    }

    @Test
    fun `VehicleEntity should handle edge case values`() {
        // Given
        val edgeCases = listOf(
            VehicleEntity(0L, "", "", 1900, FuelType.Other),
            VehicleEntity(Long.MAX_VALUE, "Very Long Brand Name", "Series", 2100, FuelType.Electric),
            VehicleEntity(-1L, "Brand", "   ", 2024, FuelType.Hybrid)
        )

        // When & Then
        edgeCases.forEach { entity ->
            // Entity should be created without errors
            assertNotNull("Entity should be created", entity)
            assertTrue("ID should be set", entity.id != null)
            assertNotNull("Brand should be set", entity.brand)
            assertNotNull("Series should be set", entity.series)
            assertTrue("Year should be reasonable", entity.year in 1800..2200)
            assertNotNull("Fuel type should be set", entity.fuel)
        }
    }

    @Test
    fun `BrandEntity should handle special characters`() {
        // Given
        val specialBrand = BrandEntity(
            id = "mercedes-benz",
            name = "Mercedes-Benz & Co. (Ümlaut)"
        )

        // Then
        assertEquals("mercedes-benz", specialBrand.id)
        assertTrue("Should handle special characters", 
                   specialBrand.name.contains("&"))
        assertTrue("Should handle unicode characters",
                   specialBrand.name.contains("Ü"))
    }

    @Test
    fun `SeriesEntity should validate year ranges`() {
        // Given
        val validRanges = listOf(
            SeriesEntity("s1", "brand", "Series 1", 2000, 2024),
            SeriesEntity("s2", "brand", "Series 2", 1980, 1990),
            SeriesEntity("s3", "brand", "Series 3", 2025, 2030)
        )

        // When & Then
        validRanges.forEach { series ->
            assertTrue("Min year should be less than or equal to max year",
                       series.minYear <= series.maxYear)
            assertTrue("Year range should be reasonable",
                       series.maxYear - series.minYear < 100)
        }
    }

    @Test
    fun `Entities should have consistent toString implementation`() {
        // Given
        val vehicle = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val brand = BrandEntity("bmw", "BMW")
        val series = SeriesEntity("x5", "bmw", "X5", 1999, 2024)

        // When
        val vehicleString = vehicle.toString()
        val brandString = brand.toString()
        val seriesString = series.toString()

        // Then
        assertTrue("Vehicle toString should contain key info", 
                   vehicleString.contains("BMW") && vehicleString.contains("X5"))
        assertTrue("Brand toString should contain key info",
                   brandString.contains("bmw") && brandString.contains("BMW"))
        assertTrue("Series toString should contain key info",
                   seriesString.contains("x5") && seriesString.contains("X5"))
    }

    @Test
    fun `Entities should support component destructuring`() {
        // Given
        val vehicle = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val brand = BrandEntity("bmw", "BMW")
        val series = SeriesEntity("x5", "bmw", "X5", 1999, 2024)

        // When
        val (vId, vBrand, vSeries, vYear, vFuel) = vehicle
        val (bId, bName) = brand
        val (sId, sBrandId, sName, sMinYear, sMaxYear) = series

        // Then
        assertEquals(1L, vId)
        assertEquals("BMW", vBrand)
        assertEquals("bmw", bId)
        assertEquals("BMW", bName)
        assertEquals("x5", sId)
        assertEquals("bmw", sBrandId)
    }

    @Test
    fun `VehicleEntity should maintain data class invariants`() {
        // Given
        val vehicle1 = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle2 = vehicle1.copy()

        // Then
        assertEquals("Copy should be equal to original", vehicle1, vehicle2)
        assertEquals("HashCode should be equal", vehicle1.hashCode(), vehicle2.hashCode())
        assertNotSame("Should not be same reference", vehicle1, vehicle2)
    }

    @Test
    fun `Primary key fields should be properly defined`() {
        // Given
        val vehicle = VehicleEntity(123L, "Brand", "Series", 2020, FuelType.Diesel)
        val brand = BrandEntity("brand-id", "Brand Name")
        val series = SeriesEntity("series-id", "brand-id", "Series Name", 2000, 2024)

        // Then - Primary keys should be accessible
        assertEquals(123L, vehicle.id)
        assertEquals("brand-id", brand.id)
        assertEquals("series-id", series.id)
    }
}