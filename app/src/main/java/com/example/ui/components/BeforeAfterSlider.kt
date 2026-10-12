package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun BeforeAfterSlider(
    beforeImageUrl: String,
    afterImageUrl: String,
    modifier: Modifier = Modifier,
    title: String = "Site Transformation"
) {
    var sliderPosition by remember { mutableFloatStateOf(0.5f) }
    var containerWidthPx by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("before_after_slider_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Drag slider to compare",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (containerWidthPx > 0) {
                                sliderPosition = (sliderPosition + dragAmount.x / containerWidthPx)
                                    .coerceIn(0.05f, 0.95f)
                            }
                        }
                    }
            ) {
                containerWidthPx = constraints.maxWidth.toFloat()
                val fullWidth = maxWidth
                val dividerX = fullWidth * sliderPosition

                // 1. Bottom Layer: Finished / After Image
                AsyncImage(
                    model = afterImageUrl,
                    contentDescription = "After - Finished Luxury Interior",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // 2. Top Layer: Raw Site / Before Image (Clipped to dividerX)
                Box(
                    modifier = Modifier
                        .width(dividerX)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                ) {
                    AsyncImage(
                        model = beforeImageUrl,
                        contentDescription = "Before - Bare Civil Structure",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .requiredWidth(fullWidth)
                            .fillMaxHeight()
                    )
                }

                // 3. Vertical Divider Line
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = (containerWidthPx * sliderPosition).roundToInt() - 2.dp.roundToPx(), y = 0) }
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                )

                // 4. Center Draggable Handle
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (containerWidthPx * sliderPosition).roundToInt() - 20.dp.roundToPx(),
                                y = (constraints.maxHeight / 2) - 20.dp.roundToPx()
                            )
                        }
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag("slider_handle"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = "Drag to compare",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Labels
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "BEFORE (Civil Raw)",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = "AFTER (Turnkey Finish)",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
