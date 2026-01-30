# SwiftQuantum Android Architecture

**Version 5.6.0** | SwiftQuantumBackend v5.6.0 Compatible

## Overview

SwiftQuantum Android follows Clean Architecture principles with MVVM pattern for the presentation layer. Version 5.6.0 introduces a 3-Layer Cache Architecture, Operations Readiness system, and integrated error monitoring.

## Architecture Layers

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │  Screens    │  │  ViewModels │  │  Components │  │
│  │  (Compose)  │  │  (State)    │  │  (UI)       │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
├─────────────────────────────────────────────────────┤
│                    Domain Layer                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │   Models    │  │  Use Cases  │  │ Repositories│  │
│  │  (Entities) │  │  (Business) │  │ (Interfaces)│  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
├─────────────────────────────────────────────────────┤
│                     Data Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │    APIs     │  │    DTOs     │  │   Repos     │  │
│  │  (Retrofit) │  │  (Network)  │  │   (Impl)    │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
├─────────────────────────────────────────────────────┤
│                   Cache Layer (v5.6.0)               │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │ L1 Memory   │  │  L2 Redis   │  │ L3 Database │  │
│  │  (In-App)   │  │(Distributed)│  │ (Persistent)│  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
├─────────────────────────────────────────────────────┤
│                Monitoring Layer (v5.6.0)             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │   Sentry    │  │  Ops Ready  │  │  Analytics  │  │
│  │  (Errors)   │  │ (Checklist) │  │ (Metrics)   │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────┘
```

## Module Structure

### Domain Layer
- **Models**: Core business entities (User, Circuit, Gate, ExecutionResult, AustralianStandards)
- **Repository Interfaces**: Abstract data access contracts
- **Use Cases**: Single-responsibility business operations

### Data Layer
- **API Interfaces**: Retrofit service definitions (AuthApi, CircuitApi, AustralianStandardsApi)
- **DTOs**: Network request/response models
- **Repository Implementations**: Concrete data access
- **Local Storage**: DataStore for preferences and tokens

### Presentation Layer
- **ViewModels**: UI state management with StateFlow
- **Screens**: Jetpack Compose UI (including AustralianStandardsScreen)
- **Components**: Reusable UI elements (BlochSphere, Histogram)
- **Navigation**: Compose Navigation with bottom nav and Australian Standards route

## Dependency Injection

Hilt provides dependency injection across all layers:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
```

## Data Flow

```
User Action → ViewModel → Use Case → Repository → API/Local
     ↑                                               │
     └───────────── StateFlow ←─────────────────────┘
```

## Key Design Decisions

1. **Clean Architecture**: Separation of concerns, testability
2. **MVVM**: Unidirectional data flow, lifecycle awareness
3. **Kotlin Coroutines/Flow**: Async operations, reactive streams
4. **Compose**: Declarative UI, state-driven rendering
5. **Hilt**: Compile-time DI, reduced boilerplate

## Cross-App Integration

### SharedAuthManager
Cross-app authentication via ContentProvider with signature-level permissions:

```kotlin
// SharedAuthManager provides single sign-on across ecosystem apps
class SharedAuthManager(context: Context) {
    fun observeAuthState(): Flow<AuthData>
    suspend fun saveAuth(authData: AuthData)
    suspend fun clearAuth()
}
```

### Unified Navigation Drawer
Deep linking between SwiftQuantum Ecosystem apps:

```kotlin
UnifiedNavigationDrawer(
    currentAppName = "SwiftQuantum",
    userDisplayName = authState.user?.name,
    currentAppFeatures = drawerMenuItems,
    onNavigate = { route -> navController.navigate(route) }
)
```

### Responsive Layout
WindowSizeClass for adaptive layouts:

```kotlin
// Phone: Compact layout
// Foldable: Medium layout with side panels
// Tablet: Expanded layout with master-detail
ResponsiveLayout(
    compact = { CompactContent() },
    medium = { MediumContent() },
    expanded = { ExpandedContent() }
)
```

## Australian Quantum Standards Integration (v5.2.0)

### AustralianStandardsApi
API interface for Australian quantum computing standards:

```kotlin
interface AustralianStandardsApi {
    @GET("standards/qctrl/error-suppression")
    suspend fun getQCtrlErrorSuppression(): QCtrlResponse

    @GET("standards/microqiskit/optimization")
    suspend fun getMicroQiskitOptimization(): MicroQiskitResponse

    @GET("standards/labscript/protocols")
    suspend fun getLabScriptProtocols(): LabScriptResponse

    @GET("standards/sqc/fidelity-grading")
    suspend fun getSqcFidelityGrading(): SqcFidelityResponse
}
```

### Navigation Integration
Australian Standards route added to Navigation.kt:

```kotlin
composable("australian-standards") {
    AustralianStandardsScreen(
        viewModel = hiltViewModel(),
        onNavigateBack = { navController.popBackStack() }
    )
}
```

## 3-Layer Cache Architecture (v5.6.0)

### Cache Hierarchy

