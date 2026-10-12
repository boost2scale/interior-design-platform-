package com.example.data

import com.example.model.*

object SeedData {

    val initialTenants = listOf(
        Tenant(
            id = "tenant_default",
            name = "Interior Design Studio",
            defaultName = "Interior Design Studio",
            tagline = "Bespoke Architecture, Luxury Finishes & Turnkey Execution",
            logoUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=400&q=80",
            brandColorHex = "#C59B27", // Warm Gold
            contactEmail = "concierge@interiordesignstudio.com",
            contactPhone = "+1 (800) 456-7890",
            whatsappNumber = "+18004567890",
            address = "742 Evergreen Terrace, Suite 400, Design District",
            serviceLocations = listOf("Metro Central", "West Beverly", "Highland Park", "Marina Bay", "Oakland Heights"),
            businessHours = "Mon - Sat: 9:00 AM - 7:00 PM",
            plan = "ENTERPRISE",
            active = true
        ),
        Tenant(
            id = "tenant_luxe",
            name = "Luxe Living Architects",
            defaultName = "Luxe Living Architects",
            tagline = "Ultra-Modern Minimalist & Sustainable Habitats",
            logoUrl = "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?w=400&q=80",
            brandColorHex = "#1E5F74", // Deep Cyan / Teal
            contactEmail = "studios@luxeliving.design",
            contactPhone = "+1 (888) 920-5544",
            whatsappNumber = "+18889205544",
            address = "1200 Avenue of the Arts, 18th Floor",
            serviceLocations = listOf("North Hills", "Lakefront Peninsula", "Skyline Ridge"),
            businessHours = "Mon - Fri: 10:00 AM - 6:00 PM",
            plan = "PRO",
            active = true
        ),
        Tenant(
            id = "tenant_heritage",
            name = "Heritage Craft Interiors",
            defaultName = "Heritage Craft Interiors",
            tagline = "Artisanal Woodwork, Classical Moulding & Timeless Elegance",
            logoUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=400&q=80",
            brandColorHex = "#8B4513", // Saddle Brown
            contactEmail = "artisan@heritagecraft.com",
            contactPhone = "+1 (800) 714-3320",
            whatsappNumber = "+18007143320",
            address = "45 Artisan Way, Historic Quarter",
            serviceLocations = listOf("Old Town", "Greenwood", "Royal Park"),
            businessHours = "Mon - Sat: 9:30 AM - 6:30 PM",
            plan = "STARTER",
            active = false // Suspended tenant to demonstrate tenant status in super admin!
        )
    )

