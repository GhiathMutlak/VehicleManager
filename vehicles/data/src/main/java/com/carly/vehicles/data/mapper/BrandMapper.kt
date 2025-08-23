package com.carly.vehicles.data.mapper

import com.carly.vehicles.data.database.entity.BrandEntity
import com.carly.vehicles.domain.model.Brand

fun BrandEntity.toBrand(): Brand = Brand(
    id = id,
    name = name
)

fun Brand.toBrandEntity(): BrandEntity = BrandEntity(
    id = id,
    name = name
)