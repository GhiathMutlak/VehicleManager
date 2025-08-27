package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for SetSelectedVehicleUseCase testing vehicle selection logic.
 * 
 * Tests cover:
 * - Setting selected vehicle ID
 * - Clearing selection (null ID)
 * - Repository interaction
 * - Error handling
 */
class SetSelectedVehicleUseCaseTest {

    @Mock
    private lateinit var selectionRepo: VehicleSelectionRepository

    private lateinit var useCase: SetSelectedVehicleUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = SetSelectedVehicleUseCase(selectionRepo)
    }

    @Test
    fun `invoke should set selected vehicle ID`() = runTest {
        // Given
        val vehicleId = 123L
        whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleId)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(selectionRepo).setSelectedVehicleId(vehicleId)
    }

    @Test
    fun `invoke should clear selection with null ID`() = runTest {
        // Given
        whenever(selectionRepo.setSelectedVehicleId(null)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(null)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(selectionRepo).setSelectedVehicleId(null)
    }

    @Test
    fun `invoke should pass through repository failures`() = runTest {
        // Given
        val vehicleId = 123L
        val error = DataError.Local.DISK_FULL
        whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Failure(error))

        // When
        val result = useCase(vehicleId)

        // Then
        assertTrue("Should be Failure", result is Result.Failure)
        assertEquals(error, (result as Result.Failure).error)
    }

    @Test
    fun `invoke should handle zero ID`() = runTest {
        // Given
        val vehicleId = 0L
        whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleId)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(selectionRepo).setSelectedVehicleId(vehicleId)
    }

    @Test
    fun `invoke should handle negative ID`() = runTest {
        // Given
        val vehicleId = -1L
        whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleId)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(selectionRepo).setSelectedVehicleId(vehicleId)
    }

    @Test
    fun `invoke should handle large ID values`() = runTest {
        // Given
        val vehicleId = Long.MAX_VALUE
        whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Success(Unit))

        // When
        val result = useCase(vehicleId)

        // Then
        assertTrue("Should be Success", result is Result.Success)
        verify(selectionRepo).setSelectedVehicleId(vehicleId)
    }

    @Test
    fun `invoke should handle multiple different IDs`() = runTest {
        // Given
        val vehicleIds = listOf(1L, 42L, 100L, 999L, 1234567L)

        // When & Then
        vehicleIds.forEach { vehicleId ->
            whenever(selectionRepo.setSelectedVehicleId(vehicleId)).thenReturn(Result.Success(Unit))
            
            val result = useCase(vehicleId)
            assertTrue("Should set vehicle ID $vehicleId", result is Result.Success)
            verify(selectionRepo).setSelectedVehicleId(vehicleId)
        }
    }
}