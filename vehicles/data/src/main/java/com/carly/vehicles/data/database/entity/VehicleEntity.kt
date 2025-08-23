package com.carly.vehicles.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carly.vehicles.domain.model.FuelType

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val brand: String,
    val series: String,
    val year: Int,
    val fuel: FuelType
)