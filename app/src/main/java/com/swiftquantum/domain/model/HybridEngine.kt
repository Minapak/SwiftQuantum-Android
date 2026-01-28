package com.swiftquantum.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Cloud Hybrid Engine types with their performance characteristics
 */
@Serializable
enum class HybridEngineType(
    val displayName: String,
    val description: String,
    val speedupFactor: Double,
    val isCloud: Boolean = true
) {
    @SerialName("local")
    LOCAL(
        displayName = "Local",
        description = "Local device simulation",
        speedupFactor = 1.0,
        isCloud = false
    ),

    @SerialName("python")
    PYTHON(
        displayName = "Python Engine",
        description = "NumPy/Qiskit based simulation (1x baseline)",
        speedupFactor = 1.0
    ),

    @SerialName("rust")
    RUST(
        displayName = "Rust Engine",
        description = "High-performance Rust simulation (900x faster)",
        speedupFactor = 900.0
    ),

    @SerialName("cpp_hpc")
    CPP_HPC(
        displayName = "C++ HPC Engine",
        description = "Optimized C++ with OpenMP/MPI (1200x faster)",
        speedupFactor = 1200.0
    );

    companion object {
        val cloudEngines = entries.filter { it.isCloud }

        fun fromString(name: String): HybridEngineType? = entries.find {
            it.name.equals(name, ignoreCase = true) ||
            it.displayName.equals(name, ignoreCase = true)
        }
    }
}

/**
 * Optimization levels for hybrid engine execution
 */
@Serializable
enum class OptimizationLevel(
    val displayName: String,
    val description: String,
    val value: Int
) {
    @SerialName("none")
    NONE("None", "No optimization applied", 0),

    @SerialName("basic")
    BASIC("Basic", "Gate simplification and merging", 1),

    @SerialName("aggressive")
    AGGRESSIVE("Aggressive", "Full circuit optimization", 2),

    @SerialName("maximum")
    MAXIMUM("Maximum", "Maximum optimization with transpilation", 3)
}

/**
 * Configuration for hybrid engine execution
 */
@Serializable
data class HybridEngineConfig(
    @SerialName("engine_type")
    val engineType: HybridEngineType = HybridEngineType.RUST,

    @SerialName("optimization_level")
    val optimizationLevel: OptimizationLevel = OptimizationLevel.BASIC,

    @SerialName("cache_enabled")
    val cacheEnabled: Boolean = true,

    @SerialName("parallel_shots")
    val parallelShots: Boolean = true,

    @SerialName("precision")
    val precision: String = "double" // "float" or "double"
) {
    companion object {
        fun default() = HybridEngineConfig()

        fun highPerformance() = HybridEngineConfig(
            engineType = HybridEngineType.CPP_HPC,
            optimizationLevel = OptimizationLevel.MAXIMUM,
            cacheEnabled = true,
            parallelShots = true
        )

        fun balanced() = HybridEngineConfig(
            engineType = HybridEngineType.RUST,
            optimizationLevel = OptimizationLevel.AGGRESSIVE,
            cacheEnabled = true,
            parallelShots = true
        )
    }
}

/**
 * Performance metrics from hybrid engine execution
 */
@Serializable
data class EnginePerformanceMetrics(
    @SerialName("execution_time_ms")
    val executionTimeMs: Long,

    @SerialName("memory_used_bytes")
    val memoryUsedBytes: Long,

    @SerialName("speedup_factor")
    val speedupFactor: Double,

    @SerialName("gates_per_second")
    val gatesPerSecond: Double? = null,

    @SerialName("circuit_depth")
    val circuitDepth: Int? = null,

    @SerialName("optimization_time_ms")
    val optimizationTimeMs: Long? = null,

    @SerialName("cache_hit")
    val cacheHit: Boolean = false
) {
    val memoryUsedMB: Double
        get() = memoryUsedBytes / (1024.0 * 1024.0)

    val formattedExecutionTime: String
        get() = when {
            executionTimeMs < 1 -> "<1ms"
            executionTimeMs < 1000 -> "${executionTimeMs}ms"
            else -> String.format("%.2fs", executionTimeMs / 1000.0)
        }

    val formattedSpeedup: String
        get() = when {
            speedupFactor >= 1000 -> String.format("%.1fx", speedupFactor / 1000) + "K"
            speedupFactor >= 100 -> String.format("%.0fx", speedupFactor)
            speedupFactor >= 10 -> String.format("%.1fx", speedupFactor)
            else -> String.format("%.2fx", speedupFactor)
        }
}

