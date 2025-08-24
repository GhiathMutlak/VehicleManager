package com.carly.vehicles.presentation.ui.mycarslist

import com.carly.vehicles.domain.model.Vehicle

data class MyCarsListState(
    val isLoading: Boolean = false,
    val vehicles: List<Vehicle> = emptyList(),
    val selectedVehicleId: Long? = null
)
