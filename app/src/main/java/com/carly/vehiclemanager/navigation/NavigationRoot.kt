package com.carly.vehiclemanager.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        startDestination = Screen.Dashboard.route,
        modifier = Modifier.background(Color(0xFF1A1A1A))
    ) {
        composable(
            route = Screen.Dashboard.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200))
            }
        ) {
            DashboardScreen(
                onNavigateToCreateVehicle = {
                    navController.navigate(Screen.CreateVehicle.route)
                },
                onSwitchCar = {
                    navController.navigate(Screen.MyCarsList.route)
                }
            )
        }

        composable(
            route = Screen.MyCarsList.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200))
            }
        ) {
            MyCarsListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddVehicle = {
                    navController.navigate(Screen.CreateVehicle.route)
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.CreateVehicle.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200))
            }
        ) {
            CreateVehicleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onVehicleCreated = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}