package com.carly.vehicles.presentation.ui.createvehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carly.vehicles.presentation.ui.components.CarlyTopAppBar
import com.carly.vehicles.presentation.ui.components.SearchBar
import com.carly.vehicles.presentation.ui.createvehicle.components.SearchableSelectionList
import com.carly.vehicles.presentation.ui.createvehicle.components.SelectionBreadcrumb
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVehicleScreenContent(
    state: CreateVehicleState = CreateVehicleState(),
    onAction: (CreateVehicleAction) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3A3F49), // gradientTop
                        Color(0xFF2E323A), // gradientMiddle
                        Color(0xFF23262B)  // gradientBottom
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                CarlyTopAppBar(
                    title = "Car selection",
                    onNavigateBack = { onAction(CreateVehicleAction.NavigateBack) },
                    backgroundColor = Color.Transparent
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = Color.Transparent,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                key(state.currentStep) {
                    SearchBar(
                        searchQuery = state.searchQuery,
                        onSearchQueryChange = {
                            onAction(CreateVehicleAction.UpdateSearchQuery(it))
                        },
                        searchPlaceholder = state.searchPlaceholder
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Show breadcrumb for steps after brand selection
                SelectionBreadcrumb(breadcrumb = state.breadcrumb)

                // Main content based on current step
                when (state.currentStep) {
                    CreateVehicleStep.BrandSelection -> {
                        BrandSelectionContent(
                            state = state,
                            onAction = onAction
                        )
                    }

                    CreateVehicleStep.SeriesSelection -> {
                        SeriesSelectionContent(
                            state = state,
                            onAction = onAction
                        )
                    }

                    CreateVehicleStep.YearSelection -> {
                        YearSelectionContent(
                            state = state,
                            onAction = onAction
                        )
                    }

                    CreateVehicleStep.FuelTypeSelection -> {
                        FuelTypeSelectionContent(
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandSelectionContent(
    state: CreateVehicleState,
    onAction: (CreateVehicleAction) -> Unit
) {
    SearchableSelectionList(
        items = state.filteredBrands,
        onItemClick = { brand ->
            onAction(CreateVehicleAction.SelectBrand(brand))
        },
        itemText = { it.name },
        isLoading = state.isLoading
    )
}

@Composable
private fun SeriesSelectionContent(
    state: CreateVehicleState,
    onAction: (CreateVehicleAction) -> Unit
) {
    SearchableSelectionList(
        items = state.filteredSeries,
        onItemClick = { series ->
            onAction(CreateVehicleAction.SelectSeries(series))
        },
        itemText = { it.name },
        isLoading = state.isLoading
    )
}

@Composable
private fun YearSelectionContent(
    state: CreateVehicleState,
    onAction: (CreateVehicleAction) -> Unit
) {
    SearchableSelectionList(
        items = state.filteredYears,
        onItemClick = { year ->
            onAction(CreateVehicleAction.SelectYear(year))
        },
        itemText = { it.toString() },
        isLoading = state.isLoading
    )
}

@Composable
private fun FuelTypeSelectionContent(
    state: CreateVehicleState,
    onAction: (CreateVehicleAction) -> Unit
) {
    SearchableSelectionList(
        items = state.filteredFuelTypes,
        onItemClick = { fuelType -> onAction(CreateVehicleAction.SelectFuelType(fuelType)) },
        itemText = { it.name },
        isLoading = state.isLoading
    )
}

@Preview
@Composable
fun CreateVehicleScreenContentPreview() {
    VehicleManagerTheme {
        CreateVehicleScreenContent()
    }
}