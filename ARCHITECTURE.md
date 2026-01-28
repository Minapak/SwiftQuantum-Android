# SwiftQuantum Android Architecture

## Overview

SwiftQuantum Android follows Clean Architecture principles with MVVM pattern for the presentation layer.

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
└─────────────────────────────────────────────────────┘
```

## Module Structure

### Domain Layer
- **Models**: Core business entities (User, Circuit, Gate, ExecutionResult)
- **Repository Interfaces**: Abstract data access contracts
- **Use Cases**: Single-responsibility business operations

### Data Layer
- **API Interfaces**: Retrofit service definitions
- **DTOs**: Network request/response models
- **Repository Implementations**: Concrete data access
- **Local Storage**: DataStore for preferences and tokens

### Presentation Layer
- **ViewModels**: UI state management with StateFlow
- **Screens**: Jetpack Compose UI
- **Components**: Reusable UI elements (BlochSphere, Histogram)
- **Navigation**: Compose Navigation with bottom nav

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
