package com.carly.vehicles.presentation.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carly.vehicles.domain.model.FuelType
import com.carly.vehicles.domain.model.Vehicle
import com.carly.vehicles.presentation.R
import com.carly.vehicles.presentation.ui.theme.Typography
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@Composable
fun CarInfo(
    modifier: Modifier = Modifier,
    vehicle: Vehicle
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "${vehicle.brand} - ${vehicle.series}",
                style = Typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${vehicle.year} - ${vehicle.fuel.name}",
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.switch_car),
            contentDescription = "Switch Car"
        )
    }
}

@Preview
@Composable
private fun CarInfoPrev() {
    VehicleManagerTheme {
        CarInfo(
            vehicle = Vehicle(
                id = 1,
                brand = "BMW",
                series = "X5",
                year = 2020,
                fuel = FuelType.Diesel
            )
        )
    }
}