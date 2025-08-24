package com.carly.vehicles.domain.model

sealed class FuelType(val name: String) {
    data object Diesel : FuelType("Diesel")
    data object Gasoline : FuelType("Gasoline")
    data object Hybrid : FuelType("Hybrid")
    data object Electric : FuelType("Electric")
    data object Other : FuelType("Other")
}
