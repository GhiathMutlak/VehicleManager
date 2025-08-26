package com.carly.vehicles.presentation.ui.createvehicle.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carly.vehicles.presentation.ui.components.ItemCard
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableSelectionList(
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemText: (T) -> String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // Loading or List Content
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF48505B),
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF222529),
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    count = items.size,
                    key = { index -> itemText(items[index]) }
                ) { index ->
                    val item = items[index]
                    ItemCard(
                        title = itemText(item),
                        onClick = { onItemClick(item) },
                        isDividerVisible = index < items.lastIndex,
                        backgroundColors = listOf(Color.Transparent, Color.Transparent),
                        itemPadding = 10.dp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchableSelectionListPreview() {
    VehicleManagerTheme {
        SearchableSelectionList(
            items = listOf("BMW", "Mercedes", "Audi", "Volkswagen", "Toyota"),
            onItemClick = {},
            itemText = { it },
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchableSelectionListLoadingPreview() {
    VehicleManagerTheme {
        SearchableSelectionList(
            items = emptyList<String>(),
            onItemClick = {},
            itemText = { it },
            isLoading = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchableSelectionListWithQueryPreview() {
    VehicleManagerTheme {
        SearchableSelectionList(
            items = listOf("BMW", "Mercedes"),
            onItemClick = {},
            itemText = { it },
            isLoading = false
        )
    }
}

