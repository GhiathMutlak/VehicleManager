package com.carly.vehicles.domain.model

data class Dashboard(
    val vehicles: List<Vehicle>,
    val selected: Vehicle?,
    val features: Set<Feature>
)