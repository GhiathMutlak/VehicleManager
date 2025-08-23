package com.carly.vehicles.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class SeriesDto(
    val id: String,
    val name: String,
    val minYear: Int,
    val maxYear: Int,
    val features: List<String>
)