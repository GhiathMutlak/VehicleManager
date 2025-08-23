package com.carly.vehicles.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class BrandDto(
    val brandId: String,
    val brandName: String,
    val series: List<SeriesDto>,
    val features: List<String>
)