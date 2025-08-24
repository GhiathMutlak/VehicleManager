@file:OptIn(ExperimentalMaterial3Api::class)

package com.carly.vehicles.presentation.ui.mycarslist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
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
fun MyCarsListScreen(
    viewModel: MyCarsListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddVehicle: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MyCarsListEvent.NavigateToAddVehicle -> onNavigateToAddVehicle()
            is MyCarsListEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }


    MyCarsListScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}