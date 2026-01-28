# SwiftQuantum Android Features

## Core Features

### Quantum Simulation
- **Multi-qubit support**: Up to 40+ qubits (tier dependent)
- **Local simulation**: Fast in-app quantum state evolution
- **Remote simulation**: Server-side Rust/C++ engines for large circuits

### Quantum Gates (22 Total)

**Single-Qubit Gates:**
- Hadamard (H)
- Pauli-X, Pauli-Y, Pauli-Z
- Phase (S, T)
- Rotation (RX, RY, RZ)
- Universal (U1, U2, U3)

**Multi-Qubit Gates:**
- CNOT (CX), CZ, CY
- SWAP, iSWAP
- Controlled Rotations (CRX, CRY, CRZ)
- Toffoli (CCX), Fredkin (CSWAP), CCZ

### Circuit Builder
- Drag-and-drop gate placement
- Multi-qubit circuit visualization
- Gate parameter editing
- Circuit save/load functionality
- QASM import/export support

### Visualization
- **3D Bloch Sphere**: Real-time quantum state representation
- **Histogram Charts**: Measurement probability distribution
- **Circuit Diagrams**: Wire-based gate visualization

### IBM Quantum Integration
- IBM Quantum API token authentication
- Backend selection (Brisbane, Osaka, Kyoto - 127 qubits)
- Job submission and monitoring
- Queue position tracking
- Result retrieval

## User Features

### Authentication
- Email/password login
- Google Sign-In (planned)
- JWT token management
- Auto token refresh
- Cross-app single sign-on (SharedAuthManager)
- Guest mode with login access in Settings
- Logout functionality

### Subscription Tiers

| Feature | FREE | PRO | MASTER |
|---------|------|-----|--------|
| Max Qubits | 20 | 30 | 40+ |
| Python Engine | ✓ | ✓ | ✓ |
| Rust Engine | - | ✓ | ✓ |
| C++ Engine | - | - | ✓ |
| IBM Hardware | - | - | ✓ |
| Price | Free | ₩12,000/mo | ₩36,000/mo |

### Profile
- User statistics (simulations run, qubits used)
- Subscription management
- Settings and preferences

## UI/UX Features

### Material Design 3
- Dynamic color support (Android 12+)
- Light and dark themes (dark mode default)
- Adaptive layouts for Phone/Foldable/Tablet
- Brand Color: Swift-Purple (#7B2FFF)

### Localization (5 Languages)
- English (default)
- Korean (한국어)
- Japanese (日本語)
- Chinese (中文)
- German (Deutsch)

### Navigation
- Bottom navigation bar
- 4 main tabs: Simulator, Circuit, Hardware, Profile
- Unified Navigation Drawer for SwiftQuantum Ecosystem
- Deep links to QuantumNative, Q-Bridge, QuantumCareer
- Smooth animations and transitions

### Settings Screen
- Language selection with in-app switching
- Theme toggle (Light/Dark)
- Dynamic colors toggle (Android 12+)
- Default simulation engine selection
- Default shots configuration
- Subscription management

### Paywall Screen
- Cyberpunk-themed subscription UI
- Three-tier comparison (Free/Pro/Master)
- Feature breakdown by tier
- Google Play Billing integration
- Upgrade/downgrade flow

### Visualization Screen
- Bloch Sphere tab with 3D rotation
- Histogram tab for measurement results
- State Vector tab for amplitude display
- Real-time updates during simulation

### Splash Screen
- Animated logo with brand colors
- Loading progress indicator
- Smooth transition to main app

### Lottie Animations
- Cloud processing feedback
- Loading states
- Success/error animations

## Technical Features

### Performance
- Efficient state management with StateFlow
- Lazy loading for large data sets
- Background processing with Coroutines

### Security
- Secure token storage with DataStore
- HTTPS-only communication
- ProGuard/R8 obfuscation

### Offline Support
- Basic quantum simulation works offline
- Cached user data
- Queue operations for sync
