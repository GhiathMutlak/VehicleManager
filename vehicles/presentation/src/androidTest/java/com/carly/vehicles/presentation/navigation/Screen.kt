package com.carly.vehicles.presentation.navigation

sealed class Screen(val route: String) {
    object CreateVehicle : Screen("create_vehicle")
    object MyCarsList : Screen("my_cars_list")
    object Dashboard : Screen("dashboard")
}