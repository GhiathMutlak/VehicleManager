package com.carly.vehicles.presentation.ui.mycarslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.usecase.DeleteVehicleUseCase
import com.carly.vehicles.domain.usecase.SetSelectedVehicleUseCase
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
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
 * Unit tests for MyCarsListViewModel testing MVI architecture and business logic.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MyCarsListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val vehicleRepo = mockk<VehicleRepo>()
    private val vehicleSelectionRepository = mockk<VehicleSelectionRepository>()
    private val deleteVehicleUseCase = mockk<DeleteVehicleUseCase>()
    private val setSelectedVehicleUseCase = mockk<SetSelectedVehicleUseCase>()

    private val testDispatcher = StandardTestDispatcher()

    private val sampleVehicles = listOf(
        Vehicle(1L, "BMW", "X5", 2020, FuelType.Diesel),
        Vehicle(2L, "Audi", "A4", 2019, FuelType.Gasoline),
        Vehicle(3L, "Mercedes", "C-Class", 2021, FuelType.Hybrid)
    )

    private val vehiclesFlow = MutableStateFlow<List<Vehicle>>(emptyList())
    private val selectedVehicleIdFlow = MutableStateFlow<Long?>(null)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Setup default mock behavior
        every { vehicleRepo.observeVehicles() } returns vehiclesFlow
        every { vehicleSelectionRepository.selectedVehicleId } returns selectedVehicleIdFlow
    }

    @Test
    fun `init should load empty state`() = runTest {
        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("Vehicles list should be empty", state.vehicles.isEmpty())
        assertNull("No vehicle should be selected", state.selectedVehicleId)
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `should load vehicles list`() = runTest {
        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        vehiclesFlow.value = sampleVehicles
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Should load vehicles", sampleVehicles, state.vehicles)
        assertFalse("Should not be loading", state.isLoading)
    }

    @Test
    fun `should track selected vehicle changes`() = runTest {
        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        selectedVehicleIdFlow.value = 1L
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Should track selected vehicle ID", 1L, state.selectedVehicleId)

        selectedVehicleIdFlow.value = 2L
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedState = viewModel.state.value
        assertEquals("Should update selected vehicle ID", 2L, updatedState.selectedVehicleId)
    }

    @Test
    fun `onAction SelectVehicle should set selected vehicle`() = runTest {
        val vehicleId = 1L
        coEvery { setSelectedVehicleUseCase(vehicleId) } returns Result.Success(Unit)

        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)
        
        viewModel.onAction(MyCarsListAction.SelectVehicle(vehicleId))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { setSelectedVehicleUseCase(vehicleId) }
    }

    @Test
    fun `onAction SelectVehicle should handle selection failure`() = runTest {
        val vehicleId = 1L
        val error = DataError.Local.DISK_FULL
        coEvery { setSelectedVehicleUseCase(vehicleId) } returns Result.Failure(error)

        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)

        viewModel.onAction(MyCarsListAction.SelectVehicle(vehicleId))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { setSelectedVehicleUseCase(vehicleId) }
    }

    @Test
    fun `onAction DeleteVehicle should delete vehicle successfully`() = runTest {
        val vehicleId = 2L
        coEvery { deleteVehicleUseCase(vehicleId) } returns Result.Success(Unit)

        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)

        viewModel.onAction(MyCarsListAction.DeleteVehicle(vehicleId))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { deleteVehicleUseCase(vehicleId) }
    }

    @Test
    fun `onAction DeleteVehicle should handle selected vehicle protection`() = runTest {
        val selectedVehicleId = 1L
        coEvery { deleteVehicleUseCase(selectedVehicleId) } throws IllegalArgumentException("Cannot delete selected vehicle")

        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)

        viewModel.onAction(MyCarsListAction.DeleteVehicle(selectedVehicleId))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { deleteVehicleUseCase(selectedVehicleId) }
    }

    @Test
    fun `should handle empty vehicle list scenarios`() = runTest {
        val viewModel = MyCarsListViewModel(vehicleRepo, vehicleSelectionRepository, deleteVehicleUseCase, setSelectedVehicleUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val emptyState = viewModel.state.value
        assertTrue("Should handle empty vehicle list", emptyState.vehicles.isEmpty())
        assertNull("Should have no selected vehicle", emptyState.selectedVehicleId)
    }
}