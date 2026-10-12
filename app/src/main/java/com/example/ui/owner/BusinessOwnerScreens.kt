package com.example.ui.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.EcosystemRepository
import com.example.model.*
import com.example.ui.components.MetricCard
import com.example.ui.components.StatusChip
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@Composable
fun BusinessOwnerDashboardScreen(
    repository: EcosystemRepository,
    modifier: Modifier = Modifier
) {
    val activeTenantId by repository.activeTenantId.collectAsState()
    val allTenants by repository.tenants.collectAsState()
    val tenant = allTenants.find { it.id == activeTenantId } ?: allTenants.firstOrNull()

    val leads by repository.leads.collectAsState()
    val bookings by repository.bookings.collectAsState()
    val portfolio by repository.portfolio.collectAsState()
    val clientProjects by repository.clientProjects.collectAsState()
    val quotations by repository.quotations.collectAsState()
    val invoices by repository.invoices.collectAsState()
    val auditLogs by repository.auditLogs.collectAsState()

    var activeOwnerSection by remember { mutableStateOf("OVERVIEW") } // OVERVIEW, CRM, PORTFOLIO, PRICING, BOOKINGS, PROJECTS, SETTINGS

    // Modals
    var showAddProjectDialog by remember { mutableStateOf(false) }
    var showAddPhotoDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 }
    val totalRevenue = invoices.filter { it.status == "PAID" }.sumOf { it.amount }
    val pendingRevenue = invoices.filter { it.status != "PAID" }.sumOf { it.amount }

    Column(modifier = modifier.fillMaxSize()) {
        // Owner Navigation Sub-Tabs (Scrollable)
        ScrollableTabRow(
            selectedTabIndex = when (activeOwnerSection) {
                "OVERVIEW" -> 0
                "CRM" -> 1
                "PORTFOLIO" -> 2
                "PRICING" -> 3
                "BOOKINGS" -> 4
                "PROJECTS" -> 5
                else -> 6
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            listOf(
                "OVERVIEW" to "Dashboard",
                "CRM" to "Lead CRM (${leads.size})",
                "PORTFOLIO" to "Portfolio (${portfolio.size})",
                "PRICING" to "Pricing & Rates",
                "BOOKINGS" to "Site Visits (${bookings.size})",
                "PROJECTS" to "Projects & Billing",
                "SETTINGS" to "Studio Branding"
            ).forEach { (section, label) ->
                Tab(
                    selected = activeOwnerSection == section,
                    onClick = { activeOwnerSection = section },
                    text = { Text(label, fontSize = 12.sp) }
                )
            }
        }

        when (activeOwnerSection) {
            "OVERVIEW" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Banner with editable Business Name
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Studio Executive Console",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = tenant?.name ?: "Interior Design Studio",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Tenant ID: ${tenant?.id} • Plan: ${tenant?.plan}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { showSettingsDialog = true },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Edit Studio")
                                }
                            }
                        }
                    }

                    // Key Performance Indicators (KPIs)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                MetricCard(
                                    title = "Active Leads",
                                    value = "${leads.size}",
                                    subtitle = "${leads.count { it.status == "NEW" }} new enquiries",
                                    icon = Icons.Default.Groups,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Site Visits Booked",
                                    value = "${bookings.size}",
                                    subtitle = "Upcoming visits",
                                    icon = Icons.Default.CalendarToday,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                MetricCard(
                                    title = "Revenue Collected",
                                    value = currencyFormat.format(totalRevenue),
                                    subtitle = "${invoices.count { it.status == "PAID" }} milestones settled",
                                    icon = Icons.Default.AttachMoney,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Pipeline Value",
                                    value = currencyFormat.format(pendingRevenue + totalRevenue),
                                    subtitle = "Ongoing executions",
                                    icon = Icons.Default.TrendingUp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Recent Leads Quick Summary
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Enquiry Pipeline",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = { activeOwnerSection = "CRM" }) {
                                Text("View CRM Pipeline")
                            }
                        }
                    }

                    items(leads.take(3)) { lead ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = lead.customerName, fontWeight = FontWeight.Bold)
                                    Text(text = "${lead.projectType} • Est. ${currencyFormat.format(lead.estimatedBudget)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                StatusChip(status = lead.status)
                            }
                        }
                    }

                    // Studio Operational Audit Log
                    item {
                        Text(
                            text = "Recent Studio Operational Logs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(auditLogs.take(4)) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(text = log.actorRole, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = log.details, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            "CRM" -> {
                // Interactive Lead CRM Pipeline
                var filterStatus by remember { mutableStateOf("ALL") }
                val stages = listOf("ALL", "NEW", "CONTACTED", "QUALIFIED", "CONSULTATION_SCHEDULED", "PROPOSAL_SENT", "WON", "LOST")

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(stages) { stage ->
                                FilterChip(
                                    selected = filterStatus == stage,
                                    onClick = { filterStatus = stage },
                                    label = { Text(stage, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    val filteredLeads = leads.filter { filterStatus == "ALL" || it.status == filterStatus }

                    items(filteredLeads) { lead ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = lead.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = "${lead.phone} • ${lead.email}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    StatusChip(status = lead.status)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Scope: ${lead.projectType} (${lead.carpetAreaSqFt} sq. ft.) • Budget: ${currencyFormat.format(lead.estimatedBudget)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (lead.notes.isNotEmpty()) {
                                    Text(text = "Notes: ${lead.notes}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(modifier = Modifier.height(12.dp))

                                // Quick Stage Transition Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (lead.status == "NEW") {
                                        OutlinedButton(
                                            onClick = { repository.updateLeadStatus(lead.id, "CONTACTED") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Mark Contacted", fontSize = 11.sp)
                                        }
                                    }
                                    if (lead.status in listOf("NEW", "CONTACTED")) {
                                        Button(
                                            onClick = { repository.updateLeadStatus(lead.id, "QUALIFIED") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Qualify", fontSize = 11.sp)
                                        }
                                    }
                                    if (lead.status == "QUALIFIED") {
                                        Button(
                                            onClick = { repository.updateLeadStatus(lead.id, "CONSULTATION_SCHEDULED") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Schedule Visit", fontSize = 11.sp)
                                        }
                                    }
                                    if (lead.status == "CONSULTATION_SCHEDULED") {
                                        Button(
                                            onClick = { repository.updateLeadStatus(lead.id, "PROPOSAL_SENT") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Send Proposal", fontSize = 11.sp)
                                        }
                                    }
                                    if (lead.status == "PROPOSAL_SENT") {
                                        Button(
                                            onClick = { repository.updateLeadStatus(lead.id, "WON") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                        ) {
                                            Text("Mark Won", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "PORTFOLIO" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Published Design Portfolio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { showAddProjectDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Project")
                            }
                        }
                    }

                    items(portfolio) { proj ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = proj.coverImage,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = proj.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = "Category: ${proj.roomCategory} • Tier: ${proj.budgetTier}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "Carpet Area: ${proj.carpetAreaSqFt} sqft • Turnkey: ${currencyFormat.format(proj.estimatedCost)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            "PRICING" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "Configurable Studio Pricing Engine",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Configure base per sq. ft. formulas for Standard, Premium & Luxury customer quotes.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Standard Tier", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text("Base: $28.00 / sq. ft. • Modular Kitchen: $6,000 base • Wardrobes: $4,500/unit", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Premium Tier (Most Popular)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text("Base: $46.00 / sq. ft. • Modular Kitchen: $11,500 base • Wardrobes: $8,800/unit", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Luxury Tier (Artisanal Finishes)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text("Base: $78.00 / sq. ft. • Modular Kitchen: $19,500 base • Wardrobes: $15,000/unit", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Add-ons & Statutory Taxes", fontWeight = FontWeight.Bold)
                                Text("Acoustic Gypsum: $4.20/sqft • Italian Marble: $12.00/sqft • Automation: $4.80/sqft • GST/Sales Tax: 9% • Contingency: 5%", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            "BOOKINGS" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "Consultation & Site Visit Schedule",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(bookings) { booking ->
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = booking.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = booking.customerPhone, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    StatusChip(status = booking.status)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Date: ${booking.scheduledDate} (${booking.timeSlot})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text(text = "Mode: ${booking.consultationType} • Site: ${booking.propertyAddress}", fontSize = 12.sp)
                                Text(text = "Assigned Lead: ${booking.assignedStaff}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { repository.updateBookingStatus(booking.id, "COMPLETED") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Completed", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "PROJECTS" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Turnkey Projects & Site Photos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Button(onClick = { showAddPhotoDialog = true }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Upload Site Update")
                            }
                        }
                    }

                    items(invoices) { inv ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "${inv.invoiceNumber}: ${inv.description}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Client: ${inv.customerName} • Due: ${inv.dueDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "Amount: ${currencyFormat.format(inv.amount)}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                                }
                                if (inv.status != "PAID") {
                                    Button(
                                        onClick = { repository.recordInvoicePayment(inv.id) },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Paid", fontSize = 11.sp)
                                    }
                                } else {
                                    StatusChip(status = "PAID")
                                }
                            }
                        }
                    }
                }
            }

            "SETTINGS" -> {
                // Business Profile & Branding Settings
                showSettingsDialog = true
                activeOwnerSection = "OVERVIEW"
            }
        }
    }

    // Modal: Edit Business Profile & Branding
    if (showSettingsDialog) {
        var businessName by remember { mutableStateOf(tenant?.name ?: "Interior Design Studio") }
        var tagline by remember { mutableStateOf(tenant?.tagline ?: "") }
        var brandColor by remember { mutableStateOf(tenant?.brandColorHex ?: "#C59B27") }
        var email by remember { mutableStateOf(tenant?.contactEmail ?: "") }
        var phone by remember { mutableStateOf(tenant?.contactPhone ?: "") }
        var hours by remember { mutableStateOf(tenant?.businessHours ?: "Mon - Sat: 9:00 AM - 7:00 PM") }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Studio Profile & Branding") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Customize the business name and visual identity for this tenant.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business Name (Default: Interior Design Studio)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tagline,
                        onValueChange = { tagline = it },
                        label = { Text("Studio Tagline") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = brandColor,
                        onValueChange = { brandColor = it },
                        label = { Text("Brand Accent Color Hex (e.g. #C59B27)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Concierge Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone / WhatsApp") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = hours,
                        onValueChange = { hours = it },
                        label = { Text("Business Hours") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.updateTenantBranding(
                            tenantId = activeTenantId,
                            name = businessName,
                            brandColorHex = brandColor,
                            tagline = tagline,
                            email = email,
                            phone = phone,
                            hours = hours
                        )
                        showSettingsDialog = false
                    }
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Modal: Add Portfolio Project
    if (showAddProjectDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("LIVING_ROOM") }
        var style by remember { mutableStateOf("Contemporary Luxury") }
        var carpetArea by remember { mutableStateOf("600") }
        var cost by remember { mutableStateOf("25000") }
        var coverUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80") }

        AlertDialog(
            onDismissRequest = { showAddProjectDialog = false },
            title = { Text("Publish New Portfolio Project") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Project Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = style, onValueChange = { style = it }, label = { Text("Architectural Style") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = carpetArea, onValueChange = { carpetArea = it }, label = { Text("Carpet Area (sqft)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = cost, onValueChange = { cost = it }, label = { Text("Estimated Cost ($)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = coverUrl, onValueChange = { coverUrl = it }, label = { Text("Cover Image URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotEmpty()) {
                            repository.addPortfolioProject(
                                PortfolioProject(
                                    id = "proj_" + UUID.randomUUID().toString().take(6),
                                    tenantId = activeTenantId,
                                    title = title,
                                    roomCategory = category,
                                    style = style,
                                    carpetAreaSqFt = carpetArea.toIntOrNull() ?: 500,
                                    estimatedCost = cost.toDoubleOrNull() ?: 20000.0,
                                    coverImage = coverUrl,
                                    description = "Custom architectural finish with precision joinery."
                                )
                            )
                            showAddProjectDialog = false
                        }
                    }
                ) {
                    Text("Publish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProjectDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Modal: Upload Site Update
    if (showAddPhotoDialog) {
        var updateTitle by remember { mutableStateOf("Ceiling Cove Lighting & Paint Primer") }
        var supervisorNote by remember { mutableStateOf("Electrical inspections cleared and dry-run completed.") }

        AlertDialog(
            onDismissRequest = { showAddPhotoDialog = false },
            title = { Text("Authorized Supervisor Site Update") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = updateTitle, onValueChange = { updateTitle = it }, label = { Text("Update Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = supervisorNote, onValueChange = { supervisorNote = it }, label = { Text("Site Supervisor Log Note") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.addProgressUpdate(
                            ProgressUpdate(
                                id = "upd_" + UUID.randomUUID().toString().take(6),
                                tenantId = activeTenantId,
                                projectId = "proj_client_vance",
                                milestoneId = "ms_03",
                                title = updateTitle,
                                note = supervisorNote,
                                photoUrls = listOf("https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=600&q=80"),
                                submittedBy = "Lead Site Architect",
                                authorized = true
                            )
                        )
                        showAddPhotoDialog = false
                    }
                ) {
                    Text("Post to Client App")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPhotoDialog = false }) { Text("Cancel") }
            }
        )
    }
}
