package com.carly.vehicles.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class BrandDto(
    val id: String,
    val name: String,
    val series: List<SeriesDto>
)