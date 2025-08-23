package com.carly.vehicles.data.mapper

import com.carly.vehicles.data.database.entity.FeatureEntity
import com.carly.vehicles.domain.model.Feature

fun FeatureEntity.toFeature() = Feature.fromId(
    id = id,
    name = name
)