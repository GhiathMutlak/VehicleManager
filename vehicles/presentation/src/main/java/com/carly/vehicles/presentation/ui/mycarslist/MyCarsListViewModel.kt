package com.carly.vehicles.presentation.ui.mycarslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.usecase.DeleteVehicleUseCase
import com.carly.vehicles.domain.usecase.SetSelectedVehicleUseCase
import com.carly.vehicles.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the My Cars List screen following MVI architecture pattern.
 * 
 * Manages the list of user's vehicles, handles vehicle selection and deletion operations,
 * and maintains the UI state for the vehicles listing screen.
 * 
 * Key responsibilities:
 * - Display list of user's vehicles
 * - Handle vehicle selection with visual feedback
 * - Manage vehicle deletion with validation (prevent deleting selected vehicle)
 * - Navigate to vehicle creation and dashboard screens
 * 
 * @param vehicleRepo Repository for vehicle data operations
 * @param vehicleSelectionRepository Repository for managing selected vehicle state
 * @param deleteVehicleUseCase Use case for deleting vehicles with business logic
 * @param setSelectedVehicleUseCase Use case for setting the currently selected vehicle
 */
@HiltViewModel
class MyCarsListViewModel @Inject constructor(
    private val vehicleRepo: VehicleRepo,
    private val vehicleSelectionRepository: VehicleSelectionRepository,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val setSelectedVehicleUseCase: SetSelectedVehicleUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(MyCarsListState())
    val state: StateFlow<MyCarsListState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<MyCarsListEvent>()
    val events = _events.asSharedFlow()
    
    init {
        observeVehicles()
        observeSelection()
    }
    
    private fun observeVehicles() {
        vehicleRepo.observeVehicles()
            .onEach { vehicles ->
                _state.update { 
                    it.copy(
                        vehicles = vehicles,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun observeSelection() {
        vehicleSelectionRepository.selectedVehicleId
            .onEach { selectedId ->
                _state.update { it.copy(selectedVehicleId = selectedId) }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Handles user actions from the UI following MVI architecture.
     * 
     * Processes all user interactions and updates state or triggers navigation events accordingly.
     * Implements business logic validation for vehicle deletion (prevents deleting selected vehicle).
     * 
     * @param action The user action to process (SelectVehicle, DeleteVehicle, AddNewVehicle)
     */
    fun onAction(action: MyCarsListAction) {
        when (action) {
            is MyCarsListAction.SelectVehicle -> {
                viewModelScope.launch {
                    val result = setSelectedVehicleUseCase(action.vehicleId)
                    if (result is Result.Success) {
                        _events.emit(MyCarsListEvent.NavigateToDashboard)
                    } else {
                        _events.emit(MyCarsListEvent.Error("Failed to select vehicle"))
                    }
                }
            }
            is MyCarsListAction.DeleteVehicle -> {
                viewModelScope.launch {
                    try {
                        val result = deleteVehicleUseCase(action.vehicleId)
                        if (result is Result.Failure) {
                            _events.emit(MyCarsListEvent.Error("Failed to delete vehicle"))
                        }
                    } catch (_: IllegalArgumentException) {
                        val message = "Cannot delete currently selected vehicle"
                        _events.emit(MyCarsListEvent.Error(message))
                    }
                }
            }
            is MyCarsListAction.AddNewVehicle -> {
                viewModelScope.launch {
                    _events.emit(MyCarsListEvent.NavigateToAddVehicle)
                }
            }
        }
    }
}
