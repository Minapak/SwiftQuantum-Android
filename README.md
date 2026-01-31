# SwiftQuantum Android

Professional Quantum Computing IDE for Android

**Version 5.8.3** | SwiftQuantumBackend v5.7.1 Compatible

## Overview

SwiftQuantum Android is the Android port of the iOS SwiftQuantum app, providing a comprehensive quantum computing development environment with circuit design, simulation, and quantum hardware integration. Part of the SwiftQuantum Ecosystem.

## Features

- **Quantum Simulation**: Support for 20-40+ qubits depending on subscription tier
- **22 Quantum Gates**: H, X, Y, Z, S, T, RX, RY, RZ, U1, U2, U3, CNOT, CZ, CY, SWAP, iSWAP, CRX, CRY, CRZ, Toffoli, Fredkin, CCZ
- **3D Bloch Sphere**: Real-time quantum state visualization
- **Circuit Builder**: Visual drag-and-drop circuit design
- **Quantum Hardware Integration**: Direct connection to quantum hardware providers (Master tier)
- **Multi-Engine Support**: Python, Rust (900x faster), C++ (1200x faster)
- **Australian Quantum Standards**: Q-CTRL Error Suppression, MicroQiskit Optimization, LabScript Protocol, SQC Fidelity Grading
- **Material Design 3**: Modern Android UI with Jetpack Compose
- **Unified Navigation Drawer**: Deep link integration with SwiftQuantum Ecosystem apps
- **Cross-App Authentication**: Single sign-on across all SwiftQuantum apps
- **5-Language Support**: English, Korean, Japanese, Chinese, German
- **Settings Screen**: Full configuration including language, theme, simulation engine
- **Paywall Screen**: Cyberpunk-themed subscription management
- **Advanced Visualization**: Bloch sphere, histogram, state vector tabs

### v5.8.3 New Features (iOS Parity)

- **Admin Dashboard Module**: Full admin panel with 5 screens (Dashboard, Users, Content, Team, Settings)
- **Enterprise Team Management**: Team members, roles, invitations, audit logging
- **Complete Admin Localization**: 90+ admin strings in 5 languages (EN, KO, JA, ZH, DE)

### v5.8.2 New Features

- **State Compass Visualization**: Radial probability distribution chart (3rd visualization type)
- **Circuit Presets Menu**: Bell State, GHZ State, QFT, Random circuit templates

### v5.6.0 New Features

- **Operations Readiness Checklist**: Pre-deployment validation and system health monitoring
- **3-Layer Cache Architecture**: Memory (L1), Redis (L2), Database (L3) caching for optimal performance
- **Redis Advanced Integration**: Enhanced caching with TTL management and cache invalidation
- **Sentry Error Monitoring**: Real-time error tracking, performance monitoring, and crash reporting

## Requirements

- Android 8.0 (API 26) or higher
- Android Studio Ladybug or newer
- Kotlin 2.0+
- JDK 17+

## Tech Stack

- **Language**: Kotlin 2.0
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt
- **Network**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Storage**: DataStore Preferences
- **Billing**: Google Play Billing Library

## Project Structure

```
app/src/main/java/com/swiftquantum/
├── data/
│   ├── api/           # Retrofit API interfaces
│   ├── dto/           # Data Transfer Objects
│   ├── local/         # DataStore, TokenManager
│   └── repository/    # Repository implementations
├── domain/
│   ├── model/         # Domain models
│   ├── repository/    # Repository interfaces
│   └── usecase/       # Use cases
├── presentation/
│   ├── ui/
│   │   ├── component/ # Reusable UI components
│   │   ├── screen/    # Compose screens
│   │   └── theme/     # Material theme
│   ├── navigation/    # Navigation setup
│   └── viewmodel/     # ViewModels
└── di/                # Hilt DI modules
```

## Subscription Tiers

| Tier | Qubits | Features |
|------|--------|----------|
| FREE | 20 | Python engine, basic features |
| PRO | 30 | + Rust engine, advanced features |
| MASTER | 40+ | + C++ engine, Quantum Hardware access |

## Build & Run

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device/emulator

## API Configuration

- Backend: `https://api.swiftquantum.tech/api/v1`
- Bridge: `https://bridge.swiftquantum.tech`
- Sentry DSN: Configured via BuildConfig

## Backend Compatibility

- **SwiftQuantumBackend**: v5.7.1
- **API Version**: v1
- **3-Layer Cache**: Memory / Redis / Database
- **Monitoring**: Sentry integration enabled

## License

Copyright © 2026 SwiftQuantum. All rights reserved.

## Contact

- Email: admin@swiftquantumnative.com
- Website: https://swiftquantum.tech
