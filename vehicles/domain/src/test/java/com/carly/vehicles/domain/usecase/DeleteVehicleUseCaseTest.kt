package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for DeleteVehicleUseCase testing business logic validation.
 * 
 * Tests cover:
 * - Selected vehicle protection (cannot delete selected vehicle)
 * - Repository interaction
 * - Error handling and edge cases
 * - Business rule enforcement
 */
class DeleteVehicleUseCaseTest {

    @Mock
    private lateinit var vehicleRepo: VehicleRepo

    @Mock
    private lateinit var selectionRepo: VehicleSelectionRepository

    private lateinit var useCase: DeleteVehicleUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = DeleteVehicleUseCase(vehicleRepo, selectionRepo)
    }

    @Test
    fun `invoke should delete non-selected vehicle`() = runTest {
        // Given
        val selectedId = 1L
        val vehicleToDelete = 2L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))
        whenever(vehicleRepo.deleteVehicle(vehicleToDelete)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleToDelete)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(vehicleRepo).deleteVehicle(vehicleToDelete)
    }

    @Test
    fun `invoke should reject deleting selected vehicle`() = runTest {
        // Given
        val selectedId = 1L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))

        // When & Then
        try {
            useCase(selectedId)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }
    }

    @Test
    fun `invoke should handle no selected vehicle`() = runTest {
        // Given
        val vehicleToDelete = 1L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(null))
        whenever(vehicleRepo.deleteVehicle(vehicleToDelete)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleToDelete)

        // Then
        assertTrue("Should be Success when no vehicle selected", result is Result.Success)
        verify(vehicleRepo).deleteVehicle(vehicleToDelete)
    }

    @Test
    fun `invoke should pass through repository failures`() = runTest {
        // Given
        val selectedId = 1L
        val vehicleToDelete = 2L
        val error = DataError.Local.DISK_FULL
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))
        whenever(vehicleRepo.deleteVehicle(vehicleToDelete)).thenReturn(Result.Failure(error))

        // When
        val result = useCase(vehicleToDelete)

        // Then
        assertTrue("Should be Failure", result is Result.Failure)
        assertEquals(error, (result as Result.Failure).error)
    }

    @Test
    fun `invoke should handle different vehicle IDs correctly`() = runTest {
        // Given
        val selectedId = 100L
        val vehiclesToDelete = listOf(1L, 50L, 99L, 101L, 200L)
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))

        // When & Then
        vehiclesToDelete.forEach { vehicleId ->
            whenever(vehicleRepo.deleteVehicle(vehicleId)).thenReturn(Result.Success(Unit))
            
            val result = useCase(vehicleId)
            assertTrue("Should delete vehicle $vehicleId (not selected)", 
                       result is Result.Success)
        }
    }

    @Test
    fun `invoke should consistently reject selected vehicle deletion`() = runTest {
        // Given
        val selectedIds = listOf(1L, 25L, 100L, 999L)

        // When & Then
        selectedIds.forEach { selectedId ->
            whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))
            
            try {
                useCase(selectedId)
                fail("Should reject deleting selected vehicle $selectedId")
            } catch (e: IllegalArgumentException) {
                // Expected exception
            }
        }
    }

    @Test
    fun `invoke should provide meaningful error message`() = runTest {
        // Given
        val selectedId = 1L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))

        // When & Then
        try {
            useCase(selectedId)
            fail("Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue("Error message should mention selected vehicle",
                       e.message?.contains("selected") == true)
            assertTrue("Error message should mention cannot delete",
                       e.message?.contains("Cannot delete") == true)
        }
    }

    @Test
    fun `invoke should handle zero IDs correctly`() = runTest {
        // Given
        val selectedId = 0L
        val vehicleToDelete = 1L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))
        whenever(vehicleRepo.deleteVehicle(vehicleToDelete)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleToDelete)

        // Then
        assertTrue("Should succeed when deleting non-zero ID", result is Result.Success)
    }

    @Test
    fun `invoke should handle negative IDs correctly`() = runTest {
        // Given
        val selectedId = -1L
        val vehicleToDelete = 1L
        whenever(selectionRepo.selectedVehicleId).thenReturn(flowOf(selectedId))
        whenever(vehicleRepo.deleteVehicle(vehicleToDelete)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleToDelete)

        // Then
        assertTrue("Should succeed when IDs are different", result is Result.Success)
    }
}