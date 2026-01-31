# Changelog

All notable changes to SwiftQuantum Android will be documented in this file.

## [5.8.0] - 2026-01-31

### Added
- **iOS Parity Localization**: Added 100+ new localization strings to match iOS SwiftQuantum features
  - Cloud Visualization strings (interactive 3D, state compass, lattice controls)
  - Tab navigation strings (Circuit, Benchmark, Visualize, Cloud, Settings)
  - Entertainment extended strings (art styles, puzzle solved, oracle)
  - Premium/Paywall extended strings (unlock features, tier descriptions)
  - Tier features strings (Free/Scholar/Master feature lists)
  - Settings extended strings (admin mode, feature descriptions)
  - Auth extended strings (welcome back, password reset flow)
  - Measurement/Statistics strings (expected vs measured, entropy)

### Changed
- All 5 language files (EN, KO, JA, ZH, DE) updated with new iOS-matching strings
- Improved localization coverage to match iOS L10n.swift 1:1

### Fixed
- Duplicate class build error in DEX merge (added annotation-experimental resolution strategy)
- All hardcoded strings in screen files now use stringResource()

### Technical
- Updated build.gradle.kts with dependency resolution for annotation-experimental
- Full build verification passed for all configurations

## [5.7.1] - 2026-01-31

### Changed
- Replaced IBM Quantum references with generic "Quantum Hardware" in strings.xml

### Fixed
- Trademark compliance - removed IBM branding from user-visible strings

### Note
- Educational content describing IBM technology preserved (factual references)

## [5.6.0] - 2026-01-31

### Added
- **Operations Readiness Checklist**: Pre-deployment validation system for ensuring application stability
  - System health monitoring dashboard
  - Dependency version compatibility checks
  - Network connectivity validation
  - Database connection verification
  - API endpoint availability checks
- **3-Layer Cache Architecture**: Optimized caching strategy for enhanced performance
  - L1 Memory Cache: In-memory caching for frequently accessed data
  - L2 Redis Cache: Distributed caching with TTL management
  - L3 Database Cache: Persistent storage fallback
  - Automatic cache invalidation and refresh policies
- **Redis Advanced Integration**: Enhanced Redis connectivity and management
  - Connection pooling for improved throughput
  - Automatic reconnection with exponential backoff
  - Cache key namespacing for multi-tenant support
  - Pub/Sub support for real-time cache invalidation
- **Sentry Error Monitoring**: Comprehensive error tracking and performance monitoring
  - Real-time crash reporting with stack traces
  - Performance transaction monitoring
  - User context and breadcrumb tracking
  - Release health monitoring
  - Custom event tagging for quantum operations

### Changed
- Updated to SwiftQuantumBackend v5.6.0 compatibility
- Improved API response caching with 3-layer architecture
- Enhanced error handling with Sentry integration

### Technical
- Added SentryModule for Hilt dependency injection
- Added CacheManager with multi-layer support
- Added OperationsReadinessRepository and ViewModel
- Added Redis configuration in NetworkModule

## [5.3.0] - 2026-01-29

### Added
- **Immediate Language Switching**: Language changes in Settings now apply instantly using AppCompatDelegate.setApplicationLocales()
- **Language Selection Onboarding**: First-time users see language selection screen with 5 language options

### Changed
- Migrated from deprecated resources.updateConfiguration() to modern AppCompatDelegate locale API
- Improved localization system for immediate UI updates without app restart

### Fixed
- All remaining hardcoded text strings replaced with stringResource() calls
- Settings language change now reflects immediately across all screens

## [5.2.0] - 2026-01-29

### Added
- **Australian Quantum Standards Integration**: New AustralianStandardsScreen for comprehensive quantum standards compliance
- **AustralianStandardsApi.kt**: Complete API interface with all Australian Quantum Standards endpoints
  - Q-CTRL Error Suppression protocols
  - MicroQiskit Optimization endpoints
  - LabScript Protocol integration
  - SQC Fidelity Grading system
- **Navigation Update**: Navigation.kt updated with Australian Standards route (`/australian-standards`)
- **5-Language Localization**: Full translation support for Australian Standards feature
  - English (EN)
  - Korean (KO)
  - Japanese (JA)
  - Chinese (ZH)
  - German (DE)

### Technical
- Added AustralianStandardsRepository with full implementation
- Added AustralianStandardsViewModel for state management
- Integrated Australian Standards into Unified Navigation Drawer

## [1.1.1] - 2026-01-29

### Added
- Complete 5-language localization (English, Korean, Japanese, Chinese, German) with 420+ string keys per language
- Comprehensive translations for Circuit Builder, Benchmark, QASM Editor, Hardware, Visualize, Settings, Paywall screens
- Gate category labels, status messages, error messages, and common actions fully localized

### Fixed
- All hardcoded strings in Kotlin source files replaced with stringResource() calls
- Missing translation keys for benchmark, QASM, hardware status, and measurement results sections

## [1.1.0] - 2026-01-28

### Added
- **Unified Navigation Drawer**: Deep link integration with SwiftQuantum Ecosystem apps
- **Cross-App Authentication**: SharedAuthManager for single sign-on across all ecosystem apps
- **Settings Screen**: Full configuration including language selection, theme options, simulation engine preferences
- **Paywall Screen**: Cyberpunk-themed subscription management with tier comparison
- **Visualization Screen**: Enhanced visualization with Bloch sphere, histogram, and state vector tabs
- **Splash Screen**: Animated startup screen with logo
- **Lottie Animations**: Cloud feedback animations for processing states
- **5-Language Localization**: Added Japanese (日本語), Chinese (中文), German (Deutsch)
- **Responsive Layouts**: Support for Phone, Foldable, and Tablet devices
- **Guest Mode**: Browse without login, access login from Settings

### Changed
- Enhanced Material Design 3 with brand color (Swift-Purple #7B2FFF)
- Improved dark mode as default theme
- Updated navigation system with drawer integration

### Technical
- Added SharedAuthManager via ContentProvider
- Added Lottie dependency for animations
- Added WindowSizeClass for responsive layouts

## [1.0.0] - 2026-01-28

### Added
- Initial release of SwiftQuantum Android
- Complete iOS app feature parity
- 22 quantum gates support (H, X, Y, Z, S, T, RX, RY, RZ, U1, U2, U3, CNOT, CZ, CY, SWAP, iSWAP, CRX, CRY, CRZ, Toffoli, Fredkin, CCZ)
- Circuit builder with drag-and-drop interface
- 3D Bloch sphere visualization
- Real-time histogram for measurement results
- IBM Quantum hardware integration
- JWT-based authentication with DataStore
- Google Play Billing integration
- Material Design 3 UI with dark/light themes
- English and Korean localization
- Clean Architecture with MVVM pattern
- Hilt dependency injection
- Retrofit + OkHttp networking

### Technical
- Min SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)
- Kotlin: 2.0+
- Jetpack Compose: 1.7+
- Hilt: 2.52
- Retrofit: 2.11.0
