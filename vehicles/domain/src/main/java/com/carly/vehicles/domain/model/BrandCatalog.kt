package com.carly.vehicles.domain.model

data class BrandCatalog(
    val brand: Brand,
    val series: List<Series>,
    val features: Set<Feature>
)
