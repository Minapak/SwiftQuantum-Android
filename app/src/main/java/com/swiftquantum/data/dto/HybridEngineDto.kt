package com.swiftquantum.data.dto

import com.swiftquantum.domain.model.BenchmarkResult
import com.swiftquantum.domain.model.EngineBenchmarkEntry
import com.swiftquantum.domain.model.EnginePerformanceMetrics
import com.swiftquantum.domain.model.EngineStatus
import com.swiftquantum.domain.model.ExecutionStatus
import com.swiftquantum.domain.model.HybridEngineConfig
import com.swiftquantum.domain.model.HybridEngineType
import com.swiftquantum.domain.model.HybridExecutionResult
import com.swiftquantum.domain.model.OptimizationLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request DTO for hybrid engine execution
 */
@Serializable
data class HybridExecutionRequestDto(
    val circuit: CircuitDto,

    @SerialName("engine_type")
    val engineType: String = "rust",

    val shots: Int = 1024,

    @SerialName("optimization_level")
    val optimizationLevel: String = "basic",

    @SerialName("return_state_vector")
    val returnStateVector: Boolean = false,

    val config: HybridEngineConfigDto? = null
)

/**
 * Config DTO for hybrid engine
 */
@Serializable
data class HybridEngineConfigDto(
    @SerialName("engine_type")
    val engineType: String = "rust",

    @SerialName("optimization_level")
    val optimizationLevel: String = "basic",

    @SerialName("cache_enabled")
    val cacheEnabled: Boolean = true,

    @SerialName("parallel_shots")
    val parallelShots: Boolean = true,

    val precision: String = "double"
) {
    fun toDomain(): HybridEngineConfig = HybridEngineConfig(
        engineType = HybridEngineType.fromString(engineType) ?: HybridEngineType.RUST,
        optimizationLevel = try {
            OptimizationLevel.valueOf(optimizationLevel.uppercase())
        } catch (e: Exception) {
            OptimizationLevel.BASIC
        },
        cacheEnabled = cacheEnabled,
        parallelShots = parallelShots,
        precision = precision
    )

    companion object {
        fun fromDomain(config: HybridEngineConfig): HybridEngineConfigDto = HybridEngineConfigDto(
            engineType = config.engineType.name.lowercase(),
            optimizationLevel = config.optimizationLevel.name.lowercase(),
            cacheEnabled = config.cacheEnabled,
            parallelShots = config.parallelShots,
            precision = config.precision
        )
    }
}

/**
 * Response DTO for hybrid engine execution
 */
@Serializable
data class HybridExecutionResultDto(
    val id: String? = null,

    @SerialName("circuit_id")
    val circuitId: String? = null,

    @SerialName("engine_type")
    val engineType: String,

    val status: String = "completed",

    val counts: Map<String, Int> = emptyMap(),

    val probabilities: Map<String, Double> = emptyMap(),

    @SerialName("state_vector")
    val stateVector: List<ComplexNumberDto>? = null,

    val shots: Int = 1024,

    val metrics: EnginePerformanceMetricsDto,

    val error: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
) {
    fun toDomain(): HybridExecutionResult = HybridExecutionResult(
        id = id,
        circuitId = circuitId,
        engineType = HybridEngineType.fromString(engineType) ?: HybridEngineType.RUST,
        status = try {
            ExecutionStatus.valueOf(status.uppercase())
        } catch (e: Exception) {
            ExecutionStatus.COMPLETED
        },
        counts = counts,
        probabilities = probabilities,
        stateVector = stateVector?.map { it.toDomain() },
        shots = shots,
        metrics = metrics.toDomain(),
        error = error,
        createdAt = createdAt
    )
}

/**
 * Performance metrics DTO
 */
@Serializable
data class EnginePerformanceMetricsDto(
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
    fun toDomain(): EnginePerformanceMetrics = EnginePerformanceMetrics(
        executionTimeMs = executionTimeMs,
        memoryUsedBytes = memoryUsedBytes,
        speedupFactor = speedupFactor,
        gatesPerSecond = gatesPerSecond,
        circuitDepth = circuitDepth,
        optimizationTimeMs = optimizationTimeMs,
        cacheHit = cacheHit
    )

    companion object {
        fun fromDomain(metrics: EnginePerformanceMetrics): EnginePerformanceMetricsDto =
            EnginePerformanceMetricsDto(
                executionTimeMs = metrics.executionTimeMs,
                memoryUsedBytes = metrics.memoryUsedBytes,
                speedupFactor = metrics.speedupFactor,
                gatesPerSecond = metrics.gatesPerSecond,
                circuitDepth = metrics.circuitDepth,
                optimizationTimeMs = metrics.optimizationTimeMs,
                cacheHit = metrics.cacheHit
            )
    }
}

/**
 * Benchmark result DTO
 */
@Serializable
data class BenchmarkResultDto(
    val id: String? = null,

    @SerialName("circuit_name")
    val circuitName: String,

    @SerialName("num_qubits")
    val numQubits: Int,

    @SerialName("circuit_depth")
    val circuitDepth: Int,

    val shots: Int,

    val results: List<EngineBenchmarkEntryDto>,

    @SerialName("created_at")
    val createdAt: String? = null
) {
    fun toDomain(): BenchmarkResult = BenchmarkResult(
        id = id,
        circuitName = circuitName,
        numQubits = numQubits,
        circuitDepth = circuitDepth,
        shots = shots,
        results = results.map { it.toDomain() },
        createdAt = createdAt
    )
}

/**
 * Single engine benchmark entry DTO
 */
@Serializable
data class EngineBenchmarkEntryDto(
    @SerialName("engine_type")
    val engineType: String,

    @SerialName("execution_time_ms")
    val executionTimeMs: Long,

    @SerialName("memory_used_bytes")
    val memoryUsedBytes: Long,

    @SerialName("speedup_factor")
    val speedupFactor: Double,

    val success: Boolean = true,

    val error: String? = null
) {
    fun toDomain(): EngineBenchmarkEntry = EngineBenchmarkEntry(
        engineType = HybridEngineType.fromString(engineType) ?: HybridEngineType.PYTHON,
        executionTimeMs = executionTimeMs,
        memoryUsedBytes = memoryUsedBytes,
        speedupFactor = speedupFactor,
        success = success,
        error = error
    )
}

/**
 * Engine status DTO
 */
@Serializable
data class EngineStatusDto(
    @SerialName("engine_type")
    val engineType: String,

    @SerialName("is_available")
    val isAvailable: Boolean,

    @SerialName("current_load")
    val currentLoad: Double? = null,

    @SerialName("queue_length")
    val queueLength: Int? = null,

    @SerialName("estimated_wait_ms")
    val estimatedWaitMs: Long? = null,

    val version: String? = null
) {
    fun toDomain(): EngineStatus = EngineStatus(
        engineType = HybridEngineType.fromString(engineType) ?: HybridEngineType.RUST,
        isAvailable = isAvailable,
        currentLoad = currentLoad,
        queueLength = queueLength,
        estimatedWaitMs = estimatedWaitMs,
        version = version
    )
}
