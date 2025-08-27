package com.carly.vehicles.data.mapper

import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.data.database.entity.VehicleEntity
import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Series
import com.carly.vehicles.domain.model.Vehicle
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for data layer mappers testing domain-entity transformations.
 * 
 * Tests cover:
 * - Bidirectional mapping between domain models and database entities
 * - Property preservation during transformation
 * - Edge cases and data consistency
 * - Mapping accuracy and completeness
 */
class DataMappersTest {

    @Test
    fun `VehicleEntity to Vehicle mapping should preserve all properties`() {
        // Given
        val entity = VehicleEntity(
            id = 1L,
            brand = "BMW",
            series = "X5",
            year = 2020,
            fuel = FuelType.Diesel
        )

        // When
        val domain = entity.toVehicle()

        // Then
        assertEquals(1L, domain.id)
        assertEquals("BMW", domain.brand)
        assertEquals("X5", domain.series)
        assertEquals(2020, domain.year)
        assertEquals(FuelType.Diesel, domain.fuel)
    }

    @Test
    fun `Vehicle to VehicleEntity mapping should preserve all properties`() {
        // Given
        val domain = Vehicle(
            id = 2L,
            brand = "Audi",
            series = "A4",
            year = 2019,
            fuel = FuelType.Gasoline
        )

        // When
        val entity = domain.toVehicleEntity()

        // Then
        assertEquals(2L, entity.id)
        assertEquals("Audi", entity.brand)
        assertEquals("A4", entity.series)
        assertEquals(2019, entity.year)
        assertEquals(FuelType.Gasoline, entity.fuel)
    }

    @Test
    fun `Vehicle mapping should be bidirectional`() {
        // Given
        val originalVehicle = Vehicle(
            id = 123L,
            brand = "Mercedes",
            series = "C-Class",
            year = 2021,
            fuel = FuelType.Hybrid
        )

        // When
        val entity = originalVehicle.toVehicleEntity()
        val mappedBack = entity.toVehicle()

        // Then
        assertEquals(originalVehicle, mappedBack)
    }

    @Test
    fun `VehicleEntity mapping should be bidirectional`() {
        // Given
        val originalEntity = VehicleEntity(
            id = 456L,
            brand = "Tesla",
            series = "Model 3",
            year = 2022,
            fuel = FuelType.Electric
        )

        // When
        val domain = originalEntity.toVehicle()
        val mappedBack = domain.toVehicleEntity()

        // Then
        assertEquals(originalEntity, mappedBack)
    }

    @Test
    fun `BrandEntity to Brand mapping should preserve all properties`() {
        // Given
        val entity = BrandEntity(
            id = "bmw",
            name = "BMW"
        )

        // When
        val domain = entity.toBrand()

        // Then
        assertEquals("bmw", domain.id)
        assertEquals("BMW", domain.name)
    }

    @Test
    fun `Brand to BrandEntity mapping should preserve all properties`() {
        // Given
        val domain = Brand(
            id = "audi",
            name = "Audi"
        )

        // When
        val entity = domain.toBrandEntity()

        // Then
        assertEquals("audi", entity.id)
        assertEquals("Audi", entity.name)
    }

    @Test
    fun `Brand mapping should be bidirectional`() {
        // Given
        val originalBrand = Brand(
            id = "mercedes",
            name = "Mercedes-Benz"
        )

        // When
        val entity = originalBrand.toBrandEntity()
        val mappedBack = entity.toBrand()

        // Then
        assertEquals(originalBrand, mappedBack)
    }

    @Test
    fun `SeriesEntity to Series mapping should preserve all properties`() {
        // Given
        val entity = SeriesEntity(
            id = "x5",
            brandId = "bmw",
            name = "X5",
            minYear = 1999,
            maxYear = 2024
        )

        // When
        val domain = entity.toSeries()

        // Then
        assertEquals("x5", domain.id)
        assertEquals("bmw", domain.brandId)
        assertEquals("X5", domain.name)
        assertEquals(1999, domain.minYear)
        assertEquals(2024, domain.maxYear)
    }

    @Test
    fun `Series to SeriesEntity mapping should preserve all properties`() {
        // Given
        val domain = Series(
            id = "a4",
            brandId = "audi",
            name = "A4",
            minYear = 1994,
            maxYear = 2024
        )

        // When
        val entity = domain.toSeriesEntity()

        // Then
        assertEquals("a4", entity.id)
        assertEquals("audi", entity.brandId)
        assertEquals("A4", entity.name)
        assertEquals(1994, entity.minYear)
        assertEquals(2024, entity.maxYear)
    }

    @Test
    fun `Series mapping should be bidirectional`() {
        // Given
        val originalSeries = Series(
            id = "c-class",
            brandId = "mercedes",
            name = "C-Class",
            minYear = 1993,
            maxYear = 2024
        )

        // When
        val entity = originalSeries.toSeriesEntity()
        val mappedBack = entity.toSeries()

        // Then
        assertEquals(originalSeries, mappedBack)
    }

    @Test
    fun `Vehicle mapping should handle all fuel types correctly`() {
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
            val vehicle = Vehicle(1L, "Brand", "Series", 2020, fuelType)
            val entity = vehicle.toVehicleEntity()
            val mappedBack = entity.toVehicle()

            assertEquals("Fuel type $fuelType should be preserved", fuelType, mappedBack.fuel)
        }
    }

    @Test
    fun `Vehicle mapping should handle edge case values`() {
        // Given
        val edgeVehicle = Vehicle(
            id = 0L,
            brand = "",
            series = " ",
            year = 1980,
            fuel = FuelType.Other
        )

        // When
        val entity = edgeVehicle.toVehicleEntity()
        val mappedBack = entity.toVehicle()

        // Then
        assertEquals(edgeVehicle, mappedBack)
    }

    @Test
    fun `Brand mapping should handle special characters`() {
        // Given
        val brand = Brand(
            id = "mercedes-benz",
            name = "Mercedes-Benz & Co."
        )

        // When
        val entity = brand.toBrandEntity()
        val mappedBack = entity.toBrand()

        // Then
        assertEquals(brand, mappedBack)
    }

    @Test
    fun `Series mapping should handle year range edge cases`() {
        // Given
        val series = Series(
            id = "classic",
            brandId = "brand",
            name = "Classic Model",
            minYear = 1950,
            maxYear = 1960
        )

        // When
        val entity = series.toSeriesEntity()
        val mappedBack = entity.toSeries()

        // Then
        assertEquals(series, mappedBack)
    }

    @Test
    fun `Mapping should preserve data class equality semantics`() {
        // Given
        val vehicle1 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle2 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // When
        val entity1 = vehicle1.toVehicleEntity()
        val entity2 = vehicle2.toVehicleEntity()

        // Then
        assertEquals("Equal vehicles should map to equal entities", entity1, entity2)
        assertEquals("Equal entities should map to equal vehicles", 
                     entity1.toVehicle(), entity2.toVehicle())
    }

    @Test
    fun `Mapping should preserve hashCode consistency`() {
        // Given
        val vehicle = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // When
        val entity = vehicle.toVehicleEntity()
        val mappedBack = entity.toVehicle()

        // Then
        assertEquals("Original and mapped-back should have same hashCode",
                     vehicle.hashCode(), mappedBack.hashCode())
    }
}