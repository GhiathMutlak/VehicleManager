package com.carly.vehicles.presentation.ui.mycarslist

sealed interface MyCarsListEvent {
    data class Error(val message: String) : MyCarsListEvent
    data object NavigateToAddVehicle : MyCarsListEvent
    data object NavigateToDashboard : MyCarsListEvent
}
