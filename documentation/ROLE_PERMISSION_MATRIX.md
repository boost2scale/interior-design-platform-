# Role & Permission Matrix

| Capability / Resource | Customer App | Business Owner Admin | Developer Super Admin | Staff Designer |
| :--- | :---: | :---: | :---: | :---: |
| **Browse Room Portfolio** | READ | READ | READ | READ |
| **Use Before/After Slider** | READ | READ | READ | READ |
| **Run Budget Estimator** | READ | READ | READ | READ |
| **Book Site Consultations** | CREATE, READ (Own) | READ, UPDATE (Tenant) | READ (All) | READ (Assigned) |
| **CRM Lead Pipeline** | NO ACCESS | FULL (Tenant) | FULL (Audit) | READ (Assigned) |
| **Publish Portfolio Items** | NO ACCESS | FULL (Tenant) | FULL (Global) | DRAFT |
| **Edit Pricing Tier Rates** | NO ACCESS | FULL (Tenant) | FULL (Global) | NO ACCESS |
| **View Client Project Timeline** | READ (Own) | FULL (Tenant) | FULL (Global) | UPDATE (Site Photos) |
| **Upload Supervisor Site Photos**| NO ACCESS | FULL (Tenant) | FULL (Global) | CREATE (Supervisor) |
| **Approve Quotation** | APPROVE / REVISE | CREATE, SEND (Tenant)| READ (Audit) | NO ACCESS |
| **Record Invoice Payment** | NO ACCESS | UPDATE (Tenant) | FULL (Global) | NO ACCESS |
| **Change Studio Name / Brand** | NO ACCESS | FULL (Tenant) | FULL (Global) | NO ACCESS |
| **Suspend / Activate Tenant** | NO ACCESS | NO ACCESS | FULL (Global) | NO ACCESS |
| **Mutate Feature Flags** | NO ACCESS | NO ACCESS | FULL (Global) | NO ACCESS |
| **Rollback Feature Rollouts** | NO ACCESS | NO ACCESS | FULL (Global) | NO ACCESS |
| **Immutable Audit Logs** | NO ACCESS | READ (Tenant) | READ, EXPORT (All) | NO ACCESS |
