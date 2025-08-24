package com.carly.vehicles.presentation.ui.createvehicle

import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Series

data class CreateVehicleState(
    val currentStep: CreateVehicleStep = CreateVehicleStep.BrandSelection,
    val selectedBrand: Brand? = null,
    val selectedSeries: Series? = null,
    val selectedYear: Int? = null,
    val selectedFuelType: FuelType? = null,
    val brands: List<Brand> = emptyList(),
    val series: List<Series> = emptyList(),
    val availableYears: List<Int> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
) {
    val filteredBrands: List<Brand>
        get() = if (searchQuery.isBlank()) brands else brands.filter { 
            it.name.contains(searchQuery, ignoreCase = true) 
        }
    
    val filteredSeries: List<Series>
        get() = if (searchQuery.isBlank()) series else series.filter { 
            it.name.contains(searchQuery, ignoreCase = true) 
        }
    
    val filteredYears: List<Int>
        get() = if (searchQuery.isBlank()) availableYears else availableYears.filter { 
            it.toString().contains(searchQuery) 
        }
    
    val filteredFuelTypes: List<FuelType>
        get() {
            val allFuelTypes = listOf(
                FuelType.Gasoline, FuelType.Diesel, FuelType.Hybrid, 
                FuelType.Electric, FuelType.Other
            )
            return if (searchQuery.isBlank()) allFuelTypes else allFuelTypes.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }
        }
    
    val searchPlaceholder: String
        get() = when (currentStep) {
            CreateVehicleStep.BrandSelection -> "Search for brand ..."
            CreateVehicleStep.SeriesSelection -> "Search for series ..."
            CreateVehicleStep.YearSelection -> "Search for build year ..."
            CreateVehicleStep.FuelTypeSelection -> "Search for fuel type ..."
        }
    
    val breadcrumb: String
        get() = buildString {
            selectedBrand?.let { append(it.name) }
            selectedSeries?.let { 
                if (isNotEmpty()) append(", ")
                append(it.name) 
            }
            selectedYear?.let { 
                if (isNotEmpty()) append(", ")
                append(it) 
            }
        }
    
    val canNavigateForward: Boolean
        get() = when (currentStep) {
            CreateVehicleStep.BrandSelection -> selectedBrand != null
            CreateVehicleStep.SeriesSelection -> selectedSeries != null
            CreateVehicleStep.YearSelection -> selectedYear != null
            CreateVehicleStep.FuelTypeSelection -> selectedFuelType != null
        }
}

enum class CreateVehicleStep {
    BrandSelection,
    SeriesSelection, 
    YearSelection,
    FuelTypeSelection
}
