package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.repo.VehicleSelectionRepository
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import kotlinx.coroutines.flow.first


class DeleteVehicleUseCase(
    private val vehiclesRepo: VehicleRepo,
    private val vehicleSelectionRepository: VehicleSelectionRepository
) {
    suspend operator fun invoke(id: Long): EmptyResult<DataError.Local> {
        val selected = vehicleSelectionRepository.selectedVehicleId.first()
        require(selected != id) { "Cannot delete selected vehicle." }
        return vehiclesRepo.deleteVehicle(id)
    }
}