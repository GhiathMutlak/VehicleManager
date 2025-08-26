@file:OptIn(ExperimentalMaterial3Api::class)

package com.carly.vehicles.presentation.ui.createvehicle

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carly.vehicles.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun CreateVehicleScreen(
    viewModel: CreateVehicleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onVehicleCreated: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle system back button for step navigation - only when NOT on first step
    BackHandler(enabled = state.currentStep != CreateVehicleStep.BrandSelection) {
        viewModel.onAction(CreateVehicleAction.NavigateBack)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateVehicleEvent.NavigateBack -> onNavigateBack()
            is CreateVehicleEvent.VehicleCreated -> onVehicleCreated()
            is CreateVehicleEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    CreateVehicleScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}