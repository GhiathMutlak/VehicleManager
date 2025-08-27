package com.carly.vehicles.presentation.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for DashboardViewModel testing MVI architecture and reactive state management.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val vehicleSelectionRepository = mockk<VehicleSelectionRepository>()
    private val vehicleRepo = mockk<VehicleRepo>()
    private val catalogRepo = mockk<CatalogRepo>()

    private val testDispatcher = StandardTestDispatcher()

    private val sampleVehicle = Vehicle(
        id = 1L,
        brand = "BMW",
        series = "X5",
        year = 2020,
        fuel = FuelType.Diesel
    )

    private val sampleBrands = listOf(
        Brand("bmw", "BMW"),
        Brand("audi", "Audi")
    )

    private val sampleFeatures = setOf(
        Feature.fromId("diagnostics", "Diagnostics"),
        Feature.fromId("live-data", "Live Data"),
        Feature.fromId("coding", "Coding")
    )

    private val selectedVehicleIdFlow = MutableStateFlow<Long?>(null)
    private val vehiclesFlow = MutableStateFlow<List<Vehicle>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Setup default mock behavior
        every { vehicleSelectionRepository.selectedVehicleId } returns selectedVehicleIdFlow
        every { vehicleRepo.observeVehicles() } returns vehiclesFlow
        coEvery { catalogRepo.getBrands() } returns Result.Success(sampleBrands)
        coEvery { catalogRepo.getFeatures("bmw") } returns Result.Success(sampleFeatures)
    }

    @Test
    fun `init should load empty state when no vehicle selected`() = runTest {
        val viewModel = DashboardViewModel(vehicleSelectionRepository, vehicleRepo, catalogRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNull("No vehicle should be selected", state.selectedVehicle)
        assertTrue("Features should be empty", state.features.isEmpty())
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `should load selected vehicle with features`() = runTest {
        // Setup selected vehicle
        selectedVehicleIdFlow.value = 1L
        vehiclesFlow.value = listOf(sampleVehicle)

        val viewModel = DashboardViewModel(vehicleSelectionRepository, vehicleRepo, catalogRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Should load selected vehicle", sampleVehicle, state.selectedVehicle)
        assertEquals("Should load features", sampleFeatures, state.features)
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `should handle vehicle selection change`() = runTest {
        val viewModel = DashboardViewModel(vehicleSelectionRepository, vehicleRepo, catalogRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Add vehicle and select it
        vehiclesFlow.value = listOf(sampleVehicle)
        selectedVehicleIdFlow.value = 1L
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithVehicle = viewModel.state.value
        assertEquals("Should select vehicle", sampleVehicle, stateWithVehicle.selectedVehicle)
        assertEquals("Should load features", sampleFeatures, stateWithVehicle.features)

        // Deselect vehicle
        selectedVehicleIdFlow.value = null
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithoutVehicle = viewModel.state.value
        assertNull("Should deselect vehicle", stateWithoutVehicle.selectedVehicle)
        assertTrue("Should clear features", stateWithoutVehicle.features.isEmpty())
    }

    @Test
    fun `should handle empty vehicle list`() = runTest {
        selectedVehicleIdFlow.value = 1L // Selected ID but no vehicles
        vehiclesFlow.value = emptyList()

        val viewModel = DashboardViewModel(vehicleSelectionRepository, vehicleRepo, catalogRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNull("Should have no selected vehicle", state.selectedVehicle)
        assertTrue("Should have empty features", state.features.isEmpty())
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `should handle brand not found scenario`() = runTest {
        // Vehicle with unknown brand
        val unknownBrandVehicle = Vehicle(1L, "UnknownBrand", "Model", 2020, FuelType.Gasoline)
        
        selectedVehicleIdFlow.value = 1L
        vehiclesFlow.value = listOf(unknownBrandVehicle)

        val viewModel = DashboardViewModel(vehicleSelectionRepository, vehicleRepo, catalogRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Should load vehicle", unknownBrandVehicle, state.selectedVehicle)
        assertTrue("Should have empty features for unknown brand", state.features.isEmpty())
        assertFalse("Should not be loading", state.isLoading)
    }
}