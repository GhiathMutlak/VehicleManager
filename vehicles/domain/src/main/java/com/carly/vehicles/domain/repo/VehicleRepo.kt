package com.carly.vehicles.domain.repo

import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface VehicleRepo {
    fun observeVehicles(): Flow<List<Vehicle>>
    suspend fun createVehicle(vehicle: Vehicle): Result<Long, DataError.Local>
    suspend fun deleteVehicle(id: Long) : EmptyResult<DataError.Local>
    suspend fun getVehicle(id: Long): Result<Vehicle?, DataError.Local>
}