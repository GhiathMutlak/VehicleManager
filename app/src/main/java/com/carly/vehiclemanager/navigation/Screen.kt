package com.carly.vehiclemanager.navigation

sealed class Screen(val route: String) {
    object Brands : Screen("brands")

    data class Series(val brandId: String) : Screen("series/{brandId}") {
        companion object {
            fun createRoute(brandId: String) = "series/$brandId"
        }
    }

    data class BuildYear(val seriesId: String) : Screen("features/{seriesId}") {
        companion object {
            fun createRoute(seriesId: String) = "features/$seriesId"
        }
    }

    object FuelType : Screen("fuel_type")

    object YourCars : Screen("your_cars")

    object Dashboard : Screen("dashboard")
}