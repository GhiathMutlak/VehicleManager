package com.carly.vehicles.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarlyTopAppBar(
    onNavigateBack: () -> Unit,
    title: String,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        )
    )
}

@Preview
@Composable
private fun CarlyTopAppBarPreview() {
    VehicleManagerTheme {
        CarlyTopAppBar(
            onNavigateBack = {},
            title = "My Cars",
            backgroundColor = Color.Red
        )
    }
}