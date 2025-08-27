package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Unit tests for CreateVehicleUseCase testing business logic validation.
 * 
 * Tests cover:
 * - Input validation (brand, series, year ranges)
 * - Repository interaction
 * - Error handling and edge cases
 * - Business rule enforcement
 */
class CreateVehicleUseCaseTest {

    @Mock
    private lateinit var vehicleRepo: VehicleRepo

    private lateinit var useCase: CreateVehicleUseCase

    private val validVehicle = Vehicle(
        id = 0L,
        brand = "BMW",
        series = "X5",
        year = 2020,
        fuel = FuelType.Diesel
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = CreateVehicleUseCase(vehicleRepo)
    }

    @Test
    fun `invoke with valid vehicle should call repository`() = runTest {
        // Given
        whenever(vehicleRepo.createVehicle(validVehicle)).thenReturn(Result.Success(1L))

        // When
        val result = useCase(validVehicle)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        assertEquals(1L, (result as Result.Success).data)
    }

    @Test
    fun `invoke should reject blank brand`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(brand = "")

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should reject blank series`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(series = "")

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should reject whitespace-only brand`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(brand = "   ")

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should reject whitespace-only series`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(series = "   ")

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should reject year too old`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(year = 1979)

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should reject year too future`() = runTest {
        // Given
        val invalidVehicle = validVehicle.copy(year = 2051)

        // When & Then
        try {
            useCase(invalidVehicle)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should accept minimum valid year`() = runTest {
        // Given
        val validMinYear = validVehicle.copy(year = 1980)
        whenever(vehicleRepo.createVehicle(validMinYear)).thenReturn(Result.Success(1L))

        // When
        val result = useCase(validMinYear)

        // Then
        assertTrue("Should be Success", result is Result.Success)
    }

    @Test
    fun `invoke should accept maximum valid year`() = runTest {
        // Given
        val validMaxYear = validVehicle.copy(year = 2050)
        whenever(vehicleRepo.createVehicle(validMaxYear)).thenReturn(Result.Success(1L))

        // When
        val result = useCase(validMaxYear)

        // Then
        assertTrue("Should be Success", result is Result.Success)
    }

    @Test
    fun `invoke should pass through repository failures`() = runTest {
        // Given
        val error = DataError.Local.DISK_FULL
        whenever(vehicleRepo.createVehicle(validVehicle)).thenReturn(Result.Failure(error))

        // When
        val result = useCase(validVehicle)

        // Then
        assertTrue("Should be Failure", result is Result.Failure)
        assertEquals(error, (result as Result.Failure).error)
    }

    @Test
    fun `invoke should accept all fuel types`() = runTest {
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
            val vehicle = validVehicle.copy(fuel = fuelType)
            whenever(vehicleRepo.createVehicle(vehicle)).thenReturn(Result.Success(1L))
            
            val result = useCase(vehicle)
            assertTrue("Should accept $fuelType", result is Result.Success)
        }
    }

    @Test
    fun `invoke should validate with different realistic vehicles`() = runTest {
        // Given
        val vehicles = listOf(
            Vehicle(0L, "BMW", "X5", 2020, FuelType.Diesel),
            Vehicle(0L, "Audi", "A4", 2019, FuelType.Gasoline),
            Vehicle(0L, "Mercedes", "C-Class", 2021, FuelType.Hybrid),
            Vehicle(0L, "Tesla", "Model 3", 2022, FuelType.Electric),
            Vehicle(0L, "Volkswagen", "Golf", 2018, FuelType.Other)
        )

        // When & Then
        vehicles.forEach { vehicle ->
            whenever(vehicleRepo.createVehicle(vehicle)).thenReturn(Result.Success(1L))
            
            val result = useCase(vehicle)
            assertTrue("Should accept ${vehicle.brand} ${vehicle.series}", 
                       result is Result.Success)
        }
    }

    @Test
    fun `invoke should handle edge case years correctly`() = runTest {
        // Given
        val edgeCaseYears = listOf(1980, 1981, 2049, 2050)

        // When & Then
        edgeCaseYears.forEach { year ->
            val vehicle = validVehicle.copy(year = year)
            whenever(vehicleRepo.createVehicle(vehicle)).thenReturn(Result.Success(1L))
            
            val result = useCase(vehicle)
            assertTrue("Should accept year $year", result is Result.Success)
        }
    }
}