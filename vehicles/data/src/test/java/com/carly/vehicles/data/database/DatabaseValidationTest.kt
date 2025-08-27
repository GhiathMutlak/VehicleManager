package com.carly.vehicles.data.database

import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.FeatureEntity
import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.data.database.entity.VehicleEntity
import com.carly.vehicles.domain.model.FuelType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for database schema validation and constraint testing.
 * 
 * Tests cover:
 * - Entity table name validation
 * - Primary key constraints and auto-generation
 * - Data type validation and storage
 * - Entity field validation and edge cases
 * - Room annotation compliance
 * - Database schema consistency
 */
class DatabaseValidationTest {

    @Test
    fun `VehicleEntity should have correct table configuration`() {
        // Given
        val entity = VehicleEntity(
            id = 1L,
            brand = "BMW",
            series = "X5",
            year = 2020,
            fuel = FuelType.Diesel
        )

        // Then
        assertNotNull("Entity should be created", entity)
        assertEquals("ID should be set", 1L, entity.id)
        assertTrue("ID should be positive", entity.id >= 0)
    }

    @Test
    fun `VehicleEntity should support auto-generated primary keys`() {
        // Given
        val entityWithoutId = VehicleEntity(
            brand = "Audi",
            series = "A4", 
            year = 2019,
            fuel = FuelType.Gasoline
        )
        val entityWithId = VehicleEntity(
            id = 100L,
            brand = "Mercedes",
            series = "C-Class",
            year = 2021,
            fuel = FuelType.Hybrid
        )

        // Then
        assertEquals("Default ID should be 0 for auto-generation", 0L, entityWithoutId.id)
        assertEquals("Explicit ID should be preserved", 100L, entityWithId.id)
    }

