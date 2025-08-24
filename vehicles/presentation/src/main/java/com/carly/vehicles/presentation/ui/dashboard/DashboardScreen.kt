package com.carly.vehicles.presentation.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToCreateVehicle: () -> Unit,
    onSwitchCar: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    DashboardScreenContent(
        modifier = modifier,
        selectedCar = state.selectedVehicle,
        features = state.features,
        onNavigateToCreateVehicle = onNavigateToCreateVehicle,
        onSwitchCar = onSwitchCar
    )
}