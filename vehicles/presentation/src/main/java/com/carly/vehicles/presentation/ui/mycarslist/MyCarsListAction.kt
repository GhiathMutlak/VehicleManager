package com.carly.vehicles.presentation.ui.mycarslist

sealed interface MyCarsListAction {
    data class SelectVehicle(val vehicleId: Long) : MyCarsListAction
    data class DeleteVehicle(val vehicleId: Long) : MyCarsListAction
    data object AddNewVehicle : MyCarsListAction
}
