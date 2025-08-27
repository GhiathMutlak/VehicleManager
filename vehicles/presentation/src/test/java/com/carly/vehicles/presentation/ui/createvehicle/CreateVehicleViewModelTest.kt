package com.carly.vehicles.presentation.ui.createvehicle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Series
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.usecase.CreateVehicleUseCase
import com.carly.vehicles.domain.usecase.SetSelectedVehicleUseCase
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Unit tests for CreateVehicleViewModel testing MVI architecture patterns and business logic.
 * 
 * Tests cover:
 * - State initialization and step navigation
 * - Loading and error handling for catalog data
 * - Search functionality and filtering
 * - Vehicle creation flow with validation
 * - Use case integration and error scenarios
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreateVehicleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogRepo = mockk<CatalogRepo>()
    private val createVehicleUseCase = mockk<CreateVehicleUseCase>()
    private val setSelectedVehicleUseCase = mockk<SetSelectedVehicleUseCase>()

    private val testDispatcher = StandardTestDispatcher()

    private val sampleBrands = listOf(
        Brand("bmw", "BMW"),
        Brand("audi", "Audi"),
        Brand("mercedes", "Mercedes-Benz")
    )

    private val sampleSeries = listOf(
        Series("x5", "bmw", "X5", 1999, 2024),
        Series("x3", "bmw", "X3", 2003, 2024),
        Series("3-series", "bmw", "3 Series", 1975, 2024)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init should load brands and set initial state`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(CreateVehicleStep.BrandSelection, state.currentStep)
        assertEquals(sampleBrands, state.brands)
        assertFalse(state.isLoading)
        assertTrue(state.series.isEmpty())
        assertTrue(state.availableYears.isEmpty())
        assertTrue(state.searchQuery.isEmpty())
    }

    @Test
    fun `init should handle brands loading failure`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Failure(DataError.Local.UNKNOWN)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertFalse("Should not be loading after error", state.isLoading)
        assertTrue("Brands should be empty on error", state.brands.isEmpty())
    }

    @Test
    fun `onAction UpdateSearchQuery should update search query`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onAction(CreateVehicleAction.UpdateSearchQuery("BMW"))
        
        val state = viewModel.state.value
        assertEquals("BMW", state.searchQuery)
    }

    @Test
    fun `onAction SelectBrand should navigate to series selection and load series`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        coEvery { catalogRepo.getSeries("bmw") } returns Result.Success(sampleSeries)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val brand = sampleBrands[0]
        viewModel.onAction(CreateVehicleAction.SelectBrand(brand))
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(CreateVehicleStep.SeriesSelection, state.currentStep)
        assertEquals(brand, state.selectedBrand)
        assertEquals(sampleSeries, state.series)
        assertTrue("Search query should be cleared", state.searchQuery.isEmpty())
    }

    @Test
    fun `onAction SelectSeries should navigate to year selection and generate years`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val series = sampleSeries[0] // X5: 1999-2024
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val expectedYears = (1999..minOf(2024, currentYear)).toList().reversed()
        
        viewModel.onAction(CreateVehicleAction.SelectSeries(series))
        
        val state = viewModel.state.value
        assertEquals(CreateVehicleStep.YearSelection, state.currentStep)
        assertEquals(series, state.selectedSeries)
        assertEquals(expectedYears, state.availableYears)
        assertTrue("Search query should be cleared", state.searchQuery.isEmpty())
    }

    @Test
    fun `onAction SelectYear should navigate to fuel type selection`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onAction(CreateVehicleAction.SelectYear(2020))
        
        val state = viewModel.state.value
        assertEquals(CreateVehicleStep.FuelTypeSelection, state.currentStep)
        assertEquals(2020, state.selectedYear)
        assertTrue("Search query should be cleared", state.searchQuery.isEmpty())
    }

    @Test
    fun `onAction SelectFuelType should create vehicle with complete state`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        coEvery { catalogRepo.getSeries("bmw") } returns Result.Success(sampleSeries)
        coEvery { createVehicleUseCase(any()) } returns Result.Success(123L)
        coEvery { setSelectedVehicleUseCase(123L) } returns Result.Success(Unit)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val brand = sampleBrands[0]
        val series = sampleSeries[0]
        val year = 2020
        val fuelType = FuelType.Diesel
        
        viewModel.onAction(CreateVehicleAction.SelectBrand(brand))
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onAction(CreateVehicleAction.SelectSeries(series))
        viewModel.onAction(CreateVehicleAction.SelectYear(year))
        viewModel.onAction(CreateVehicleAction.SelectFuelType(fuelType))
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(fuelType, state.selectedFuelType)
        assertFalse("Should not be loading after creation", state.isLoading)
        
        coVerify { createVehicleUseCase(any()) }
        coVerify { setSelectedVehicleUseCase(123L) }
    }

    @Test
    fun `vehicle creation should use correct data mapping`() = runTest {
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        coEvery { catalogRepo.getSeries("bmw") } returns Result.Success(sampleSeries)
        
        val brand = sampleBrands[0]
        val series = sampleSeries[0]
        val year = 2020
        val fuelType = FuelType.Electric
        val vehicleId = 789L
        
        var capturedVehicle: Vehicle? = null
        coEvery { createVehicleUseCase(any()) } answers {
            capturedVehicle = firstArg()
            Result.Success(vehicleId)
        }
        coEvery { setSelectedVehicleUseCase(vehicleId) } returns Result.Success(Unit)
        
        val viewModel = CreateVehicleViewModel(catalogRepo, createVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onAction(CreateVehicleAction.SelectBrand(brand))
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onAction(CreateVehicleAction.SelectSeries(series))
        viewModel.onAction(CreateVehicleAction.SelectYear(year))
        viewModel.onAction(CreateVehicleAction.SelectFuelType(fuelType))
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertNotNull("Should capture vehicle", capturedVehicle)
        assertEquals(0L, capturedVehicle!!.id) // Database will assign ID
        assertEquals(brand.name, capturedVehicle!!.brand)
        assertEquals(series.name, capturedVehicle!!.series)
        assertEquals(year, capturedVehicle!!.year)
        assertEquals(fuelType, capturedVehicle!!.fuel)
    }
}