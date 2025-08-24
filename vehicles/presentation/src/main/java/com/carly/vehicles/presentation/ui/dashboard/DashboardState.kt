package com.carly.vehicles.presentation.ui.dashboard

import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.Vehicle

data class DashboardState(
    val selectedVehicle: Vehicle? = null,
    val features: Set<Feature> = emptySet(),
    val isLoading: Boolean = true
)