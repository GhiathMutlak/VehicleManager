package com.carly.vehicles.data.mapper

import com.carly.vehicles.data.database.entity.VehicleEntity
import com.carly.vehicles.domain.model.Vehicle

fun VehicleEntity.toVehicle(): Vehicle = Vehicle(
    id = id,
    brand = brand,
    series = series,
    year = year,
    fuel = fuel
)

fun Vehicle.toVehicleEntity(): VehicleEntity = VehicleEntity(
    id = id,
    brand = brand,
    series = series,
    year = year,
    fuel = fuel
)