package com.carly.vehicles.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_brand")
data class BrandEntity(
    @PrimaryKey val id: String,
    val name: String
)