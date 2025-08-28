package com.carly.vehicles.presentation.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carly.vehicles.domain.model.Feature
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.presentation.R
import com.carly.vehicles.presentation.ui.components.ItemCard

@Composable
fun DashboardScreenContent(
    modifier: Modifier = Modifier,
    selectedCar: Vehicle? = null,
    features: Set<Feature> = emptySet(),
    onNavigateToCreateVehicle: () -> Unit,
    onSwitchCar: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.66f),
            contentScale = ContentScale.FillWidth
        )

        // Gradient overlay starting from middle and going to bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF3A3F49).copy(alpha = 0.7f),
                            Color(0xFF2E323A).copy(alpha = 0.9f),
                            Color(0xFF23262B)
                        ),
                        startY = 700f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Carly Logo",
                modifier = Modifier.size(100.dp)
            )

            if (selectedCar == null) {
                BoxWithConstraints {
                    Spacer(modifier = Modifier.height(maxHeight * 0.25f)) // 25% of screen height
                }

                // Custom add button with glowing background
                Box(
                    modifier = Modifier
                        .size(110.dp) // Larger clickable area
                        .clip(CircleShape)
                        .clickable(
                            onClick = onNavigateToCreateVehicle,
                            indication = ripple(bounded = false),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.8f),
                                    Color.White.copy(alpha = 0.4f),
                                    Color.White.copy(alpha = 0.2f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.7f),
                                    Color.White.copy(alpha = 0.45f),
                                    Color.Gray.copy(alpha = 0.3f),
                                    Color.Gray.copy(alpha = 0.15f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Car",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))

                // Vehicle Info Section
                CarInfo(
                    vehicle = selectedCar,
                    onSwitchCar = onSwitchCar
                )

                BoxWithConstraints {
                    Spacer(modifier = Modifier.height(maxHeight * 0.02f)) // 25% of screen height
                }

                // Car Image (centered)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BoxWithConstraints {
                        Image(
                            painter = painterResource(id = R.drawable.car),
                            contentDescription = "Car",
                            modifier = Modifier.size(maxWidth * 0.7f) // 70% of screen width
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Discover Your Car Section
                DiscoverYourCarSection(features)

                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}

@Composable
private fun DiscoverYourCarSection(
    features: Set<Feature>
) {
    Column {
        // Section Title
        Text(
            text = "Discover your car",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
        )

        // Feature Cards
        ElevatedCard(
            modifier = Modifier
                .padding(top = 4.dp)
                .heightIn(max = 350.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2E3238),
                            Color(0xFF32363E),
                            Color(0xFF32363F),
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 16.dp
            )
        ) {
            val featureList = features.toList()

            LazyColumn {
                itemsIndexed(featureList) { index, feature ->
                    val isLast = index == featureList.lastIndex
                    ItemCard(
                        title = feature.displayName,
                        onClick = {},
                        isDividerVisible = !isLast
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarlyAppScreenPreview() {
    MaterialTheme {
        DashboardScreenContent(
            selectedCar = null,
            features = emptySet(),
            onNavigateToCreateVehicle = {},
            onSwitchCar = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarlyAppScreenPreviewWithCar() {
    MaterialTheme {
        DashboardScreenContent(
            selectedCar = Vehicle(
                id = 1,
                brand = "BMW",
                series = "X5",
                year = 2020,
                fuel = FuelType.Diesel
            ),
            features = setOf(
                Feature.Diagnostics,
                Feature.LiveData,
                Feature.CarCheck
            ),
            onNavigateToCreateVehicle = {},
            onSwitchCar = {}
        )
    }
}