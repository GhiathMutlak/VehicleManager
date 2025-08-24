package com.carly.vehicles.presentation.ui.createvehicle.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@Composable
fun SelectionBreadcrumb(
    breadcrumb: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (breadcrumb.isNotBlank()) {
            Text(
                text = breadcrumb,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionBreadcrumbPreview() {
    VehicleManagerTheme {
        SelectionBreadcrumb(
            breadcrumb = "BMW, 7 Series"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionBreadcrumbLongPreview() {
    VehicleManagerTheme {
        SelectionBreadcrumb(
            breadcrumb = "Mercedes-Benz, E-Class, 2023"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionBreadcrumbEmptyPreview() {
    VehicleManagerTheme {
        SelectionBreadcrumb(
            breadcrumb = ""
        )
    }
}