/**
 * Request for hybrid engine execution
 */
@Serializable
data class HybridExecutionRequest(
    val circuit: Circuit,

    @SerialName("engine_type")
    val engineType: HybridEngineType = HybridEngineType.RUST,

    val shots: Int = 1024,

    @SerialName("optimization_level")
    val optimizationLevel: OptimizationLevel = OptimizationLevel.BASIC,

    @SerialName("return_state_vector")
    val returnStateVector: Boolean = false,

    @SerialName("config")
    val config: HybridEngineConfig? = null
)

/**
 * Result from hybrid engine execution
 */
@Serializable
data class HybridExecutionResult(
    val id: String? = null,

    @SerialName("circuit_id")
    val circuitId: String? = null,

    @SerialName("engine_type")
    val engineType: HybridEngineType,

    val status: ExecutionStatus = ExecutionStatus.COMPLETED,

    val counts: Map<String, Int> = emptyMap(),

    val probabilities: Map<String, Double> = emptyMap(),

    @SerialName("state_vector")
    val stateVector: List<ComplexNumber>? = null,

    val shots: Int = 1024,

    val metrics: EnginePerformanceMetrics,

    val error: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
) {
    val isSuccess: Boolean
        get() = status == ExecutionStatus.COMPLETED && error == null

    fun toExecutionResult(): ExecutionResult = ExecutionResult(
        id = id,
        circuitId = circuitId,
        status = status,
        backend = when (engineType) {
            HybridEngineType.LOCAL -> ExecutionBackend.RUST_SIMULATOR
            HybridEngineType.PYTHON -> ExecutionBackend.IBM_QASM_SIMULATOR
            HybridEngineType.RUST -> ExecutionBackend.RUST_SIMULATOR
            HybridEngineType.CPP_HPC -> ExecutionBackend.RUST_SIMULATOR
        },
        counts = counts,
        probabilities = probabilities,
        stateVector = stateVector,
        shots = shots,
        executionTimeMs = metrics.executionTimeMs,
        error = error,
        createdAt = createdAt
    )
}

/**
 * Benchmark result comparing different engines
 */
@Serializable
data class BenchmarkResult(
    val id: String? = null,

    @SerialName("circuit_name")
    val circuitName: String,

    @SerialName("num_qubits")
    val numQubits: Int,

    @SerialName("circuit_depth")
    val circuitDepth: Int,

    val shots: Int,

    val results: List<EngineBenchmarkEntry>,

    @SerialName("created_at")
    val createdAt: String? = null
) {
    val fastestEngine: HybridEngineType?
        get() = results.minByOrNull { it.executionTimeMs }?.engineType

    val baselineTime: Long
        get() = results.find { it.engineType == HybridEngineType.PYTHON }?.executionTimeMs ?: 0
}

/**
 * Single engine benchmark entry
 */
@Serializable
data class EngineBenchmarkEntry(
    @SerialName("engine_type")
    val engineType: HybridEngineType,

    @SerialName("execution_time_ms")
    val executionTimeMs: Long,

    @SerialName("memory_used_bytes")
    val memoryUsedBytes: Long,

    @SerialName("speedup_factor")
    val speedupFactor: Double,

    val success: Boolean = true,

    val error: String? = null
)

/**
 * Engine status information
 */
@Serializable
data class EngineStatus(
    @SerialName("engine_type")
    val engineType: HybridEngineType,

    @SerialName("is_available")
    val isAvailable: Boolean,

    @SerialName("current_load")
    val currentLoad: Double? = null, // 0.0 to 1.0

    @SerialName("queue_length")
    val queueLength: Int? = null,

    @SerialName("estimated_wait_ms")
    val estimatedWaitMs: Long? = null,

    val version: String? = null
) {
    val loadPercentage: Int
        get() = ((currentLoad ?: 0.0) * 100).toInt()

    val statusText: String
        get() = when {
            !isAvailable -> "Offline"
            (currentLoad ?: 0.0) > 0.9 -> "High Load"
            (currentLoad ?: 0.0) > 0.5 -> "Moderate"
            else -> "Available"
        }
}
