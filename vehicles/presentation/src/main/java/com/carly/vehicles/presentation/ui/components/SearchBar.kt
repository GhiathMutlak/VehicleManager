package com.carly.vehicles.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchPlaceholder: String,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shadowColor: Color = Color(0xFF0A0B0D)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
    ) {
        // Inner shadows using gradient overlays
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    // Top shadow
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(shadowColor.copy(alpha = 0.4f), Color.Transparent),
                            startY = 5f,
                            endY = 20f
                        )
                    )
                    // Left shadow
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(shadowColor.copy(alpha = 0.4f), Color.Transparent),
                            startX = 0f,
                            endX = 20f
                        )
                    )
                }
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = searchPlaceholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )
    }

}


@Preview
@Composable
private fun SearchBarPrev() {
    VehicleManagerTheme { 
        SearchBar(
            searchQuery = "BMW",
            onSearchQueryChange = {},
            searchPlaceholder = "Select a brand"
        )
    }
}