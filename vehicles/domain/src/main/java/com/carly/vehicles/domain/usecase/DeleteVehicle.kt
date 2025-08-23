package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.SelectionRepo
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import kotlinx.coroutines.flow.first


class DeleteVehicle(
    private val vehiclesRepo: VehicleRepo,
    private val selectionRepo: SelectionRepo
) {
    suspend operator fun invoke(id: Long): EmptyResult<DataError.Local> {
        val selected = selectionRepo.selectedVehicleId.first()
        require(selected != id) { "Cannot delete selected vehicle." }
        return vehiclesRepo.deleteVehicle(id)
    }
}