# Changelog

All notable changes to SwiftQuantum Android will be documented in this file.

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
