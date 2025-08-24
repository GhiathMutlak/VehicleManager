package com.carly.vehicles.presentation.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    DashboardScreenContent(
        selectedCar = Vehicle(
            id = 1,
            series = "Tesla Model S",
            brand = "Tesla",
            year = 2022,
            fuel = FuelType.Electric,
        )
    )
}