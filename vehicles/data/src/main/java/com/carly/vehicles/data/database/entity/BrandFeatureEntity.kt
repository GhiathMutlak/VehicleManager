package com.carly.vehicles.data.database.entity

import androidx.room.Entity
import com.carly.vehicles.domain.model.Feature

@Entity(
    tableName = "catalog_features",
    primaryKeys = ["brandId", "feature"]
)
data class BrandFeatureEntity(
    val brandId: String,
    val feature: Feature
)