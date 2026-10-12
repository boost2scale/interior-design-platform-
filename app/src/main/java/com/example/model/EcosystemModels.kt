package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class UserRole {
    CUSTOMER,
    BUSINESS_OWNER,
    SUPER_ADMIN,
    STAFF_DESIGNER
}

enum class EcosystemApp {
    CUSTOMER_APP,
    BUSINESS_OWNER_APP,
    DEVELOPER_SUPER_ADMIN_APP
}

@IgnoreExtraProperties
data class Tenant(
    @DocumentId val id: String = "",
    val name: String = "Interior Design Studio",
    val defaultName: String = "Interior Design Studio",
    val tagline: String = "Crafting timeless residential & commercial spaces",
    val logoUrl: String = "",
    val brandColorHex: String = "#C59B27", // Luxury warm gold
    val contactEmail: String = "hello@interiordesignstudio.com",
    val contactPhone: String = "+1 (800) 456-7890",
    val whatsappNumber: String = "+18004567890",
    val address: String = "742 Evergreen Terrace, Suite 400, Design District",
    val serviceLocations: List<String> = listOf("Metro Downtown", "North Hills", "West End", "Greenwood", "Lakeside"),
    val businessHours: String = "Mon - Sat: 9:00 AM - 7:00 PM",
    val plan: String = "ENTERPRISE",
    val active: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FeatureFlag(
    @DocumentId val key: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "Core",
    val defaultEnabled: Boolean = true,
    val minAppVersion: String = "1.0.0",
    val isRolloutSupported: Boolean = true,
    val tenantOverrides: Map<String, Boolean> = emptyMap(),
    val rolloutPercentage: Int = 100,
    val lastKnownGoodConfig: String = "{}",
    val currentConfigJson: String = "{}"
)

@IgnoreExtraProperties
data class Lead(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val customerName: String = "",
    val phone: String = "",
    val email: String = "",
    val projectType: String = "Complete Home", // Living Room, Kitchen, Villa, Commercial
    val estimatedBudget: Double = 35000.0,
    val carpetAreaSqFt: Int = 1450,
    val status: String = "NEW", // NEW, CONTACTED, QUALIFIED, CONSULTATION_SCHEDULED, PROPOSAL_SENT, WON, LOST
    val notes: String = "",
    val source: String = "Mobile App",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

enum class RoomCategory(val displayName: String) {
    ALL("All Rooms"),
    LIVING_ROOM("Living Rooms"),
    BEDROOM("Bedrooms"),
    MODULAR_KITCHEN("Modular Kitchens"),
    WARDROBES("Wardrobes"),
    FALSE_CEILINGS("False Ceilings"),
    COMPLETE_HOME("Complete Homes"),
    COMMERCIAL("Commercial Interiors")
}

@IgnoreExtraProperties
data class PortfolioProject(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val title: String = "",
    val roomCategory: String = RoomCategory.LIVING_ROOM.name,
    val style: String = "Modern Scandinavian", // Contemporary, Neo-Classical, Minimalist, Japandi, Industrial
    val carpetAreaSqFt: Int = 450,
    val budgetTier: String = "PREMIUM", // STANDARD, PREMIUM, LUXURY
    val estimatedCost: Double = 18500.0,
    val coverImage: String = "",
    val beforeImage: String = "",
    val afterImage: String = "",
    val galleryImages: List<String> = emptyList(),
    val videoWalkthroughUrl: String = "",
    val description: String = "",
    val clientName: String = "",
    val completionDurationWeeks: Int = 6,
    val featured: Boolean = true,
    val savedByUsers: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Booking(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val customerEmail: String = "",
    val consultationType: String = "SITE_VISIT", // SITE_VISIT, VIRTUAL_CALL, SHOWROOM_VISIT
    val scheduledDate: String = "2026-10-20",
    val timeSlot: String = "10:30 AM - 12:00 PM",
    val propertyAddress: String = "",
    val carpetAreaSqFt: Int = 1200,
    val preferredTier: String = "PREMIUM",
    val status: String = "CONFIRMED", // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val assignedStaff: String = "Sarah Jenkins (Senior Interior Architect)",
    val notes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ClientProject(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val customerId: String = "",
    val customerName: String = "",
    val title: String = "The Grand Horizon Penthouse",
    val propertyAddress: String = "402 Skyline Blvd, Apt 14B",
    val totalQuotation: Double = 68000.0,
    val paidAmount: Double = 40800.0,
    val status: String = "IN_PROGRESS", // PLANNING, IN_PROGRESS, FINISHING, HANDOVER, COMPLETED
    val progressPercent: Int = 65,
    val currentMilestone: String = "Modular Joinery & False Ceilings",
    val startDate: String = "2026-08-15",
    val targetDate: String = "2026-11-30",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ProjectMilestone(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val projectId: String = "",
    val title: String = "",
    val description: String = "",
    val stepOrder: Int = 1,
    val status: String = "COMPLETED", // PENDING, IN_PROGRESS, COMPLETED
    val completionDate: String? = null,
    val approvedByCustomer: Boolean = true,
    val progressPhotos: List<String> = emptyList()
)

@IgnoreExtraProperties
data class ProgressUpdate(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val projectId: String = "",
    val milestoneId: String = "",
    val title: String = "",
    val note: String = "",
    val photoUrls: List<String> = emptyList(),
    val submittedBy: String = "Lead Site Supervisor",
    val updateDate: String = "2026-10-10",
    val authorized: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Quotation(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val projectId: String = "",
    val customerId: String = "",
    val quoteNumber: String = "QT-2026-0842",
    val title: String = "Comprehensive Interior Turnkey Quotation",
    val tier: String = "PREMIUM",
    val itemsSummary: List<String> = listOf("Civil & Demolition Works", "Custom Modular Woodwork", "Italian Marble Flooring & Skirting", "Multi-zone Designer False Ceiling", "Premium PU Paint & Accent Wall"),
    val subtotal: Double = 62500.0,
    val taxAmount: Double = 5625.0,
    val contingencyAmount: Double = 3125.0,
    val grandTotal: Double = 71250.0,
    val status: String = "SENT", // DRAFT, SENT, APPROVED, REVISION_REQUESTED
    val validUntil: String = "2026-11-15",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Invoice(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val projectId: String = "",
    val customerName: String = "",
    val invoiceNumber: String = "INV-2026-104",
    val description: String = "Milestone 2: Modular Carpentry & Electrical Rough-in",
    val amount: Double = 22500.0,
    val status: String = "PAID", // UNPAID, PARTIALLY_PAID, PAID, OVERDUE
    val dueDate: String = "2026-10-05",
    val paidDate: String? = "2026-10-03",
    val paymentMethod: String = "Bank Wire Transfer",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PricingRule(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val tier: String = "STANDARD", // STANDARD, PREMIUM, LUXURY
    val ratePerSqFt: Double = 30.0,
    val falseCeilingRateSqFt: Double = 4.5,
    val modularKitchenBase: Double = 6500.0,
    val wardrobePerUnit: Double = 2200.0,
    val luxuryAutomationPerSqFt: Double = 6.0,
    val taxRatePercent: Double = 9.0,
    val contingencyPercent: Double = 5.0,
    val exclusions: List<String> = listOf("Structural civil alterations requiring local council permit", "External facade modifications")
)

@IgnoreExtraProperties
data class Testimonial(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val customerName: String = "David & Elena Vance",
    val projectTitle: String = "4-BHK Luxury Waterfront Apartment",
    val rating: Int = 5,
    val comment: String = "The attention to detail, transparent pricing, and daily photo tracking gave us total peace of mind. Truly world-class interior execution!",
    val videoUrl: String = "https://example.com/testimonials/vance.mp4",
    val avatarUrl: String = "",
    val verifiedProject: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AuditLog(
    @DocumentId val id: String = "",
    val tenantId: String = "tenant_default",
    val actorId: String = "usr_superadmin",
    val actorRole: String = "SUPER_ADMIN",
    val action: String = "FEATURE_ROLLOUT_UPDATED",
    val entityType: String = "FeatureFlag",
    val entityId: String = "budget_estimator",
    val details: String = "Set rollout to 100% and enabled for tenant_default",
    @ServerTimestamp val timestamp: Timestamp? = null
)