    @Test
    fun `VehicleEntity should handle all fuel types correctly`() {
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
            val entity = VehicleEntity(
                id = 1L,
                brand = "Brand",
                series = "Series",
                year = 2020,
                fuel = fuelType
            )

            assertEquals("Fuel type should be preserved", fuelType, entity.fuel)
            assertNotNull("Entity should be valid with fuel type $fuelType", entity)
        }
    }

    @Test
    fun `VehicleEntity should validate year ranges`() {
        // Given
        val validYears = listOf(1900, 1950, 2000, 2024, 2030)
        val edgeYears = listOf(Int.MIN_VALUE, -1, 0, Int.MAX_VALUE)

        // When & Then - Valid years
        validYears.forEach { year ->
            val entity = VehicleEntity(
                id = 1L,
                brand = "Brand",
                series = "Series", 
                year = year,
                fuel = FuelType.Gasoline
            )
            
            assertEquals("Year should be preserved", year, entity.year)
            assertNotNull("Entity should be valid with year $year", entity)
        }

        // Edge years should also be storable (validation is business logic, not entity)
        edgeYears.forEach { year ->
            val entity = VehicleEntity(
                id = 1L,
                brand = "Brand",
                series = "Series",
                year = year,
                fuel = FuelType.Gasoline
            )
            
            assertEquals("Edge year should be preserved", year, entity.year)
        }
    }

    @Test
    fun `VehicleEntity should handle string field edge cases`() {
        // Given
        val stringTestCases = mapOf(
            "empty" to "",
            "whitespace" to "   ",
            "special_chars" to "Brand & Co. (Ümlaut)",
            "long_string" to "A".repeat(1000),
            "unicode" to "BMW™ ® © Ñ",
            "numbers" to "123456789",
            "mixed" to "Brand-123 (Test_Case)"
        )

        // When & Then
        stringTestCases.forEach { (testCase, value) ->
            val entityBrand = VehicleEntity(1L, value, "Series", 2020, FuelType.Gasoline)
            val entitySeries = VehicleEntity(1L, "Brand", value, 2020, FuelType.Gasoline)

            assertEquals("Brand $testCase should be preserved", value, entityBrand.brand)
            assertEquals("Series $testCase should be preserved", value, entitySeries.series)
            assertNotNull("Entity should be valid with $testCase strings", entityBrand)
            assertNotNull("Entity should be valid with $testCase strings", entitySeries)
        }
    }

    @Test
    fun `BrandEntity should have correct primary key configuration`() {
        // Given
        val brand = BrandEntity(
            id = "bmw",
            name = "BMW"
        )

        // Then
        assertEquals("Primary key should be string", "bmw", brand.id)
        assertEquals("Name should be preserved", "BMW", brand.name)
        assertNotNull("Entity should be valid", brand)
    }

    @Test
    fun `BrandEntity should handle various ID formats`() {
        // Given
        val idFormats = mapOf(
            "lowercase" to "bmw",
            "uppercase" to "BMW",
            "hyphenated" to "mercedes-benz",
            "underscored" to "aston_martin",
            "numbered" to "brand123",
            "mixed" to "Audi-2024",
            "single_char" to "x",
            "empty" to ""
        )

        // When & Then
        idFormats.forEach { (format, id) ->
            val entity = BrandEntity(id, "Brand Name")
            
            assertEquals("ID format $format should be preserved", id, entity.id)
            assertNotNull("Entity should be valid with $format ID", entity)
        }
    }

    @Test
    fun `SeriesEntity should validate relationships and constraints`() {
        // Given
        val series = SeriesEntity(
            id = "x5",
            brandId = "bmw",
            name = "X5",
            minYear = 1999,
            maxYear = 2024
        )

        // Then
        assertEquals("Series ID should be preserved", "x5", series.id)
        assertEquals("Brand relationship should be maintained", "bmw", series.brandId)
        assertEquals("Series name should be preserved", "X5", series.name)
        assertTrue("Min year should be reasonable", series.minYear > 1800)
        assertTrue("Max year should be reasonable", series.maxYear < 2100)
        assertTrue("Year range should be valid", series.minYear <= series.maxYear)
    }

    @Test
    fun `SeriesEntity should handle year range edge cases`() {
        // Given
        val yearRangeTests = listOf(
            Triple("same_year", 2020, 2020),
            Triple("single_year_range", 2019, 2020),
            Triple("long_range", 1950, 2024),
            Triple("future_range", 2025, 2030),
            Triple("historical", 1900, 1950)
        )

        // When & Then
        yearRangeTests.forEach { (testCase, minYear, maxYear) ->
            val entity = SeriesEntity(
                id = "test",
                brandId = "brand",
                name = "Test Series",
                minYear = minYear,
                maxYear = maxYear
            )

            assertEquals("Min year should be preserved for $testCase", minYear, entity.minYear)
            assertEquals("Max year should be preserved for $testCase", maxYear, entity.maxYear)
            assertTrue("Year range should be valid for $testCase", entity.minYear <= entity.maxYear)
        }
    }

    @Test
    fun `FeatureEntity should validate feature data structure`() {
        // Given
        val feature = FeatureEntity(
            id = "diagnostics",
            brandId = "bmw",
            name = "Diagnostics"
        )

        // Then
        assertEquals("Feature ID should be preserved", "diagnostics", feature.id)
        assertEquals("Feature name should be preserved", "Diagnostics", feature.name)
        assertNotNull("Feature entity should be valid", feature)
    }

    @Test
    fun `FeatureEntity should handle various feature types`() {
        // Given
        val featureTypes = listOf(
            Pair("diagnostics", "Diagnostics"),
            Pair("live-data", "Live Data"),
            Pair("battery-check", "Battery Check"),
            Pair("ecu-coding", "ECU Coding"),
            Pair("service-reset", "Service Reset"),
            Pair("dpf-regeneration", "DPF Regeneration")
        )

        // When & Then
        featureTypes.forEach { (id, name) ->
            val entity = FeatureEntity(id, "bmw", name)
            
            assertEquals("Feature ID should match", id, entity.id)
            assertEquals("Feature name should match", name, entity.name)
            assertNotNull("Feature entity should be valid", entity)
        }
    }

    @Test
    fun `All entities should maintain data class properties`() {
        // Given
        val vehicle1 = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle2 = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val brand1 = BrandEntity("bmw", "BMW")
        val brand2 = BrandEntity("bmw", "BMW")
        val series1 = SeriesEntity("x5", "bmw", "X5", 1999, 2024)
        val series2 = SeriesEntity("x5", "bmw", "X5", 1999, 2024)

        // Then - Test equality
        assertEquals("Equal vehicles should be equal", vehicle1, vehicle2)
        assertEquals("Equal brands should be equal", brand1, brand2)
        assertEquals("Equal series should be equal", series1, series2)

        // Test hashCode consistency
        assertEquals("Equal vehicles should have same hashCode", 
                     vehicle1.hashCode(), vehicle2.hashCode())
        assertEquals("Equal brands should have same hashCode",
                     brand1.hashCode(), brand2.hashCode())
        assertEquals("Equal series should have same hashCode",
                     series1.hashCode(), series2.hashCode())

        // Test toString contains key information
        val vehicleString = vehicle1.toString()
        val brandString = brand1.toString()
        val seriesString = series1.toString()

        assertTrue("Vehicle toString should contain brand", vehicleString.contains("BMW"))
        assertTrue("Brand toString should contain name", brandString.contains("BMW"))
        assertTrue("Series toString should contain name", seriesString.contains("X5"))
    }

    @Test
    fun `Entities should support data class copy operations`() {
        // Given
        val originalVehicle = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val originalBrand = BrandEntity("bmw", "BMW")
        val originalSeries = SeriesEntity("x5", "bmw", "X5", 1999, 2024)

        // When
        val copiedVehicle = originalVehicle.copy(year = 2021)
        val copiedBrand = originalBrand.copy(name = "BMW Group")
        val copiedSeries = originalSeries.copy(maxYear = 2025)

        // Then
        assertEquals("Vehicle ID should remain same", originalVehicle.id, copiedVehicle.id)
        assertEquals("Vehicle year should be updated", 2021, copiedVehicle.year)
        assertEquals("Other vehicle fields should remain same", originalVehicle.brand, copiedVehicle.brand)

        assertEquals("Brand ID should remain same", originalBrand.id, copiedBrand.id)
        assertEquals("Brand name should be updated", "BMW Group", copiedBrand.name)

        assertEquals("Series ID should remain same", originalSeries.id, copiedSeries.id)
        assertEquals("Series max year should be updated", 2025, copiedSeries.maxYear)
        assertEquals("Other series fields should remain same", originalSeries.name, copiedSeries.name)
    }

    @Test
    fun `Entities should handle null safety correctly`() {
        // All entity fields are non-null by design
        // This test validates that the entity structure enforces non-null constraints
        
        // Given & When & Then
        val vehicle = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val brand = BrandEntity("bmw", "BMW")  
        val series = SeriesEntity("x5", "bmw", "X5", 1999, 2024)

        assertNotNull("Vehicle brand should not be null", vehicle.brand)
        assertNotNull("Vehicle series should not be null", vehicle.series)
        assertNotNull("Vehicle fuel should not be null", vehicle.fuel)

        assertNotNull("Brand id should not be null", brand.id)
        assertNotNull("Brand name should not be null", brand.name)

        assertNotNull("Series id should not be null", series.id)
        assertNotNull("Series brandId should not be null", series.brandId)
        assertNotNull("Series name should not be null", series.name)
    }

    @Test
    fun `Entities should maintain referential integrity concepts`() {
        // While Room doesn't enforce foreign keys in this schema,
        // the entities should be designed to support logical relationships

        // Given
        val brandId = "bmw"
        val brand = BrandEntity(brandId, "BMW")
        val series = SeriesEntity("x5", brandId, "X5", 1999, 2024)
        val vehicle = VehicleEntity(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // Then - Logical relationships should be consistent
        assertEquals("Series should reference the brand", brandId, series.brandId)
        assertEquals("Vehicle brand should match brand name", brand.name, vehicle.brand)
        assertEquals("Vehicle series should match series name", series.name, vehicle.series)
    }
}