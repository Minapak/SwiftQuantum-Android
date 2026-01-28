package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.BenchmarkResult
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.EngineStatus
import com.swiftquantum.domain.model.HybridEngineConfig
import com.swiftquantum.domain.model.HybridEngineType
import com.swiftquantum.domain.model.HybridExecutionResult
import com.swiftquantum.domain.model.OptimizationLevel

/**
 * Repository interface for Hybrid Engine operations
 */
interface HybridEngineRepository {

    /**
     * Execute a circuit using the specified hybrid engine
     */
    suspend fun executeWithEngine(
        circuit: Circuit,
        engineType: HybridEngineType = HybridEngineType.RUST,
        shots: Int = 1024,
        optimizationLevel: OptimizationLevel = OptimizationLevel.BASIC,
        config: HybridEngineConfig? = null
    ): Result<HybridExecutionResult>

    /**
     * Run a benchmark comparing different engines
     */
    suspend fun runBenchmark(
        circuit: Circuit,
        shots: Int = 1024
    ): Result<BenchmarkResult>

    /**
     * Get previous benchmark results
     */
    suspend fun getBenchmarks(
        numQubits: Int? = null,
        limit: Int = 10
    ): Result<List<BenchmarkResult>>

    /**
     * Get status of all hybrid engines
     */
    suspend fun getEngineStatus(): Result<List<EngineStatus>>

    /**
     * Get status of a specific engine
     */
    suspend fun getEngineStatus(engineType: HybridEngineType): Result<EngineStatus>
}
