package com.carly.vehicles.domain.model

data class Series(
    val id: String,
    val brandId: String,
    val name: String,
    val minYear: Int,
    val maxYear: Int
)
