package com.carly.vehicles.data.repo

import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Simple unit tests for data layer models and mapping functions.
 * 
 * These tests verify basic functionality without Android dependencies.
 * For more comprehensive testing, instrumented tests would be needed.
 */
class VehicleDataTest {

    @Test
    fun `Vehicle model should have correct properties`() {
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
    fun `FuelType sealed class should have expected types`() {
        // Given
        val diesel = FuelType.Diesel
        val gasoline = FuelType.Gasoline
        val hybrid = FuelType.Hybrid
        val electric = FuelType.Electric

        // Then
        assertEquals("Diesel", diesel.name)
        assertEquals("Gasoline", gasoline.name)
        assertEquals("Hybrid", hybrid.name)
        assertEquals("Electric", electric.name)
    }

    @Test
    fun `Vehicle equality should work correctly`() {
        // Given
        val vehicle1 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle2 = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)
        val vehicle3 = Vehicle(2L, "Audi", "A4", 2019, FuelType.Gasoline)

        // Then
        assertEquals(vehicle1, vehicle2)
        assertNotEquals(vehicle1, vehicle3)
    }

    @Test
    fun `Vehicle toString should be readable`() {
        // Given
        val vehicle = Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel)

        // When
        val toString = vehicle.toString()

        // Then
        assertTrue(toString.contains("BMW"))
        assertTrue(toString.contains("X5"))
        assertTrue(toString.contains("2020"))
        assertTrue(toString.contains("Diesel"))
    }
}