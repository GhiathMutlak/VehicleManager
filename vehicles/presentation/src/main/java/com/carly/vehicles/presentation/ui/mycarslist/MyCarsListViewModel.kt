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
    
    fun onAction(action: MyCarsListAction) {
        when (action) {
            is MyCarsListAction.SelectVehicle -> {
                viewModelScope.launch {
                    val result = setSelectedVehicleUseCase(action.vehicleId)
                    if (result is Result.Failure) {
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
