package com.carly.vehicles.data.repo

import com.carly.vehicles.data.database.dao.VehicleDao
import com.carly.vehicles.data.mapper.toVehicle
import com.carly.vehicles.data.mapper.toVehicleEntity
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.domain.repo.VehicleRepo
import com.carly.vehicles.domain.util.DataError
import com.carly.vehicles.domain.util.EmptyResult
import com.carly.vehicles.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomVehicleRepo @Inject constructor(
    private val vehicleDao: VehicleDao,
) : VehicleRepo {

    override fun observeVehicles(): Flow<List<Vehicle>> =
        vehicleDao.observeAll().map { entities ->
            entities.map { it.toVehicle() }
        }

    override suspend fun createVehicle(vehicle: Vehicle): Result<Long, DataError.Local> =
        try {
            val id = vehicleDao.upsert(vehicle.toVehicleEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.DISK_FULL)
        }

    override suspend fun deleteVehicle(id: Long): EmptyResult<DataError.Local> =
        try {
            val entity = vehicleDao.getById(id) ?: return Result.Success(Unit)
            vehicleDao.delete(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Local.DISK_FULL)
        }

    override suspend fun getVehicle(id: Long): Result<Vehicle?, DataError.Local> =
        try {
            val entity = vehicleDao.getById(id)
            Result.Success(entity?.toVehicle())
        } catch (e: Exception) {
            Result.Failure(DataError.Local.DISK_FULL)
        }
}
