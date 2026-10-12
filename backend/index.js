/**
 * Cloud Functions for Three-Application Interior Design Ecosystem
 * Enforces Tenant Isolation, Server-Side Feature Flag Validation & Audit Logging
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();
const db = admin.firestore();

/**
 * Super Admin Security Check
 */
function assertSuperAdmin(context) {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated.');
  }
  const token = context.auth.token;
  if (token.role !== 'super_admin' && !token.email.endsWith('@interiordesignstudio.com')) {
    throw new functions.https.HttpsError('permission-denied', 'Super Admin privilege required.');
  }
}

/**
 * 1. Dynamic Feature Flag Validation and Safe Rollout
 */
exports.updateFeatureFlag = functions.https.onCall(async (data, context) => {
  assertSuperAdmin(context);

  const { featureKey, defaultEnabled, tenantOverrides, rolloutPercentage } = data;
  if (!featureKey) {
    throw new functions.https.HttpsError('invalid-argument', 'featureKey is required.');
  }

  const featureRef = db.collection('feature_registry').doc(featureKey);
  const snap = await featureRef.get();
  const previousState = snap.exists ? snap.data() : null;

  const newState = {
    key: featureKey,
    defaultEnabled: !!defaultEnabled,
    tenantOverrides: tenantOverrides || {},
    rolloutPercentage: rolloutPercentage != null ? rolloutPercentage : 100,
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    updatedBy: context.auth.uid
  };

  await featureRef.set(newState, { merge: true });

  // Record Immutable Audit Log
  await db.collection('audit_logs').add({
    tenantId: 'global',
    actorId: context.auth.uid,
    actorRole: 'SUPER_ADMIN',
    action: 'FEATURE_FLAG_MUTATED',
    entityType: 'FeatureFlag',
    entityId: featureKey,
    details: `Rollout: ${newState.rolloutPercentage}%, Global: ${newState.defaultEnabled}`,
    previousState,
    newState,
    timestamp: admin.firestore.FieldValue.serverTimestamp()
  });

  return { success: true, featureKey };
});

/**
 * 2. Tenant Provisioning Trigger
 * Bootstraps default pricing formulas and tenant configuration
 */
exports.onTenantCreated = functions.firestore
  .document('tenants/{tenantId}')
  .onCreate(async (snap, context) => {
    const tenantId = context.params.tenantId;
    const tenantData = snap.data();

    // Default Pricing Rules
    await db.collection('pricing_rules').doc(`${tenantId}_default`).set({
      tenantId,
      tier: 'PREMIUM',
      ratePerSqFt: 46.0,
      falseCeilingRateSqFt: 4.2,
      modularKitchenBase: 11500.0,
      wardrobePerUnit: 8800.0,
      taxRatePercent: 9.0,
      contingencyPercent: 5.0,
      updatedAt: admin.firestore.FieldValue.serverTimestamp()
    });

    // Audit log
    await db.collection('audit_logs').add({
      tenantId,
      actorId: 'system',
      actorRole: 'PLATFORM_DAEMON',
      action: 'TENANT_INITIALIZED',
      entityType: 'Tenant',
      entityId: tenantId,
      details: `Initialized tenant '${tenantData.name}' with standard rules`,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });
  });

/**
 * 3. Quotation Approval Trigger
 * When customer approves a quote, advance project status and generate Milestone 1 invoice
 */
exports.onQuotationUpdated = functions.firestore
  .document('quotations/{quoteId}')
  .onUpdate(async (change, context) => {
    const before = change.before.data();
    const after = change.after.data();

    if (before.status !== 'APPROVED' && after.status === 'APPROVED') {
      const { tenantId, projectId, grandTotal, customerId, quoteNumber } = after;

      // Create Initial Milestone 1 Invoice (35% mobilization advance)
      const invoiceAmount = grandTotal * 0.35;
      await db.collection('invoices').add({
        tenantId,
        projectId,
        customerId,
        invoiceNumber: `INV-${Date.now().toString().slice(-4)}`,
        description: `Mobilization Advance (35%) for approved quote #${quoteNumber}`,
        amount: invoiceAmount,
        status: 'UNPAID',
        createdAt: admin.firestore.FieldValue.serverTimestamp()
      });

      // Update Client Project status
      if (projectId) {
        await db.collection('client_projects').doc(projectId).set({
          status: 'IN_PROGRESS',
          progressPercent: 15,
          currentMilestone: 'Civil & Demolition Rough-In'
        }, { merge: true });
      }
    }
  });
