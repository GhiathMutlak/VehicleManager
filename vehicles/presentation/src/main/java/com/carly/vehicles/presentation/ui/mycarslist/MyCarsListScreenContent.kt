package com.carly.vehicles.presentation.ui.mycarslist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.presentation.ui.components.CarlyTopAppBar
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCarsListScreenContent(
    state: MyCarsListState = MyCarsListState(),
    onAction: (MyCarsListAction) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        topBar = {
            CarlyTopAppBar(
                title = "Your Cars",
                onNavigateBack = onNavigateBack,
                backgroundColor = Color.Transparent
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) { paddingValues ->
        Column(
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cars List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = state.vehicles,
                    key = { it.id }
                ) { vehicle ->
                    VehicleItemCard(
                        vehicle = vehicle,
                        isSelected = vehicle.id == state.selectedVehicleId,
                        onVehicleClick = {
                            onAction(MyCarsListAction.SelectVehicle(vehicle.id))
                        },
                        onDeleteClick = {
                            onAction(MyCarsListAction.DeleteVehicle(vehicle.id))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add New Car Button
            Button(
                onClick = { onAction(MyCarsListAction.AddNewVehicle) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFBA00B),
                                Color(0xFFFCA909), 
                                Color(0xFFFDC704)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Add new car",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun VehicleItemCard(
    vehicle: Vehicle,
    isSelected: Boolean,
    onVehicleClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "vehicle_card_scale"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(300),
        label = "vehicle_card_border"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onVehicleClick() }
            .border(
                width = if (isSelected) 1.2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .semantics {
                contentDescription = if (isSelected) {
                    "Currently selected vehicle: ${vehicle.brand} ${vehicle.series}, ${vehicle.year}, ${vehicle.fuel.name}"
                } else {
                    "Select vehicle: ${vehicle.brand} ${vehicle.series}, ${vehicle.year}, ${vehicle.fuel.name}"
                }
                role = Role.Button
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${vehicle.brand} - ${vehicle.series}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${vehicle.year} - ${vehicle.fuel.name}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (!isSelected) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ${vehicle.brand} ${vehicle.series}",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyCarsListScreenContentPreview() {
    VehicleManagerTheme {
        MyCarsListScreenContent(
            state = MyCarsListState(
                vehicles = listOf(
                    Vehicle(1L, "BMW", "3 series", 2018, FuelType.Diesel),
                    Vehicle(2L, "Audi", "A4", 2007, FuelType.Gasoline),
                    Vehicle(3L, "Mercedes", "C class", 2008, FuelType.Diesel)
                ),
                selectedVehicleId = 2L
            ),
            onAction = {},
            onNavigateBack = {},
        )
    }
}
