package com.carly.vehicles.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.carly.vehicles.data.database.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles ORDER BY id DESC")
    fun observeAll(): Flow<List<VehicleEntity>>

    @Upsert
    suspend fun upsert(v: VehicleEntity): Long

    @Delete
    suspend fun delete(v: VehicleEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM vehicles WHERE id=:id)")
    suspend fun exists(id: Long): Boolean
}