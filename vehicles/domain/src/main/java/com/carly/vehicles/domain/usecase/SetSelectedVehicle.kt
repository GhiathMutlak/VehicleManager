package com.carly.vehicles.domain.usecase

import com.carly.vehicles.domain.repo.SelectionRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult

class SetSelectedVehicle(private val repo: SelectionRepo) {
    suspend operator fun invoke(id: Long?): EmptyResult<DataError.Local> =
        repo.setSelectedVehicleId(id)
}