```
┌─────────────────────────────────────────────────────┐
│                   Cache Request                      │
└──────────────────────┬──────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────┐
│              L1: Memory Cache (In-App)               │
│  • LRU eviction policy                              │
│  • TTL: 5 minutes                                   │
│  • Max size: 100 entries                            │
└──────────────────────┬──────────────────────────────┘
                       │ Cache Miss
                       ▼
┌─────────────────────────────────────────────────────┐
│             L2: Redis Cache (Distributed)            │
│  • Connection pooling                               │
│  • TTL: 30 minutes                                  │
│  • Pub/Sub invalidation                             │
└──────────────────────┬──────────────────────────────┘
                       │ Cache Miss
                       ▼
┌─────────────────────────────────────────────────────┐
│             L3: Database Cache (Persistent)          │
│  • Room database                                    │
│  • TTL: 24 hours                                    │
│  • Offline support                                  │
└─────────────────────────────────────────────────────┘
```

### CacheManager Implementation

```kotlin
@Singleton
class CacheManager @Inject constructor(
    private val memoryCache: MemoryCache,
    private val redisCache: RedisCache,
    private val databaseCache: DatabaseCache
) {
    suspend fun <T> get(key: String, loader: suspend () -> T): T {
        // L1: Check memory cache
        memoryCache.get<T>(key)?.let { return it }

        // L2: Check Redis cache
        redisCache.get<T>(key)?.let {
            memoryCache.put(key, it)
            return it
        }

        // L3: Check database cache
        databaseCache.get<T>(key)?.let {
            redisCache.put(key, it)
            memoryCache.put(key, it)
            return it
        }

        // Load from source and populate all layers
        val value = loader()
        put(key, value)
        return value
    }

    suspend fun <T> put(key: String, value: T) {
        memoryCache.put(key, value)
        redisCache.put(key, value)
        databaseCache.put(key, value)
    }

    suspend fun invalidate(key: String) {
        memoryCache.remove(key)
        redisCache.remove(key)
        databaseCache.remove(key)
    }
}
```

## Redis Integration (v5.6.0)

### Redis Configuration

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RedisModule {
    @Provides
    @Singleton
    fun provideRedisClient(): RedisClient {
        return RedisClient.create(
            RedisURI.builder()
                .withHost(BuildConfig.REDIS_HOST)
                .withPort(BuildConfig.REDIS_PORT)
                .withPassword(BuildConfig.REDIS_PASSWORD)
                .withSsl(true)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideRedisCache(client: RedisClient): RedisCache {
        return RedisCache(client)
    }
}
```

## Sentry Error Monitoring (v5.6.0)

### Sentry Integration

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SentryModule {
    @Provides
    @Singleton
    fun provideSentryClient(application: Application): SentryClient {
        SentryAndroid.init(application) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = BuildConfig.BUILD_TYPE
            options.release = BuildConfig.VERSION_NAME
            options.tracesSampleRate = 1.0
            options.isEnableAutoSessionTracking = true
        }
        return Sentry.getCurrentHub()
    }
}
```

### Error Tracking Usage

```kotlin
// Automatic crash reporting
// Manual error capture
try {
    executeQuantumCircuit(circuit)
} catch (e: QuantumExecutionException) {
    Sentry.captureException(e)
    throw e
}

// Performance monitoring
val transaction = Sentry.startTransaction("quantum-simulation", "task")
try {
    runSimulation()
    transaction.status = SpanStatus.OK
} finally {
    transaction.finish()
}

// Custom breadcrumbs
Sentry.addBreadcrumb(Breadcrumb().apply {
    category = "quantum"
    message = "Circuit executed with ${circuit.gateCount} gates"
    level = SentryLevel.INFO
})
```

## Operations Readiness Checklist (v5.6.0)

### Readiness Check System

```kotlin
interface OperationsReadinessApi {
    @GET("ops/readiness")
    suspend fun getReadinessStatus(): ReadinessResponse

    @GET("ops/health")
    suspend fun getHealthCheck(): HealthCheckResponse

    @GET("ops/dependencies")
    suspend fun getDependencyStatus(): DependencyResponse
}

data class ReadinessResponse(
    val isReady: Boolean,
    val checks: List<ReadinessCheck>,
    val timestamp: Long
)

data class ReadinessCheck(
    val name: String,
    val status: CheckStatus,
    val message: String?,
    val latencyMs: Long?
)

enum class CheckStatus {
    PASS, WARN, FAIL
}
```

### Readiness ViewModel

```kotlin
@HiltViewModel
class OperationsReadinessViewModel @Inject constructor(
    private val repository: OperationsReadinessRepository
) : ViewModel() {
    private val _readinessState = MutableStateFlow<ReadinessState>(ReadinessState.Loading)
    val readinessState: StateFlow<ReadinessState> = _readinessState.asStateFlow()

    fun checkReadiness() {
        viewModelScope.launch {
            _readinessState.value = ReadinessState.Loading
            repository.getReadinessStatus()
                .onSuccess { _readinessState.value = ReadinessState.Ready(it) }
                .onFailure { _readinessState.value = ReadinessState.Error(it.message) }
        }
    }
}
```
