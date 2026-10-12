package com.example.ui.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.EcosystemRepository
import com.example.model.*
import com.example.ui.components.BeforeAfterSlider
import com.example.ui.components.BudgetEstimatorCard
import com.example.ui.components.StatusChip
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerHomeScreen(
    repository: EcosystemRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeTenantId by repository.activeTenantId.collectAsState()
    val allTenants by repository.tenants.collectAsState()
    val tenant = allTenants.find { it.id == activeTenantId } ?: allTenants.firstOrNull()

    val portfolio by repository.portfolio.collectAsState()
    val clientProjects by repository.clientProjects.collectAsState()
    val milestones by repository.milestones.collectAsState()
    val progressUpdates by repository.progressUpdates.collectAsState()
    val quotations by repository.quotations.collectAsState()
    val testimonials by repository.testimonials.collectAsState()
    val savedProjectIds by repository.savedProjectIds.collectAsState()

    var selectedCategory by remember { mutableStateOf(RoomCategory.ALL) }
    var selectedProjectForDetails by remember { mutableStateOf<PortfolioProject?>(null) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var prefilledBudget by remember { mutableDoubleStateOf(35000.0) }
    var prefilledTier by remember { mutableStateOf("PREMIUM") }
    var prefilledSqFt by remember { mutableIntStateOf(1450) }
    var activeTab by remember { mutableStateOf("EXPLORE") } // EXPLORE, MY_PROJECT, SAVED

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 }

    // Feature Flags for Customer App
    val isPortfolioEnabled = repository.isFeatureEnabled("portfolio_showcase", activeTenantId)
    val isBeforeAfterEnabled = repository.isFeatureEnabled("before_after_slider", activeTenantId)
    val isEstimatorEnabled = repository.isFeatureEnabled("budget_estimator", activeTenantId)
    val isBookingEnabled = repository.isFeatureEnabled("consultation_booking", activeTenantId)
    val isWhatsAppEnabled = repository.isFeatureEnabled("whatsapp_integration", activeTenantId)
    val isTrackingEnabled = repository.isFeatureEnabled("client_project_tracking", activeTenantId)
    val isReviewsEnabled = repository.isFeatureEnabled("customer_reviews", activeTenantId)

    val tenantBrandColor = try {
        Color(android.graphics.Color.parseColor(tenant?.brandColorHex ?: "#C59B27"))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Navigation Bar for Customer Sub-Tabs
        TabRow(
            selectedTabIndex = when (activeTab) {
                "EXPLORE" -> 0
                "MY_PROJECT" -> 1
                else -> 2
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = activeTab == "EXPLORE",
                onClick = { activeTab = "EXPLORE" },
                text = { Text("Studio & Portfolio") },
                icon = { Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeTab == "MY_PROJECT",
                onClick = { activeTab = "MY_PROJECT" },
                text = { Text("My Project Live") },
                icon = { Icon(Icons.Default.Engineering, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeTab == "SAVED",
                onClick = { activeTab = "SAVED" },
                text = { Text("Saved (${savedProjectIds.size})") },
                icon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        when (activeTab) {
            "EXPLORE" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. Hero Brand Banner
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customer_hero_banner"),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                            ) {
                                AsyncImage(
                                    model = tenant?.logoUrl?.ifEmpty { "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&q=80" },
                                    contentDescription = "Studio Interior",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(18.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tenantBrandColor
                                    ) {
                                        Text(
                                            text = "VERIFIED ARCHITECTURAL STUDIO",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = tenant?.name ?: "Interior Design Studio",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = tenant?.tagline ?: "Crafting timeless residential & commercial spaces",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.85f),
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    // 2. Quick Action Bar
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (isBookingEnabled) {
                                Button(
                                    onClick = { showBookingDialog = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("book_consultation_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = tenantBrandColor)
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Book Visit", fontSize = 12.sp)
                                }
                            }

                            if (isWhatsAppEnabled) {
                                OutlinedButton(
                                    onClick = {
                                        val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("https://wa.me/${tenant?.whatsappNumber ?: "18004567890"}?text=Hello%20${Uri.encode(tenant?.name ?: "Studio")},%20I%20would%20like%20to%20discuss%20my%20interior%20project.")
                                        }
                                        try {
                                            context.startActivity(whatsappIntent)
                                        } catch (e: Exception) {
                                            repository.logAudit("WHATSAPP_CLICKED", "Customer", "concierge", "Initiated WhatsApp chat")
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("whatsapp_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF25D366))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("WhatsApp", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // 3. Before-and-After Slider (Dynamic Feature Flag Protected)
                    if (isBeforeAfterEnabled) {
                        item {
                            BeforeAfterSlider(
                                beforeImageUrl = "https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=800&q=80",
                                afterImageUrl = "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80",
                                title = "Real Turnkey Transformation"
                            )
                        }
                    }

                    // 4. Budget Estimator (Dynamic Feature Flag Protected)
                    if (isEstimatorEnabled) {
                        item {
                            BudgetEstimatorCard(
                                onBookConsultation = { budget, tier, sqft ->
                                    prefilledBudget = budget
                                    prefilledTier = tier
                                    prefilledSqFt = sqft
                                    showBookingDialog = true
                                }
                            )
                        }
                    }

                    // 5. Portfolio Section with Category Pills
                    if (isPortfolioEnabled) {
                        item {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Room-Wise Portfolio",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${portfolio.size} Projects",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(RoomCategory.values()) { category ->
                                        FilterChip(
                                            selected = selectedCategory == category,
                                            onClick = { selectedCategory = category },
                                            label = { Text(category.displayName, fontSize = 12.sp) }
                                        )
                                    }
                                }
                            }
                        }

                        val filteredProjects = portfolio.filter {
                            selectedCategory == RoomCategory.ALL || it.roomCategory == selectedCategory.name
                        }

                        items(filteredProjects) { project ->
                            PortfolioProjectCard(
                                project = project,
                                isSaved = savedProjectIds.contains(project.id),
                                onSaveToggle = { repository.toggleSaveProject(project.id) },
                                onDetailsClick = { selectedProjectForDetails = project }
                            )
                        }
                    }

                    // 6. Testimonials Carousel
                    if (isReviewsEnabled && testimonials.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    text = "Client Reviews & Video Testimonials",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    items(testimonials) { test ->
                                        Card(
                                            modifier = Modifier.width(280.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row {
                                                        repeat(test.rating) {
                                                            Icon(
                                                                Icons.Default.Star,
                                                                contentDescription = null,
                                                                tint = Color(0xFFFFB300),
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                    if (test.videoUrl.isNotEmpty()) {
                                                        Surface(
                                                            shape = RoundedCornerShape(6.dp),
                                                            color = MaterialTheme.colorScheme.primaryContainer
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(12.dp))
                                                                Text("Video", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "\"${test.comment}\"",
                                                    fontSize = 12.sp,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    maxLines = 4,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = test.customerName,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = test.projectTitle,
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

                    // 7. Studio Info, Service Locations & Policies
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Studio Contact & Service Areas",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = tenant?.address ?: "Design District", fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = tenant?.businessHours ?: "Mon-Sat: 9AM-7PM", fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Serving: ${(tenant?.serviceLocations ?: listOf("Metro Area")).joinToString(", ")}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Privacy Policy • Terms of Service", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                    Text("v1.0.0", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            "MY_PROJECT" -> {
                // Client Live Project Tracking CUJ
                val clientProject = clientProjects.firstOrNull()
                val quotation = quotations.firstOrNull()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("client_project_summary_card"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusChip(status = clientProject?.status ?: "IN_PROGRESS")
                                    Text(
                                        text = "${clientProject?.progressPercent ?: 65}% Complete",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = clientProject?.title ?: "The Grand Residence",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = clientProject?.propertyAddress ?: "Marina Bay Penthouse",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                LinearProgressIndicator(
                                    progress = { (clientProject?.progressPercent ?: 65) / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Current Phase: ${clientProject?.currentMilestone ?: "Modular Joinery"}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Target: ${clientProject?.targetDate ?: "Nov 2026"}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    // Quotation Approval Card
                    if (quotation != null) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quotation_approval_card"),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Turnkey Quotation #${quotation.quoteNumber}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        StatusChip(status = quotation.status)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Total Investment: ${currencyFormat.format(quotation.grandTotal)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    quotation.itemsSummary.take(3).forEach { itemSummary ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(itemSummary, fontSize = 12.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    if (quotation.status == "SENT") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { repository.approveQuotation(quotation.id) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Approve Quotation")
                                            }
                                            OutlinedButton(
                                                onClick = { repository.requestQuotationRevision(quotation.id) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Request Revision")
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = "Status: ${quotation.status} on ${quotation.validUntil}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Milestone Timeline & Progress Photos
                    item {
                        Text(
                            text = "Site Milestones & Progress Photos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(milestones) { milestone ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = when (milestone.status) {
                                                "COMPLETED" -> Icons.Default.CheckCircle
                                                "IN_PROGRESS" -> Icons.Default.HourglassTop
                                                else -> Icons.Default.RadioButtonUnchecked
                                            },
                                            contentDescription = null,
                                            tint = when (milestone.status) {
                                                "COMPLETED" -> Color(0xFF2E7D32)
                                                "IN_PROGRESS" -> Color(0xFFF57F17)
                                                else -> Color.Gray
                                            },
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Phase ${milestone.stepOrder}: ${milestone.title}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    StatusChip(status = milestone.status)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = milestone.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (milestone.progressPhotos.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(milestone.progressPhotos) { photoUrl ->
                                            AsyncImage(
                                                model = photoUrl,
                                                contentDescription = "Site Inspection Photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "SAVED" -> {
                val savedProjects = portfolio.filter { savedProjectIds.contains(it.id) }
                if (savedProjects.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No saved designs yet", fontWeight = FontWeight.Bold)
                            Text("Browse the portfolio and tap the bookmark icon to save ideas.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(savedProjects) { project ->
                            PortfolioProjectCard(
                                project = project,
                                isSaved = true,
                                onSaveToggle = { repository.toggleSaveProject(project.id) },
                                onDetailsClick = { selectedProjectForDetails = project }
                            )
                        }
                    }
                }
            }
        }
    }

    // Booking Dialog
    if (showBookingDialog) {
        BookingModal(
            tenant = tenant,
            prefilledBudget = prefilledBudget,
            prefilledTier = prefilledTier,
            prefilledSqFt = prefilledSqFt,
            onDismiss = { showBookingDialog = false },
            onSubmit = { booking ->
                repository.createBooking(booking)
                repository.addLead(
                    Lead(
                        id = "lead_" + UUID.randomUUID().toString().take(6),
                        tenantId = activeTenantId,
                        customerName = booking.customerName,
                        phone = booking.customerPhone,
                        email = booking.customerEmail,
                        projectType = "${booking.preferredTier} Tier (${booking.carpetAreaSqFt} sqft)",
                        estimatedBudget = prefilledBudget,
                        carpetAreaSqFt = booking.carpetAreaSqFt,
                        status = "CONSULTATION_SCHEDULED",
                        notes = "Customer booked ${booking.consultationType} for ${booking.scheduledDate} at ${booking.propertyAddress}"
                    )
                )
                showBookingDialog = false
            }
        )
    }

    // Project Details Modal
    selectedProjectForDetails?.let { project ->
        ProjectDetailModal(
            project = project,
            onDismiss = { selectedProjectForDetails = null },
            onBookConsultation = {
                selectedProjectForDetails = null
                prefilledBudget = project.estimatedCost
                prefilledTier = project.budgetTier
                prefilledSqFt = project.carpetAreaSqFt
                showBookingDialog = true
            }
        )
    }
}

@Composable
fun PortfolioProjectCard(
    project: PortfolioProject,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailsClick() }
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                AsyncImage(
                    model = project.coverImage,
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Budget Tier badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = project.budgetTier,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Bookmark Button
                IconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save project",
                        tint = if (isSaved) Color(0xFFFFD54F) else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Walkthrough Video pill
                if (project.videoWalkthroughUrl.isNotEmpty()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Video Tour", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${project.style} • ${project.carpetAreaSqFt} sq. ft. • Turnkey in ${project.completionDurationWeeks} wks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BookingModal(
    tenant: Tenant?,
    prefilledBudget: Double,
    prefilledTier: String,
    prefilledSqFt: Int,
    onDismiss: () -> Unit,
    onSubmit: (Booking) -> Unit
) {
    var customerName by remember { mutableStateOf("Elena Vance") }
    var customerPhone by remember { mutableStateOf("+1 (555) 782-9900") }
    var customerEmail by remember { mutableStateOf("elena.vance@gmail.com") }
    var propertyAddress by remember { mutableStateOf("402 Skyline Blvd, Apt 14B") }
    var consultationType by remember { mutableStateOf("SITE_VISIT") } // SITE_VISIT, VIRTUAL_CALL, SHOWROOM_VISIT
    var scheduledDate by remember { mutableStateOf("2026-10-25") }
    var timeSlot by remember { mutableStateOf("10:30 AM - 12:00 PM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Book Architectural Consultation") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Studio: ${tenant?.name ?: "Interior Design Studio"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = propertyAddress,
                    onValueChange = { propertyAddress = it },
                    label = { Text("Property Site Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Consultation Mode:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("SITE_VISIT" to "Site Visit", "VIRTUAL_CALL" to "Virtual 3D", "SHOWROOM_VISIT" to "Studio").forEach { (type, label) ->
                        FilterChip(
                            selected = consultationType == type,
                            onClick = { consultationType = type },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = scheduledDate,
                        onValueChange = { scheduledDate = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeSlot,
                        onValueChange = { timeSlot = it },
                        label = { Text("Time Slot") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val booking = Booking(
                        id = "bk_" + UUID.randomUUID().toString().take(6),
                        tenantId = tenant?.id ?: "tenant_default",
                        customerId = "current_user",
                        customerName = customerName,
                        customerPhone = customerPhone,
                        customerEmail = customerEmail,
                        propertyAddress = propertyAddress,
                        consultationType = consultationType,
                        scheduledDate = scheduledDate,
                        timeSlot = timeSlot,
                        carpetAreaSqFt = prefilledSqFt,
                        preferredTier = prefilledTier,
                        status = "CONFIRMED",
                        assignedStaff = "Lead Architectural Designer",
                        notes = "Estimated budget approx $${prefilledBudget.toInt()}"
                    )
                    onSubmit(booking)
                }
            ) {
                Text("Confirm Booking")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ProjectDetailModal(
    project: PortfolioProject,
    onDismiss: () -> Unit,
    onBookConsultation: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(project.title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = project.coverImage,
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Text(
                    text = "Style: ${project.style} • Category: ${project.roomCategory}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Carpet Area: ${project.carpetAreaSqFt} sq. ft. • Turnkey Cost: ${currencyFormat.format(project.estimatedCost)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = project.description,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (project.galleryImages.isNotEmpty()) {
                    Text("Gallery Details:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(project.galleryImages) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onBookConsultation) {
                Text("Book Visit for this Design")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
