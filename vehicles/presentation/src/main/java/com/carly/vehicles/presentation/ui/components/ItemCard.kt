package com.carly.vehicles.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carly.vehicles.presentation.ui.theme.Typography
import com.carly.vehicles.presentation.ui.theme.VehicleManagerTheme

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    isDividerVisible: Boolean = true,
    backgroundColors: List<Color> = listOf<Color>(
        Color(0xFF26292E),
        Color(0xFF2C2F35),
        Color(0xFF32363D),
    ),
    itemPadding: Dp = 16.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "$title feature button"
                role = Role.Button
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(colors = backgroundColors)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(all = itemPadding)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.3f),
                            spotColor = Color.Black.copy(alpha = 0.4f)
                        )
                        .border(
                            BorderStroke(
                                0.5.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            ),
                            shape = CircleShape
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navigate to $title",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (isDividerVisible) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF48505B),
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF222529),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemCardPrev() {
    VehicleManagerTheme {
        ItemCard(
            title = "Live Data",
            onClick = {}
        )
    }
}