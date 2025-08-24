package com.carly.vehicles.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val vehicleSelectionRepository: VehicleSelectionRepository,
    private val vehicleRepo: VehicleRepo,
    private val catalogRepo: CatalogRepo
) : ViewModel() {
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    
    init {
        loadSelectedVehicle()
    }
    
    private fun loadSelectedVehicle() {
        combine(
            vehicleSelectionRepository.selectedVehicleId,
            vehicleRepo.observeVehicles()
        ) { selectedId: Long?, vehicles: List<Vehicle> ->
            val selectedVehicle = vehicles.find { it.id == selectedId }
            selectedVehicle
        }.onEach { selectedVehicle: Vehicle? ->
            if (selectedVehicle != null) {
                loadFeaturesForVehicle(selectedVehicle)
            } else {
                _state.value = DashboardState(
                    selectedVehicle = null,
                    features = emptySet(),
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
    
    private fun loadFeaturesForVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            // First get all brands to find the brand ID
            catalogRepo.getBrands().onSuccess { brands ->
                val brandId = brands.find { it.name == vehicle.brand }?.id
                if (brandId != null) {
                    // Get features for this brand
                    catalogRepo.getFeatures(brandId).onSuccess { features ->
                        _state.value = DashboardState(
                            selectedVehicle = vehicle,
                            features = features,
                            isLoading = false
                        )
                    }
                } else {
                    // Brand not found, set empty features
                    _state.value = DashboardState(
                        selectedVehicle = vehicle,
                        features = emptySet(),
                        isLoading = false
                    )
                }
            }
        }
    }
}