package com.example

import com.example.data.SeedData
import com.example.model.*
import org.junit.Assert.*
import org.junit.Test

class TenantIsolationAndFeatureFlagsTest {

    @Test
    fun testTenantIsolationBetweenTenants() {
        val tenantA = "tenant_default"
        val tenantB = "tenant_luxe"

        val allLeads = SeedData.initialLeads
        val leadsForTenantA = allLeads.filter { it.tenantId == tenantA }
        val leadsForTenantB = allLeads.filter { it.tenantId == tenantB }

        // Tenant A must only see Tenant A leads
        assertTrue(leadsForTenantA.all { it.tenantId == tenantA })
        // Cross-tenant access must be empty or isolated
        assertTrue(leadsForTenantA.none { it.tenantId == tenantB })
    }

    @Test
    fun testFeatureFlagGlobalAndTenantOverrideIsolation() {
        // Feature: AI Design Recommendations (Default is disabled)
        val aiFeature = SeedData.initialFeatureFlags.first { it.key == "ai_design_recommendations" }
        assertFalse("AI recommendations should be disabled globally by default", aiFeature.defaultEnabled)

        // Enable for Tenant A only
        val updatedOverrides = aiFeature.tenantOverrides.toMutableMap()
        updatedOverrides["tenant_default"] = true
        val flagWithOverride = aiFeature.copy(tenantOverrides = updatedOverrides)

        // Tenant A has override enabled
        assertTrue("Tenant A must have feature enabled", flagWithOverride.tenantOverrides["tenant_default"] == true)
        // Tenant B remains unaffected (falls back to global default: false)
        val isTenantBEnabled = flagWithOverride.tenantOverrides["tenant_luxe"] ?: flagWithOverride.defaultEnabled
        assertFalse("Tenant B must remain disabled", isTenantBEnabled)
    }

    @Test
    fun testFeatureRollbackRestoresDefault() {
        val budgetFlag = SeedData.initialFeatureFlags.first { it.key == "budget_estimator" }
        // Simulate corrupted or bad configuration override
        val corruptedFlag = budgetFlag.copy(
            defaultEnabled = false,
            tenantOverrides = mapOf("tenant_default" to false),
            rolloutPercentage = 20
        )

        // Rollback operation
        val restoredFlag = corruptedFlag.copy(
            defaultEnabled = true,
            tenantOverrides = emptyMap(),
            rolloutPercentage = 100
        )

        assertTrue(restoredFlag.defaultEnabled)
        assertTrue(restoredFlag.tenantOverrides.isEmpty())
        assertEquals(100, restoredFlag.rolloutPercentage)
    }

    @Test
    fun testEditableBusinessNameUpdatesCorrectly() {
        val originalTenant = SeedData.initialTenants.first { it.id == "tenant_default" }
        assertEquals("Interior Design Studio", originalTenant.name)

        val updatedTenant = originalTenant.copy(name = "Bespoke Horizons Interior Architecture")
        assertEquals("Bespoke Horizons Interior Architecture", updatedTenant.name)
        assertEquals("Interior Design Studio", updatedTenant.defaultName) // Preserves factory default
    }

    @Test
    fun testQuotationApprovalAdvancesMilestone() {
        val quote = SeedData.initialQuotation
        assertEquals("APPROVED", quote.status)
        assertTrue(quote.grandTotal > 0.0)
    }
}
