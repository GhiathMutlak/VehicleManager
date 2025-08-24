package com.carly.vehiclemanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carly.vehicles.presentation.ui.createvehicle.CreateVehicleScreen
import com.carly.vehicles.presentation.ui.dashboard.DashboardScreen
import com.carly.vehicles.presentation.ui.mycarslist.MyCarsListScreen

@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CreateVehicle.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }

        composable(Screen.MyCarsList.route) {
            MyCarsListScreen()
        }

        composable(Screen.CreateVehicle.route) {
            CreateVehicleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onVehicleCreated = { vehicleId ->
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.CreateVehicle.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}