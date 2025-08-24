package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult

class SetSelectedVehicleUseCase(private val repo: VehicleSelectionRepository) {
    suspend operator fun invoke(id: Long?): EmptyResult<DataError.Local> =
        repo.setSelectedVehicleId(id)
}