package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BudgetEstimatorCard(
    onBookConsultation: (Double, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTier by remember { mutableStateOf("PREMIUM") } // STANDARD, PREMIUM, LUXURY
    var carpetArea by remember { mutableFloatStateOf(1450f) }

    // Room inclusions
    var includeKitchen by remember { mutableStateOf(true) }
    var includeWardrobes by remember { mutableStateOf(true) }
    var includeFalseCeiling by remember { mutableStateOf(true) }
    var includeItalianMarble by remember { mutableStateOf(false) }
    var includeSmartAutomation by remember { mutableStateOf(true) }

    // Rates based on tier
    val baseRatePerSqFt = when (selectedTier) {
        "STANDARD" -> 28.0
        "PREMIUM" -> 46.0
        "LUXURY" -> 78.0
        else -> 46.0
    }

    // Cost calculations
    val baseCivilAndWoodwork = carpetArea * baseRatePerSqFt
    val kitchenAddOn = if (includeKitchen) {
        when (selectedTier) {
            "STANDARD" -> 6000.0
            "PREMIUM" -> 11500.0
            "LUXURY" -> 19500.0
            else -> 11500.0
        }
    } else 0.0

    val wardrobesAddOn = if (includeWardrobes) {
        when (selectedTier) {
            "STANDARD" -> 4500.0
            "PREMIUM" -> 8800.0
            "LUXURY" -> 15000.0
            else -> 8800.0
        }
    } else 0.0

    val falseCeilingAddOn = if (includeFalseCeiling) carpetArea * 4.2 else 0.0
    val marbleAddOn = if (includeItalianMarble) carpetArea * 12.0 else 0.0
    val automationAddOn = if (includeSmartAutomation) carpetArea * 4.8 else 0.0

    val subtotal = baseCivilAndWoodwork + kitchenAddOn + wardrobesAddOn + falseCeilingAddOn + marbleAddOn + automationAddOn
    val tax = subtotal * 0.09
    val contingency = subtotal * 0.05
    val grandTotal = subtotal + tax + contingency

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("budget_estimator_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Interior Budget Estimator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Live Rates",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Calculate custom architectural interior costs with transparent tier breakdowns.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Tier Selector
            Text(
                text = "1. Select Finish & Material Tier",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("STANDARD", "Standard", "$28/sqft"),
                    Triple("PREMIUM", "Premium", "$46/sqft"),
                    Triple("LUXURY", "Luxury", "$78/sqft")
                ).forEach { (tierKey, label, rate) ->
                    val isSelected = selectedTier == tierKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { selectedTier = tierKey }
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = rate,
                                fontSize = 11.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Carpet Area Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2. Carpet Area (Sq. Ft.)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${carpetArea.toInt()} sq. ft.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Slider(
                value = carpetArea,
                onValueChange = { carpetArea = it },
                valueRange = 400f..4000f,
                steps = 35,
                modifier = Modifier.testTag("carpet_area_slider")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Modular Add-ons
            Text(
                text = "3. Modular Rooms & Luxury Add-ons",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = includeKitchen,
                    onClick = { includeKitchen = !includeKitchen },
                    label = { Text("Modular Kitchen") },
                    leadingIcon = {
                        if (includeKitchen) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
                FilterChip(
                    selected = includeWardrobes,
                    onClick = { includeWardrobes = !includeWardrobes },
                    label = { Text("Wardrobes Suite") },
                    leadingIcon = {
                        if (includeWardrobes) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
                FilterChip(
                    selected = includeFalseCeiling,
                    onClick = { includeFalseCeiling = !includeFalseCeiling },
                    label = { Text("Acoustic False Ceiling") },
                    leadingIcon = {
                        if (includeFalseCeiling) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
                FilterChip(
                    selected = includeItalianMarble,
                    onClick = { includeItalianMarble = !includeItalianMarble },
                    label = { Text("Italian Marble Flooring") },
                    leadingIcon = {
                        if (includeItalianMarble) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
                FilterChip(
                    selected = includeSmartAutomation,
                    onClick = { includeSmartAutomation = !includeSmartAutomation },
                    label = { Text("Smart Home Automation") },
                    leadingIcon = {
                        if (includeSmartAutomation) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cost Summary Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Base Civil & Carpentry:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(currencyFormat.format(baseCivilAndWoodwork), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Selected Modular Add-ons:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(currencyFormat.format(kitchenAddOn + wardrobesAddOn + falseCeilingAddOn + marbleAddOn + automationAddOn), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Taxes (9%) & Contingency (5%):", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(currencyFormat.format(tax + contingency), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Estimated Investment", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Turnkey execution timeline: 8-12 wks", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = currencyFormat.format(grandTotal),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onBookConsultation(grandTotal, selectedTier, carpetArea.toInt()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("book_with_estimate_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Book Site Visit with this Estimate")
            }
        }
    }
}
