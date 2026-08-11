// app/src/main/java/com/example/ui/components/CustomBarChart.kt
package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent

data class BarItem(
    val label: String,
    val value: Float,
    val color: Color
)

@Composable
fun CustomBarChart(
    title: String,
    barItems: List<BarItem>,
    modifier: Modifier = Modifier
) {
    val maxValue = barItems.maxOfOrNull { it.value }?.coerceAtLeast(10f) ?: 100f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height - 30.dp.toPx()
                    val barWidth = 36.dp.toPx()
                    val spaceBetween = (canvasWidth - (barItems.size * barWidth)) / (barItems.size + 1)

                    barItems.forEachIndexed { index, item ->
                        val barHeight = (item.value / maxValue) * canvasHeight
                        val xOffset = spaceBetween + index * (barWidth + spaceBetween)
                        val yOffset = canvasHeight - barHeight

                        // Draw Background Track
                        drawRoundRect(
                            color = Color.Gray.copy(alpha = 0.15f),
                            topLeft = Offset(xOffset, 0f),
                            size = Size(barWidth, canvasHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )

                        // Draw Filled Bar
                        drawRoundRect(
                            color = item.color,
                            topLeft = Offset(xOffset, yOffset),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                    }
                }

                // Labels below bars
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    barItems.forEach { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(50.dp)
                        ) {
                            Text(
                                text = "$${item.value.toInt()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
