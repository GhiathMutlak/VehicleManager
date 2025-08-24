package com.carly.vehiclemanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carly.vehicles.presentation.ui.dashboard.DashboardScreen
import com.carly.vehicles.presentation.ui.mycarslist.MyCarsListScreen

@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MyCarsList.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }

        composable(Screen.MyCarsList.route) {
            MyCarsListScreen()
        }
    }
}