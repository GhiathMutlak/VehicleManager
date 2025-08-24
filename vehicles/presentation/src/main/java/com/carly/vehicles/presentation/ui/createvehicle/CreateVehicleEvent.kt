package com.carly.vehicles.presentation.ui.createvehicle

sealed interface CreateVehicleEvent {
    data class Error(val message: String) : CreateVehicleEvent
    data class VehicleCreated(val vehicleId: Long) : CreateVehicleEvent
    data object NavigateBack : CreateVehicleEvent
}
