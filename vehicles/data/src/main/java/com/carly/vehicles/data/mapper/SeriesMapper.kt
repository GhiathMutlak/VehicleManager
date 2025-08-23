package com.carly.vehicles.data.mapper

import com.carly.vehicles.data.database.entity.SeriesEntity
import com.carly.vehicles.domain.model.Series

fun SeriesEntity.toSeries(): Series = Series(
    id = id,
    brandId = brandId,
    name = name,
    minYear = minYear,
    maxYear = maxYear,
)

fun Series.toSeriesEntity(): SeriesEntity = SeriesEntity(
    id = id,
    brandId = brandId,
    name = name,
    minYear = minYear,
    maxYear = maxYear,
)