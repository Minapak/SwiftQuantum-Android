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
- Light and dark themes
- Adaptive layouts

### Localization
- English (default)
- Korean (한국어)

### Navigation
- Bottom navigation bar
- 4 main tabs: Simulator, Circuit, Hardware, Profile
- Smooth animations and transitions

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
