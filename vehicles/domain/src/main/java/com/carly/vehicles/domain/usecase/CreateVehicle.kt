package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.Result

class CreateVehicle (private val repo: VehicleRepo) {
    suspend operator fun invoke(input: Vehicle): Result<Long, DataError.Local> {
        require(input.series.isNotBlank() && input.brand.isNotBlank())
        require(input.year in 1980..2050)
        return repo.createVehicle(input)
    }
}