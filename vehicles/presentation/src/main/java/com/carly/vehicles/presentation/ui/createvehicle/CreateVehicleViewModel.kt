package com.carly.vehicles.presentation.ui.createvehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.CatalogRepo
import com.carly.vehicles.domain.usecase.CreateVehicleUseCase
import com.carly.vehicles.domain.usecase.SetSelectedVehicleUseCase
import com.carly.vehicles.domain.util.onFailure
import com.carly.vehicles.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Create Vehicle screen implementing MVI architecture.
 * 
 * Manages the multi-step vehicle creation flow including brand selection,
 * series selection, build year, and fuel type selection with search functionality.
 * 
 * Key responsibilities:
 * - Guide users through step-by-step vehicle creation process
 * - Load and filter catalog data (brands, series, years)
 * - Handle search functionality for each selection step
 * - Create and persist new vehicles with automatic selection
 * - Navigate between creation steps and back to dashboard
 * 
 * @param catalogRepo Repository for accessing vehicle catalog data
 * @param createVehicleUseCase Use case for creating new vehicles with business validation
 * @param setSelectedVehicleUseCase Use case for automatically selecting newly created vehicle
 */
@HiltViewModel
class CreateVehicleViewModel @Inject constructor(
    private val catalogRepo: CatalogRepo,
    private val createVehicleUseCase: CreateVehicleUseCase,
    private val setSelectedVehicleUseCase: SetSelectedVehicleUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(CreateVehicleState())
    val state: StateFlow<CreateVehicleState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<CreateVehicleEvent>()
    val events = _events.asSharedFlow()
    
    init {
        // Force reset state to ensure clean start every time
        _state.value = CreateVehicleState()
        loadBrands()
    }
    
    fun onAction(action: CreateVehicleAction) {
        when (action) {
            is CreateVehicleAction.UpdateSearchQuery -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is CreateVehicleAction.SelectBrand -> {
                _state.update { 
                    it.copy(
                        selectedBrand = action.brand,
                        currentStep = CreateVehicleStep.SeriesSelection,
                        searchQuery = "",
                        selectedSeries = null,
                        selectedYear = null,
                        selectedFuelType = null
                    )
                }
                loadSeries(action.brand.id)
            }
            is CreateVehicleAction.SelectSeries -> {
                _state.update { 
                    it.copy(
                        selectedSeries = action.series,
                        currentStep = CreateVehicleStep.YearSelection,
                        searchQuery = "",
                        selectedYear = null,
                        selectedFuelType = null
                    )
                }
                generateYearsForSeries(action.series)
            }
            is CreateVehicleAction.SelectYear -> {
                _state.update { 
                    it.copy(
                        selectedYear = action.year,
                        currentStep = CreateVehicleStep.FuelTypeSelection,
                        searchQuery = "",
                        selectedFuelType = null
                    )
                }
            }
            is CreateVehicleAction.SelectFuelType -> {
                _state.update { it.copy(selectedFuelType = action.fuelType) }
                createVehicle()
            }
            is CreateVehicleAction.NavigateBack -> {
                when (_state.value.currentStep) {
                    CreateVehicleStep.BrandSelection -> {
                        viewModelScope.launch {
                            _events.emit(CreateVehicleEvent.NavigateBack)
                        }
                    }
                    CreateVehicleStep.SeriesSelection -> {
                        _state.value = CreateVehicleState(
                            brands = _state.value.brands,
                            currentStep = CreateVehicleStep.BrandSelection
                        )
                    }
                    CreateVehicleStep.YearSelection -> {
                        _state.update { 
                            it.copy(
                                currentStep = CreateVehicleStep.SeriesSelection,
                                searchQuery = "",
                                selectedYear = null,
                                selectedFuelType = null,
                                availableYears = emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    CreateVehicleStep.FuelTypeSelection -> {
                        _state.update { 
                            it.copy(
                                currentStep = CreateVehicleStep.YearSelection,
                                searchQuery = "",
                                selectedFuelType = null,
                                isLoading = false
                            )
                        }
                    }
                }
            }
            is CreateVehicleAction.CreateVehicle -> createVehicle()
        }
    }
    
    private fun loadBrands() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            catalogRepo.getBrands()
                .onSuccess { brands ->
                    _state.update { 
                        it.copy(
                            brands = brands,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { 
                        it.copy(
                            isLoading = false
                        )
                    }
                    _events.emit(CreateVehicleEvent.Error("Failed to load brands"))
                }
        }
    }
    
    private fun loadSeries(brandId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            catalogRepo.getSeries(brandId)
                .onSuccess { series ->
                    _state.update { 
                        it.copy(
                            series = series,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _state.update { 
                        it.copy(
                            isLoading = false
                        )
                    }
                    _events.emit(CreateVehicleEvent.Error("Failed to load series"))
                }
        }
    }
    
    private fun generateYearsForSeries(series: com.carly.vehicles.domain.model.Series) {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val years = (series.minYear..minOf(series.maxYear, currentYear)).toList().reversed()
        
        _state.update { 
            it.copy(
                availableYears = years,
                isLoading = false
            )
        }
    }
    
    private fun createVehicle() {
        viewModelScope.launch {
            val currentState = _state.value
            val brand = currentState.selectedBrand
            val series = currentState.selectedSeries
            val year = currentState.selectedYear
            val fuelType = currentState.selectedFuelType
            
            if (brand == null || series == null || year == null || fuelType == null) {
                _events.emit(CreateVehicleEvent.Error("Please complete all selection steps"))
                return@launch
            }
            
            _state.update { it.copy(isLoading = true) }
            
            val vehicle = Vehicle(
                id = 0L, // Will be assigned by database
                brand = brand.name,
                series = series.name,
                year = year,
                fuel = fuelType
            )
            
            createVehicleUseCase(vehicle)
                .onSuccess { vehicleId ->
                    setSelectedVehicleUseCase(vehicleId)
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            _events.emit(CreateVehicleEvent.VehicleCreated(vehicleId))
                        }
                        .onFailure {
                            _state.update { it.copy(isLoading = false) }
                            _events.emit(CreateVehicleEvent.Error("Vehicle created but failed to select it"))
                        }
                }
                .onFailure {
                    _state.update { 
                        it.copy(isLoading = false)
                    }
                    _events.emit(CreateVehicleEvent.Error("Failed to create vehicle"))
                }
        }
    }
}