    val initialFeatureFlags = listOf(
        FeatureFlag(
            key = "portfolio_showcase",
            name = "Interior Portfolio Showcase",
            description = "High-definition gallery, room categories and walkthrough videos.",
            category = "Customer Experience",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "before_after_slider",
            name = "Before-and-After Comparison Slider",
            description = "Interactive touch slider comparing raw civil site vs finished interior.",
            category = "Visual Polish",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "budget_estimator",
            name = "Interior Budget Estimator",
            description = "Real-time cost calculator with room selectors, carpet area and tier sliders.",
            category = "Sales & Conversion",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "tier_pricing_rules",
            name = "Standard, Premium & Luxury Tiers",
            description = "Configurable multi-tier rate calculation and add-on material formulas.",
            category = "Pricing Engine",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "consultation_booking",
            name = "Consultation & Site Visit Booking",
            description = "Calendar scheduling for on-site property visits and virtual design calls.",
            category = "Bookings",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "site_visit_scheduling",
            name = "Staff Site Visit Dispatch",
            description = "Assign lead interior architects to confirmed site consultations.",
            category = "Operations",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "whatsapp_integration",
            name = "WhatsApp Click-to-Chat",
            description = "Direct 1-tap WhatsApp consultation link with pre-filled enquiry details.",
            category = "Communication",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "in_app_messaging",
            name = "In-App Client Messaging",
            description = "Secure chat between homeowner, designer and site supervisor.",
            category = "Communication",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "client_project_tracking",
            name = "Live Project Progress Tracking",
            description = "Milestone timeline and live percent completion for authenticated clients.",
            category = "Client Portal",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "progress_photo_uploads",
            name = "Authorized Site Progress Photos",
            description = "Supervisor site photo uploads with client verification flags.",
            category = "Operations",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "quotations_module",
            name = "Quotation Creation & 1-Tap Approval",
            description = "Formal turnkey quotation generation and digital client sign-off.",
            category = "Sales & Finance",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "invoices_payments",
            name = "Invoices & Payment Records",
            description = "Track staged milestone billing, invoices and bank transfer references.",
            category = "Sales & Finance",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "customer_reviews",
            name = "Customer Reviews & Testimonials",
            description = "Display verified project reviews and rating stars on customer app.",
            category = "Social Proof",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "video_testimonials",
            name = "Video Walkthrough Testimonials",
            description = "Stream video reviews of completed turnkey handover homes.",
            category = "Social Proof",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "push_notifications",
            name = "Milestone & Booking Push Notifications",
            description = "Automated alerts for site updates, approvals and scheduled consultations.",
            category = "Engagement",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "advanced_analytics",
            name = "Business Owner Analytics & Reports",
            description = "Lead conversion funnels, category revenue charts and CSV export.",
            category = "Admin & BI",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "staff_dispatch",
            name = "Staff Roles & Follow-Up Reminders",
            description = "Team member assignment and automated CRM follow-up alerts.",
            category = "Admin & BI",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "ai_design_recommendations",
            name = "AI Material & Color Harmony",
            description = "Smart palette suggestions based on room orientation and light levels.",
            category = "Innovation",
            defaultEnabled = false // Disabled by default to demonstrate rollout!
        ),
        FeatureFlag(
            key = "floor_plan_upload",
            name = "CAD / PDF Floor Plan Upload",
            description = "Allow clients to attach 2D architectural blueprint files during booking.",
            category = "Client Portal",
            defaultEnabled = true
        ),
        FeatureFlag(
            key = "multi_language_support",
            name = "Multi-Language Studio Localization",
            description = "Support localized currency, units (sq ft vs sq m) and languages.",
            category = "Localization",
            defaultEnabled = true
        )
    )

