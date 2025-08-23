package com.carly.vehicles.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "catalog_series",
)
data class SeriesEntity(
    @PrimaryKey val id: String,
    val brandId: String,
    val name: String,
    val minYear: Int,
    val maxYear: Int
)