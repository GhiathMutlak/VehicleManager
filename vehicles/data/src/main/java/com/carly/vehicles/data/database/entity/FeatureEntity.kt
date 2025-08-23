package com.carly.vehicles.data.database.entity

import androidx.room.Entity

@Entity(
    tableName = "features",
    primaryKeys = ["id"]
)
data class FeatureEntity(
    val id: String,
    val brandId: String,
    val name: String
)