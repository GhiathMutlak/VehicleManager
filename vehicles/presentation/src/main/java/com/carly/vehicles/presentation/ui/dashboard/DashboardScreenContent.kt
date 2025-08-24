package com.carly.vehicles.presentation.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    selectedCar: Vehicle?
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Background image with overlay
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        if (selectedCar == null) {
            // Car Image (centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_car),
                    contentDescription = "Car",
                    modifier = Modifier
                        .size(90.dp)
                )
            }
        } else {
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

                Spacer(modifier = Modifier.height(40.dp))

                // Vehicle Info Section
                CarInfo(
                    vehicle = Vehicle(
                        id = 1,
                        brand = "BMW",
                        series = "X5",
                        year = 2020,
                        fuel = FuelType.Diesel
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Car Image (centered)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.car),
                        contentDescription = "Car",
                        modifier = Modifier.size(280.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Discover Your Car Section
                DiscoverYourCarSection(
                    setOf(
                        Feature.Diagnostics,
                        Feature.LiveData,
                        Feature.CarCheck,
                    )
                )

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
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Feature Cards
        ElevatedCard {
            val featureList = features.toList()

            featureList.forEachIndexed { index, feature ->
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

@Preview(showBackground = true)
@Composable
fun CarlyAppScreenPreview() {
    MaterialTheme {
        DashboardScreenContent(
            selectedCar = null
        )
    }
}