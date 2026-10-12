# Three-Application Interior Design Business Ecosystem Architecture

## 1. Executive Summary
This enterprise Android ecosystem provides a complete architectural & interior design business platform consisting of three dedicated applications backed by a secure, multi-tenant Cloud Firestore database:

1. **Customer Android App** (`com.yourcompany.interiordesign.customer`): Client acquisition, interactive room portfolios, before/after site comparisons, budget estimator, consultation bookings, and live milestone tracking.
2. **Business Owner Admin App** (`com.yourcompany.interiordesign.owner`): Lead CRM pipeline, portfolio publisher, tier rate engine, site visit dispatch, progress approvals, invoices, and studio branding customization.
3. **Developer Super Admin App** (`com.yourcompany.interiordesign.superadmin`): Multi-tenant business provisioning, dynamic 20+ feature flag management, per-tenant overrides, staged rollouts, and audit trails.

---

## 2. System Architecture Diagram

```
+-----------------------------------------------------------------------------------+
|                        DEVELOPER SUPER ADMIN ANDROID APP                          |
|         Tenant Management  |  Feature Management Center  |  Global Audit          |
+-----------------------------------------------------------------------------------+
                                          |
                      (Server-Side Verified Super Admin Role)
                                          v
+-----------------------------------------------------------------------------------+
|                            CLOUD FIRESTORE SHARED BACKEND                         |
|  - tenants              - feature_registry         - feature_rollout_history      |
|  - tenant_memberships   - leads                    - portfolio_projects           |
|  - bookings             - client_projects          - project_milestones           |
|  - quotations           - invoices                 - audit_logs                   |
|  - pricing_rules        - testimonials             - users                        |
+-----------------------------------------------------------------------------------+
                                  ^                    ^
                                  |                    |
            (Tenant Owner Access) |                    | (Customer Scoped Access)
                                  |                    |
+-------------------------------------+    +----------------------------------------+
|      BUSINESS OWNER ADMIN APP       |    |          CUSTOMER ANDROID APP          |
|  - Lead CRM Pipeline                |    |  - Room-Wise Design Portfolio          |
|  - Portfolio & Before/After CMS     |    |  - Before-and-After Touch Slider       |
|  - Tier Pricing & Rate Engine       |    |  - Budget Estimator (Std/Prem/Luxe)    |
|  - Staff Site Visit Dispatch        |    |  - Consultation & Site Visit Booking   |
|  - Supervisor Progress Photo Auth   |    |  - Live Project Milestone Tracker      |
|  - Invoices & Milestone Payments    |    |  - Turnkey Quotation 1-Tap Approval    |
|  - Editable Studio Name & Branding  |    |  - WhatsApp Concierge Integration      |
+-------------------------------------+    +----------------------------------------+
```

---

## 3. Dynamic Feature Activation & Rollback Engine
Every feature in the ecosystem evaluates through a 3-tier authorization model:
1. **Platform Check**: Is the feature globally enabled in `feature_registry`?
2. **Tenant Entitlement Check**: Has the developer assigned an override (True/False) for this tenant?
3. **Role & Client Capability Check**: Does the user's role and installed APK version support the module?

When the Developer Super Admin rolls back a feature, the engine restores the Last-Known-Good configuration immediately without requiring an app reinstall.

---

## 4. Tenant Isolation Invariants
- All operational documents carry a non-null `tenantId`.
- Security rules strictly restrict write access to users holding matching `token.tenantId` or verified membership records.
- Cross-tenant data leaks are physically blocked at the database query level.