    val initialPortfolio = listOf(
        PortfolioProject(
            id = "proj_01",
            tenantId = "tenant_default",
            title = "Aura Penthouse Sunken Lounge",
            roomCategory = RoomCategory.LIVING_ROOM.name,
            style = "Warm Minimalist & Scandinavian",
            carpetAreaSqFt = 580,
            budgetTier = "LUXURY",
            estimatedCost = 28500.0,
            coverImage = "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=800&q=80", // Raw brick & concrete
            afterImage = "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80",  // Luxury finished
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80",
                "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800&q=80",
                "https://images.unsplash.com/photo-1600566753376-12c8ab7fb75b?w=800&q=80"
            ),
            videoWalkthroughUrl = "https://storage.googleapis.com/sample-interior-walkthrough.mp4",
            description = "Features custom fluted acoustic oak wall panels, recessed perimeter warm LED cove lighting (2700K), Italian Statuario marble TV credenza, and custom bouclé curved seating.",
            clientName = "Mr. & Mrs. Sterling",
            completionDurationWeeks = 7,
            featured = true
        ),
        PortfolioProject(
            id = "proj_02",
            tenantId = "tenant_default",
            title = "Serene Oak Master Sanctuary",
            roomCategory = RoomCategory.BEDROOM.name,
            style = "Japandi Organic",
            carpetAreaSqFt = 420,
            budgetTier = "PREMIUM",
            estimatedCost = 16800.0,
            coverImage = "https://images.unsplash.com/photo-1616594039964-ae9021a400a0?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1541888946425-d0fbb1861593?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1616594039964-ae9021a400a0?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1616594039964-ae9021a400a0?w=800&q=80",
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=800&q=80"
            ),
            videoWalkthroughUrl = "https://storage.googleapis.com/sample-interior-bedroom.mp4",
            description = "Platform bed with floating brass nightstands, natural linen upholstered headboard, and hidden motorized blackout roller shades integrated into architectural ceiling pocket.",
            clientName = "Dr. Aris Thorne",
            completionDurationWeeks = 5,
            featured = true
        ),
        PortfolioProject(
            id = "proj_03",
            tenantId = "tenant_default",
            title = "Chef's Quartz Island Modular Kitchen",
            roomCategory = RoomCategory.MODULAR_KITCHEN.name,
            style = "Modern European Architectural",
            carpetAreaSqFt = 260,
            budgetTier = "LUXURY",
            estimatedCost = 24000.0,
            coverImage = "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800&q=80",
                "https://images.unsplash.com/photo-1600585154526-990dced4db0d?w=800&q=80"
            ),
            videoWalkthroughUrl = "https://storage.googleapis.com/sample-interior-kitchen.mp4",
            description = "Anti-fingerprint matte slate acrylic shutters, Blum soft-close tandem drawers with 60kg load rating, waterfall Calacatta quartz prep island, and built-in induction downdraft hob.",
            clientName = "Chef Camille Laurent",
            completionDurationWeeks = 4,
            featured = true
        ),
        PortfolioProject(
            id = "proj_04",
            tenantId = "tenant_default",
            title = "Smoked Glass Walk-In Wardrobe Suite",
            roomCategory = RoomCategory.WARDROBES.name,
            style = "Luxury Milanese Boutiques",
            carpetAreaSqFt = 190,
            budgetTier = "LUXURY",
            estimatedCost = 14500.0,
            coverImage = "https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1504307651254-35680f356dfd?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=800&q=80"
            ),
            videoWalkthroughUrl = "",
            description = "Floor-to-ceiling tinted grey tempered glass doors with concealed pivot hinges, velvet-lined jewelry organizer drawers, sensor-activated vertical LED lighting tracks.",
            clientName = "Victoria H.",
            completionDurationWeeks = 3,
            featured = false
        ),
        PortfolioProject(
            id = "proj_05",
            tenantId = "tenant_default",
            title = "Acoustic Gypsum Multizone False Ceiling",
            roomCategory = RoomCategory.FALSE_CEILINGS.name,
            style = "Contemporary Architectural",
            carpetAreaSqFt = 750,
            budgetTier = "STANDARD",
            estimatedCost = 6200.0,
            coverImage = "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=800&q=80"
            ),
            description = "Seamless Saint-Gobain moisture-resistant plasterboard with perimeter magnetic shadow lines, smart DALI dimmable spotlights and magnetic low-voltage track lighting.",
            clientName = "Mark & Anita",
            completionDurationWeeks = 2,
            featured = false
        ),
        PortfolioProject(
            id = "proj_06",
            tenantId = "tenant_default",
            title = "The Beverly Hills 4-BHK Turnkey Estate",
            roomCategory = RoomCategory.COMPLETE_HOME.name,
            style = "Transitional Californian Luxury",
            carpetAreaSqFt = 3400,
            budgetTier = "LUXURY",
            estimatedCost = 142000.0,
            coverImage = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80",
                "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800&q=80",
                "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800&q=80"
            ),
            description = "End-to-end architectural remodeling including structural beam reinforcement, custom teak carpentry, double-height foyer chandelier, and whole-home Lutron lighting automation.",
            clientName = "Harrison Family",
            completionDurationWeeks = 14,
            featured = true
        ),
        PortfolioProject(
            id = "proj_07",
            tenantId = "tenant_default",
            title = "Apex FinTech Executive Headquarters",
            roomCategory = RoomCategory.COMMERCIAL.name,
            style = "Biophilic Industrial Modern",
            carpetAreaSqFt = 4500,
            budgetTier = "LUXURY",
            estimatedCost = 185000.0,
            coverImage = "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80",
            beforeImage = "https://images.unsplash.com/photo-1504307651254-35680f356dfd?w=800&q=80",
            afterImage = "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80",
                "https://images.unsplash.com/photo-1497215728101-856f4ea42174?w=800&q=80"
            ),
            description = "Open collaborative workstations, acoustic moss feature walls, frameless glass executive boardrooms with privacy switchable smart glass, and artisan barista breakout lounge.",
            clientName = "Apex Capital Partners",
            completionDurationWeeks = 10,
            featured = true
        )
    )

    val initialLeads = listOf(
        Lead(
            id = "lead_01",
            tenantId = "tenant_default",
            customerName = "Rachel Green",
            phone = "+1 (555) 234-8901",
            email = "rachel.g@gmail.com",
            projectType = "Complete Home (3-BHK)",
            estimatedBudget = 55000.0,
            carpetAreaSqFt = 1850,
            status = "NEW",
            notes = "Wants Scandinavian vibe, key handover in 3 months. Needs modular kitchen and master suite first."
        ),
        Lead(
            id = "lead_02",
            tenantId = "tenant_default",
            customerName = "Arthur Pendelton",
            phone = "+1 (555) 912-4433",
            email = "arthur.p@outlook.com",
            projectType = "Luxury Villa Living Room & Dining",
            estimatedBudget = 32000.0,
            carpetAreaSqFt = 920,
            status = "CONTACTED",
            notes = "Called on Oct 11. Requested Italian marble floor samples and 3D render preview."
        ),
        Lead(
            id = "lead_03",
            tenantId = "tenant_default",
            customerName = "Sophia Martinez",
            phone = "+1 (555) 782-9900",
            email = "smartinez@biotech.org",
            projectType = "Chef's Modular Kitchen & Pantry",
            estimatedBudget = 26000.0,
            carpetAreaSqFt = 340,
            status = "QUALIFIED",
            notes = "High intent buyer. Budget pre-approved. Wants German hardware (Blum/Hettich) and quartz countertop."
        ),
        Lead(
            id = "lead_04",
            tenantId = "tenant_default",
            customerName = "Marcus & Chloe Sterling",
            phone = "+1 (555) 441-2099",
            email = "sterling.m@gmail.com",
            projectType = "Waterfront Penthouse Full Turnkey",
            estimatedBudget = 95000.0,
            carpetAreaSqFt = 2800,
            status = "CONSULTATION_SCHEDULED",
            notes = "Site visit booked for Oct 20 with Senior Architect Sarah Jenkins."
        ),
        Lead(
            id = "lead_05",
            tenantId = "tenant_default",
            customerName = "Benjamin Hayes",
            phone = "+1 (555) 609-1144",
            email = "b.hayes@hayeslegal.com",
            projectType = "Law Firm Executive Suites & Library",
            estimatedBudget = 68000.0,
            carpetAreaSqFt = 2100,
            status = "PROPOSAL_SENT",
            notes = "Sent formal revision QT-2026-0842. Awaiting partner approval meeting."
        ),
        Lead(
            id = "lead_06",
            tenantId = "tenant_default",
            customerName = "David & Elena Vance",
            phone = "+1 (555) 330-8812",
            email = "vance.family@yahoo.com",
            projectType = "4-BHK Luxury Apartment",
            estimatedBudget = 71250.0,
            carpetAreaSqFt = 2400,
            status = "WON",
            notes = "Contract signed, Milestone 1 deposit cleared, site work underway."
        )
    )

    val initialBookings = listOf(
        Booking(
            id = "bk_01",
            tenantId = "tenant_default",
            customerId = "cust_sterling",
            customerName = "Marcus & Chloe Sterling",
            customerPhone = "+1 (555) 441-2099",
            customerEmail = "sterling.m@gmail.com",
            consultationType = "SITE_VISIT",
            scheduledDate = "2026-10-20",
            timeSlot = "10:30 AM - 12:00 PM",
            propertyAddress = "402 Skyline Blvd, Apt 14B, Marina District",
            carpetAreaSqFt = 2800,
            preferredTier = "LUXURY",
            status = "CONFIRMED",
            assignedStaff = "Sarah Jenkins (Lead Interior Architect)",
            notes = "Please bring quartz slab chips, fluted wood profiles and fabric swatch decks."
        ),
        Booking(
            id = "bk_02",
            tenantId = "tenant_default",
            customerId = "cust_rachel",
            customerName = "Rachel Green",
            customerPhone = "+1 (555) 234-8901",
            customerEmail = "rachel.g@gmail.com",
            consultationType = "VIRTUAL_CALL",
            scheduledDate = "2026-10-22",
            timeSlot = "3:00 PM - 4:00 PM",
            propertyAddress = "Virtual Google Meet",
            carpetAreaSqFt = 1850,
            preferredTier = "PREMIUM",
            status = "PENDING",
            assignedStaff = "Alex Rivera (Design Consultant)",
            notes = "Initial moodboard alignment and 3D floorplan review."
        )
    )

    val initialClientProject = ClientProject(
        id = "proj_client_vance",
        tenantId = "tenant_default",
        customerId = "current_user",
        customerName = "David & Elena Vance",
        title = "The Horizon Residence (4-BHK Luxury Interior)",
        propertyAddress = "402 Skyline Blvd, Penthouse 14B",
        totalQuotation = 71250.0,
        paidAmount = 45000.0,
        status = "IN_PROGRESS",
        progressPercent = 68,
        currentMilestone = "Modular Carpentry & False Ceilings",
        startDate = "2026-08-20",
        targetDate = "2026-11-25"
    )

    val initialMilestones = listOf(
        ProjectMilestone(
            id = "ms_01",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            title = "Civil, Masonry & Concealed Electrical",
            description = "Conduit piping, switch boxes, plumbing rerouting and wall leveling completed.",
            stepOrder = 1,
            status = "COMPLETED",
            completionDate = "2026-09-08",
            approvedByCustomer = true,
            progressPhotos = listOf("https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=600&q=80")
        ),
        ProjectMilestone(
            id = "ms_02",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            title = "Acoustic False Ceiling & Air Ducting",
            description = "Saint-Gobain gypsum grid framework, AC diffuser cuts and warm LED channels.",
            stepOrder = 2,
            status = "COMPLETED",
            completionDate = "2026-09-28",
            approvedByCustomer = true,
            progressPhotos = listOf("https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=600&q=80")
        ),
        ProjectMilestone(
            id = "ms_03",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            title = "Custom Modular Carpentry & Carcasses",
            description = "Plywood carcass assembly for kitchen island, master wardrobes and TV console unit.",
            stepOrder = 3,
            status = "IN_PROGRESS",
            completionDate = null,
            approvedByCustomer = false,
            progressPhotos = listOf("https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=600&q=80")
        ),
        ProjectMilestone(
            id = "ms_04",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            title = "Italian Marble Cladding & Countertops",
            description = "Statuario bookmatched slabs, seamless epoxy grouting and mirror diamond polish.",
            stepOrder = 4,
            status = "PENDING",
            completionDate = null,
            approvedByCustomer = false
        ),
        ProjectMilestone(
            id = "ms_05",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            title = "PU Primer, Luxury Paint & Deep Handover Clean",
            description = "Benjamin Moore breathable paint coats, lighting automation testing and key handover.",
            stepOrder = 5,
            status = "PENDING",
            completionDate = null,
            approvedByCustomer = false
        )
    )

    val initialProgressUpdates = listOf(
        ProgressUpdate(
            id = "upd_01",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            milestoneId = "ms_03",
            title = "Kitchen Island Carcass Factory Inspection Passed",
            note = "High-density moisture-resistant (HDMR) ply boards calibrated and edge-banded with 2mm matching PVC. Blum drawer runners mounted and aligned.",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600&q=80",
                "https://images.unsplash.com/photo-1600585154526-990dced4db0d?w=600&q=80"
            ),
            submittedBy = "Marcus Cole (Quality Inspection Head)",
            updateDate = "2026-10-10",
            authorized = true
        ),
        ProgressUpdate(
            id = "upd_02",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            milestoneId = "ms_02",
            title = "Ceiling Cove Lighting Circuits Energized",
            note = "Smart dimming testing completed at 2700K warm white. Zero flickering across all 6 zones.",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=600&q=80"
            ),
            submittedBy = "Evelyn Reed (Electrical Supervisor)",
            updateDate = "2026-10-02",
            authorized = true
        )
    )

    val initialQuotation = Quotation(
        id = "quot_01",
        tenantId = "tenant_default",
        projectId = "proj_client_vance",
        customerId = "current_user",
        quoteNumber = "QT-2026-0842",
        title = "Turnkey Interior Master Proposal (Vance Residence)",
        tier = "PREMIUM",
        itemsSummary = listOf(
            "Civil Demolition & Wall Reconstruction: $6,500",
            "Bespoke Kitchen & Quartz Waterfall: $22,500",
            "Master & Guest Wardrobes with Sensor LEDs: $15,800",
            "Acoustic Gypsum False Ceilings (Multi-zone): $8,200",
            "Italian Marble Flooring & Polish: $9,500",
            "Lutron Caseta Smart Lighting Integration: $3,200"
        ),
        subtotal = 65700.0,
        taxAmount = 3942.0,
        contingencyAmount = 1608.0,
        grandTotal = 71250.0,
        status = "APPROVED",
        validUntil = "2026-11-20"
    )

    val initialInvoices = listOf(
        Invoice(
            id = "inv_01",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            customerName = "David & Elena Vance",
            invoiceNumber = "INV-2026-081",
            description = "Milestone 1 Advance: Civil & Structural Sign-off",
            amount = 25000.0,
            status = "PAID",
            dueDate = "2026-08-25",
            paidDate = "2026-08-24",
            paymentMethod = "JPMorgan Chase Wire Ref #88412"
        ),
        Invoice(
            id = "inv_02",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            customerName = "David & Elena Vance",
            invoiceNumber = "INV-2026-094",
            description = "Milestone 2: Modular Carpentry Materials Dispatch",
            amount = 20000.0,
            status = "PAID",
            dueDate = "2026-09-30",
            paidDate = "2026-09-29",
            paymentMethod = "ACH Transfer Ref #99231"
        ),
        Invoice(
            id = "inv_03",
            tenantId = "tenant_default",
            projectId = "proj_client_vance",
            customerName = "David & Elena Vance",
            invoiceNumber = "INV-2026-112",
            description = "Milestone 3: Marble Cladding & Final PU Coatings",
            amount = 26250.0,
            status = "UNPAID",
            dueDate = "2026-11-10",
            paidDate = null,
            paymentMethod = "Awaiting Release"
        )
    )

    val initialTestimonials = listOf(
        Testimonial(
            id = "t_01",
            tenantId = "tenant_default",
            customerName = "David & Elena Vance",
            projectTitle = "4-BHK Penthouse, Marina District",
            rating = 5,
            comment = "From the initial 3D budget estimator to the final walkthrough, everything was completely transparent. The site progress photos on our mobile app gave us total confidence while traveling!",
            videoUrl = "https://storage.googleapis.com/sample-vance-testimonial.mp4",
            verifiedProject = true
        ),
        Testimonial(
            id = "t_02",
            tenantId = "tenant_default",
            customerName = "Chef Camille Laurent",
            projectTitle = "Chef's Quartz Island Modular Kitchen",
            rating = 5,
            comment = "As a professional culinary chef, kitchen ergonomics are everything to me. The studio customized the drawer depth, spice pullouts and downdraft venting flawlessly.",
            videoUrl = "https://storage.googleapis.com/sample-camille-testimonial.mp4",
            verifiedProject = true
        ),
        Testimonial(
            id = "t_03",
            tenantId = "tenant_default",
            customerName = "Dr. Aris Thorne",
            projectTitle = "Oak Sanctuary Master Suite",
            rating = 5,
            comment = "Clean lines, natural finishes, and incredible acoustic soundproofing. Truly feels like a five-star boutique retreat in the city.",
            videoUrl = "",
            verifiedProject = true
        )
    )

    val initialAuditLogs = listOf(
        AuditLog(
            id = "log_01",
            tenantId = "tenant_default",
            actorId = "dev_superadmin",
            actorRole = "SUPER_ADMIN",
            action = "FEATURE_ROLLOUT_PUBLISHED",
            entityType = "FeatureFlag",
            entityId = "budget_estimator",
            details = "Published v2.4 configuration schema to tenant_default (100% rollout)"
        ),
        AuditLog(
            id = "log_02",
            tenantId = "tenant_default",
            actorId = "owner_admin",
            actorRole = "BUSINESS_OWNER",
            action = "TENANT_BRANDING_UPDATED",
            entityType = "Tenant",
            entityId = "tenant_default",
            details = "Updated business display name, brand gold color and studio concierge phone"
        ),
        AuditLog(
            id = "log_03",
            tenantId = "tenant_default",
            actorId = "lead_architect",
            actorRole = "STAFF_DESIGNER",
            action = "PROGRESS_UPDATE_AUTHORIZED",
            entityType = "ProgressUpdate",
            entityId = "upd_01",
            details = "Approved 2 quality inspection site photos for Milestone 3"
        )
    )
}
