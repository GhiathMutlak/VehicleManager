package com.carly.vehicles.domain.repo

import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SelectionRepo {
    val selectedVehicleId: Flow<Long?>
    suspend fun setSelectedVehicleId(id: Long?): EmptyResult<DataError.Local>
}