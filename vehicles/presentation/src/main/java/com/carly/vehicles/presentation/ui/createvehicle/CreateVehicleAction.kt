package com.carly.vehicles.presentation.ui.createvehicle

import com.carly.vehicles.domain.model.Brand
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Series

sealed interface CreateVehicleAction {
    data class UpdateSearchQuery(val query: String) : CreateVehicleAction
    data class SelectBrand(val brand: Brand) : CreateVehicleAction
    data class SelectSeries(val series: Series) : CreateVehicleAction
    data class SelectYear(val year: Int) : CreateVehicleAction
    data class SelectFuelType(val fuelType: FuelType) : CreateVehicleAction
    data object NavigateBack : CreateVehicleAction
    data object CreateVehicle : CreateVehicleAction
}
