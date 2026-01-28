# SwiftQuantum Android

Professional Quantum Computing IDE for Android

## Overview

SwiftQuantum Android is the Android port of the iOS SwiftQuantum app, providing a comprehensive quantum computing development environment with circuit design, simulation, and IBM Quantum hardware integration. Part of the SwiftQuantum Ecosystem.

## Features

- **Quantum Simulation**: Support for 20-40+ qubits depending on subscription tier
- **22 Quantum Gates**: H, X, Y, Z, S, T, RX, RY, RZ, U1, U2, U3, CNOT, CZ, CY, SWAP, iSWAP, CRX, CRY, CRZ, Toffoli, Fredkin, CCZ
- **3D Bloch Sphere**: Real-time quantum state visualization
- **Circuit Builder**: Visual drag-and-drop circuit design
- **IBM Quantum Integration**: Direct connection to IBM Quantum hardware (Master tier)
- **Multi-Engine Support**: Python, Rust (900x faster), C++ (1200x faster)
- **Material Design 3**: Modern Android UI with Jetpack Compose
- **Unified Navigation Drawer**: Deep link integration with SwiftQuantum Ecosystem apps
- **Cross-App Authentication**: Single sign-on across all SwiftQuantum apps
- **5-Language Support**: English, Korean, Japanese, Chinese, German
- **Settings Screen**: Full configuration including language, theme, simulation engine
- **Paywall Screen**: Cyberpunk-themed subscription management
- **Advanced Visualization**: Bloch sphere, histogram, state vector tabs

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
| MASTER | 40+ | + C++ engine, IBM Quantum hardware |

## Build & Run

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device/emulator

## API Configuration

- Backend: `https://api.swiftquantum.tech/api/v1`
- Bridge: `https://bridge.swiftquantum.tech`

## License

Copyright © 2026 SwiftQuantum. All rights reserved.

## Contact

- Email: admin@swiftquantumnative.com
- Website: https://swiftquantum.tech
