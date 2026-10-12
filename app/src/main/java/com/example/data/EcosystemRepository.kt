package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class EcosystemRepository(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val tag = "EcosystemRepo"

    // Cloud Firestore initialized with named database ID from firebase_applet_config.xml
    private val db: FirebaseFirestore by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            FirebaseFirestore.getInstance(dbId)
        } catch (e: Exception) {
            Log.w(tag, "Could not initialize named database ID, fallback instance", e)
            FirebaseFirestore.getInstance()
        }
    }

    private val auth = Firebase.auth

    // Navigation and active session state
    private val _currentApp = MutableStateFlow(EcosystemApp.CUSTOMER_APP)
    val currentApp: StateFlow<EcosystemApp> = _currentApp.asStateFlow()

    private val _activeTenantId = MutableStateFlow("tenant_default")
    val activeTenantId: StateFlow<String> = _activeTenantId.asStateFlow()

    private val _currentUserRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    // Persistent reactive state collections
    private val _tenants = MutableStateFlow(SeedData.initialTenants)
    val tenants: StateFlow<List<Tenant>> = _tenants.asStateFlow()

    private val _featureFlags = MutableStateFlow(SeedData.initialFeatureFlags)
    val featureFlags: StateFlow<List<FeatureFlag>> = _featureFlags.asStateFlow()

    private val _portfolio = MutableStateFlow(SeedData.initialPortfolio)
    val portfolio: StateFlow<List<PortfolioProject>> = _portfolio.asStateFlow()

    private val _leads = MutableStateFlow(SeedData.initialLeads)
    val leads: StateFlow<List<Lead>> = _leads.asStateFlow()

    private val _bookings = MutableStateFlow(SeedData.initialBookings)
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _clientProjects = MutableStateFlow(listOf(SeedData.initialClientProject))
    val clientProjects: StateFlow<List<ClientProject>> = _clientProjects.asStateFlow()

    private val _milestones = MutableStateFlow(SeedData.initialMilestones)
    val milestones: StateFlow<List<ProjectMilestone>> = _milestones.asStateFlow()

    private val _progressUpdates = MutableStateFlow(SeedData.initialProgressUpdates)
    val progressUpdates: StateFlow<List<ProgressUpdate>> = _progressUpdates.asStateFlow()

    private val _quotations = MutableStateFlow(listOf(SeedData.initialQuotation))
    val quotations: StateFlow<List<Quotation>> = _quotations.asStateFlow()

    private val _invoices = MutableStateFlow(SeedData.initialInvoices)
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _testimonials = MutableStateFlow(SeedData.initialTestimonials)
    val testimonials: StateFlow<List<Testimonial>> = _testimonials.asStateFlow()

    private val _auditLogs = MutableStateFlow(SeedData.initialAuditLogs)
    val auditLogs: StateFlow<List<AuditLog>> = _auditLogs.asStateFlow()

    private val _savedProjectIds = MutableStateFlow<Set<String>>(setOf("proj_01", "proj_03"))
    val savedProjectIds: StateFlow<Set<String>> = _savedProjectIds.asStateFlow()

    init {
        // Observe cloud data if authenticated
        syncWithFirestoreIfAvailable()
    }

    private fun syncWithFirestoreIfAvailable() {
        if (auth.currentUser != null) {
            try {
                db.collection("tenants").addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(tag, "Firestore listener: ${e.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val cloudTenants = snapshot.toObjects(Tenant::class.java)
                        if (cloudTenants.isNotEmpty()) {
                            _tenants.value = cloudTenants
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(tag, "Initial Firestore sync skipped: ${e.message}")
            }
        }
    }

    // Role & App Switchers
    fun selectApp(app: EcosystemApp) {
        _currentApp.value = app
        when (app) {
            EcosystemApp.CUSTOMER_APP -> _currentUserRole.value = UserRole.CUSTOMER
            EcosystemApp.BUSINESS_OWNER_APP -> _currentUserRole.value = UserRole.BUSINESS_OWNER
            EcosystemApp.DEVELOPER_SUPER_ADMIN_APP -> _currentUserRole.value = UserRole.SUPER_ADMIN
        }
    }

    fun selectTenant(tenantId: String) {
        _activeTenantId.value = tenantId
        logAudit("TENANT_SWITCHED", "Tenant", tenantId, "Switched active ecosystem tenant context to $tenantId")
    }

    // Dynamic Feature Flag Checking
    fun isFeatureEnabled(featureKey: String, tenantId: String = _activeTenantId.value): Boolean {
        val flag = _featureFlags.value.find { it.key == featureKey } ?: return false
        // 1. Tenant specific override has highest priority
        flag.tenantOverrides[tenantId]?.let { return it }
        // 2. Global flag setting
        return flag.defaultEnabled
    }

    // Feature Management Center Actions
    fun toggleFeatureGlobally(key: String, enabled: Boolean) {
        val previous = _featureFlags.value.find { it.key == key }
        _featureFlags.update { flags ->
            flags.map {
                if (it.key == key) it.copy(defaultEnabled = enabled) else it
            }
        }
        logAudit(
            if (enabled) "FEATURE_ENABLED_GLOBAL" else "FEATURE_DISABLED_GLOBAL",
            "FeatureFlag",
            key,
            "Toggled $key globally to $enabled (was ${previous?.defaultEnabled})"
        )
    }

    fun toggleFeatureForTenant(key: String, tenantId: String, enabled: Boolean) {
        _featureFlags.update { flags ->
            flags.map {
                if (it.key == key) {
                    val updatedOverrides = it.tenantOverrides.toMutableMap()
                    updatedOverrides[tenantId] = enabled
                    it.copy(tenantOverrides = updatedOverrides)
                } else it
            }
        }
        logAudit(
            "FEATURE_TENANT_OVERRIDE",
            "FeatureFlag",
            key,
            "Set tenant $tenantId override for $key to $enabled"
        )
    }

    fun rollbackFeature(key: String) {
        _featureFlags.update { flags ->
            flags.map {
                if (it.key == key) {
                    it.copy(
                        defaultEnabled = true,
                        tenantOverrides = emptyMap(),
                        rolloutPercentage = 100
                    )
                } else it
            }
        }
        logAudit("FEATURE_ROLLBACK", "FeatureFlag", key, "Rolled back $key to last known good configuration")
    }

    fun updateFeatureConfig(key: String, configJson: String) {
        _featureFlags.update { flags ->
            flags.map {
                if (it.key == key) it.copy(currentConfigJson = configJson) else it
            }
        }
        logAudit("FEATURE_CONFIG_UPDATED", "FeatureFlag", key, "Updated configuration schema for $key")
    }

    // Tenant Management Actions
    fun updateTenantName(tenantId: String, newName: String) {
        _tenants.update { list ->
            list.map {
                if (it.id == tenantId) it.copy(name = newName) else it
            }
        }
        logAudit("TENANT_NAME_UPDATED", "Tenant", tenantId, "Updated business name to '$newName'")
    }

    fun updateTenantBranding(
        tenantId: String,
        name: String,
        brandColorHex: String,
        tagline: String,
        email: String,
        phone: String,
        hours: String
    ) {
        _tenants.update { list ->
            list.map {
                if (it.id == tenantId) {
                    it.copy(
                        name = name,
                        brandColorHex = brandColorHex,
                        tagline = tagline,
                        contactEmail = email,
                        contactPhone = phone,
                        businessHours = hours
                    )
                } else it
            }
        }
        logAudit("TENANT_BRANDING_UPDATED", "Tenant", tenantId, "Updated branding, contacts & business hours")
    }

    fun setTenantActive(tenantId: String, active: Boolean) {
        _tenants.update { list ->
            list.map {
                if (it.id == tenantId) it.copy(active = active) else it
            }
        }
        logAudit(
            if (active) "TENANT_ACTIVATED" else "TENANT_SUSPENDED",
            "Tenant",
            tenantId,
            "Changed tenant active state to $active"
        )
    }

    fun registerNewTenant(name: String, brandColor: String, plan: String): String {
        val newId = "tenant_" + UUID.randomUUID().toString().take(6)
        val newTenant = Tenant(
            id = newId,
            name = name,
            defaultName = name,
            brandColorHex = brandColor,
            plan = plan,
            active = true
        )
        _tenants.update { it + newTenant }
        logAudit("TENANT_REGISTERED", "Tenant", newId, "Registered new tenant business '$name' on $plan plan")
        return newId
    }

    // Lead CRM Actions
    fun addLead(lead: Lead) {
        _leads.update { listOf(lead) + it }
        logAudit("LEAD_CREATED", "Lead", lead.id, "New lead enquiry from ${lead.customerName} for ${lead.projectType}")
    }

    fun updateLeadStatus(leadId: String, newStatus: String) {
        _leads.update { list ->
            list.map {
                if (it.id == leadId) it.copy(status = newStatus) else it
            }
        }
        logAudit("LEAD_STATUS_CHANGED", "Lead", leadId, "Advanced lead status to $newStatus")
    }

    // Booking Actions
    fun createBooking(booking: Booking) {
        _bookings.update { listOf(booking) + it }
        logAudit("BOOKING_CREATED", "Booking", booking.id, "Booked ${booking.consultationType} for ${booking.customerName} on ${booking.scheduledDate}")
    }

    fun updateBookingStatus(bookingId: String, newStatus: String, assignedStaff: String? = null) {
        _bookings.update { list ->
            list.map {
                if (it.id == bookingId) {
                    it.copy(
                        status = newStatus,
                        assignedStaff = assignedStaff ?: it.assignedStaff
                    )
                } else it
            }
        }
        logAudit("BOOKING_STATUS_CHANGED", "Booking", bookingId, "Status updated to $newStatus")
    }

    // Portfolio Management Actions
    fun addPortfolioProject(project: PortfolioProject) {
        _portfolio.update { listOf(project) + it }
        logAudit("PORTFOLIO_ADDED", "PortfolioProject", project.id, "Added new project '${project.title}'")
    }

    fun toggleSaveProject(projectId: String) {
        _savedProjectIds.update { set ->
            if (set.contains(projectId)) set - projectId else set + projectId
        }
    }

    // Quotation & Client Milestone Actions
    fun approveQuotation(quotationId: String) {
        _quotations.update { list ->
            list.map {
                if (it.id == quotationId) it.copy(status = "APPROVED") else it
            }
        }
        logAudit("QUOTATION_APPROVED", "Quotation", quotationId, "Client digitally approved quotation $quotationId")
    }

    fun requestQuotationRevision(quotationId: String) {
        _quotations.update { list ->
            list.map {
                if (it.id == quotationId) it.copy(status = "REVISION_REQUESTED") else it
            }
        }
        logAudit("QUOTATION_REVISION_REQUESTED", "Quotation", quotationId, "Client requested quotation revisions")
    }

    fun addProgressUpdate(update: ProgressUpdate) {
        _progressUpdates.update { listOf(update) + it }
        logAudit("PROGRESS_UPDATE_POSTED", "ProgressUpdate", update.id, "Site supervisor added photo progress update")
    }

    fun recordInvoicePayment(invoiceId: String) {
        _invoices.update { list ->
            list.map {
                if (it.id == invoiceId) it.copy(status = "PAID", paidDate = "2026-10-11") else it
            }
        }
        logAudit("INVOICE_PAID", "Invoice", invoiceId, "Marked invoice $invoiceId as PAID")
    }

    // Audit Logging Helper
    fun logAudit(action: String, entityType: String, entityId: String, details: String) {
        val entry = AuditLog(
            id = "log_" + UUID.randomUUID().toString().take(8),
            tenantId = _activeTenantId.value,
            actorId = when (_currentApp.value) {
                EcosystemApp.CUSTOMER_APP -> "usr_customer_demo"
                EcosystemApp.BUSINESS_OWNER_APP -> "usr_owner_admin"
                EcosystemApp.DEVELOPER_SUPER_ADMIN_APP -> "usr_superadmin"
            },
            actorRole = _currentUserRole.value.name,
            action = action,
            entityType = entityType,
            entityId = entityId,
            details = details
        )
        _auditLogs.update { listOf(entry) + it }
        Log.i(tag, "[AUDIT] $action on $entityType:$entityId - $details")
    }
}
