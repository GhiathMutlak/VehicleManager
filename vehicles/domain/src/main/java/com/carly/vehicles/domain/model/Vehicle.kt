package com.carly.vehicles.domain.model

data class Vehicle(
    val id: Long,
    val brand: String,
    val series: String,
    val year: Int,
    val fuel: FuelType
)