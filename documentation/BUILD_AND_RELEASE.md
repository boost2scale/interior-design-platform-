# Build, Packaging and Release Guide

## 1. Project Directory Structure
```
/customer-app                # Standalone Customer Application source & Gradle config
/owner-admin-app             # Standalone Business Owner Manager source & Gradle config
/developer-super-admin-app   # Standalone Super Admin source & Gradle config
/shared-models               # Reusable Kotlin data contracts & serializable schemas
/shared-ui                   # Common Compose widgets (Sliders, Estimators, TopBars)
/backend                     # Firebase Cloud Functions, tenant hooks & audit daemons
/security-rules              # Firestore & Cloud Storage hardened zero-trust rules
/documentation               # System architecture, schemas, and security matrices
/release                     # Output directory for verified APK binaries
```

## 2. Compiling the Applications

To build the debug and release APKs for the individual applications via Gradle:

### Unified Emulator Build (Interactive AI Studio Preview)
```bash
gradle :app:assembleDebug
```

### Standalone Customer Android App
```bash
cd customer-app
gradle assembleRelease
# Output: /release/customer-app.apk
```

### Standalone Business Owner Admin Android App
```bash
cd owner-admin-app
gradle assembleRelease
# Output: /release/owner-admin-app.apk
```

### Standalone Developer Super Admin Android App
```bash
cd developer-super-admin-app
gradle assembleRelease
# Output: /release/developer-super-admin-app.apk
```

---

## 3. Environment Variables & Secrets
Secrets are securely supplied via the AI Studio Secrets panel and `.env` properties:
- `FIREBASE_APPCHECK_DEBUG_TOKEN`
- `GEMINI_API_KEY` (if AI recommendation extensions are enabled)
