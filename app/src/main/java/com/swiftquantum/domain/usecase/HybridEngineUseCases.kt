package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.BenchmarkResult
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.EngineStatus
import com.swiftquantum.domain.model.HybridEngineConfig
import com.swiftquantum.domain.model.HybridEngineType
import com.swiftquantum.domain.model.HybridExecutionResult
import com.swiftquantum.domain.model.OptimizationLevel
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.repository.BillingRepository
import com.swiftquantum.domain.repository.HybridEngineRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for executing circuit with hybrid engine
 */
class ExecuteWithHybridEngineUseCase @Inject constructor(
    private val hybridEngineRepository: HybridEngineRepository,
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(
        circuit: Circuit,
        engineType: HybridEngineType = HybridEngineType.RUST,
        shots: Int = 1024,
        optimizationLevel: OptimizationLevel = OptimizationLevel.BASIC,
        config: HybridEngineConfig? = null
    ): Result<HybridExecutionResult> {
        // Validate circuit
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(
                IllegalArgumentException("Invalid circuit: ${validation.errors.joinToString(", ")}")
            )
        }

        // Check user tier for engine access
        val userTier = billingRepository.getUserTier().first()
        if (engineType == HybridEngineType.CPP_HPC && userTier != UserTier.MASTER) {
            return Result.failure(
                IllegalAccessException("C++ HPC Engine requires MASTER tier subscription")
            )
        }

        // Validate shots
        val validatedShots = shots.coerceIn(1, 100000)

        return hybridEngineRepository.executeWithEngine(
            circuit = circuit,
            engineType = engineType,
            shots = validatedShots,
            optimizationLevel = optimizationLevel,
            config = config
        )
    }
}

/**
 * Use case for running benchmarks
 */
class RunBenchmarkUseCase @Inject constructor(
    private val hybridEngineRepository: HybridEngineRepository,
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(
        circuit: Circuit,
        shots: Int = 1024
    ): Result<BenchmarkResult> {
        // Validate circuit
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(
                IllegalArgumentException("Invalid circuit: ${validation.errors.joinToString(", ")}")
            )
        }

        // Check user tier - benchmarks require at least PRO tier
        val userTier = billingRepository.getUserTier().first()
        if (userTier == UserTier.FREE) {
            return Result.failure(
                IllegalAccessException("Benchmarks require PRO or MASTER tier subscription")
            )
        }

        // Validate shots
        val validatedShots = shots.coerceIn(1, 100000)

        return hybridEngineRepository.runBenchmark(circuit, validatedShots)
    }
}

/**
 * Use case for getting benchmark history
 */
class GetBenchmarkHistoryUseCase @Inject constructor(
    private val hybridEngineRepository: HybridEngineRepository
) {
    suspend operator fun invoke(
        numQubits: Int? = null,
        limit: Int = 10
    ): Result<List<BenchmarkResult>> {
        return hybridEngineRepository.getBenchmarks(numQubits, limit)
    }
}

/**
 * Use case for getting engine status
 */
class GetEngineStatusUseCase @Inject constructor(
    private val hybridEngineRepository: HybridEngineRepository
) {
    suspend operator fun invoke(): Result<List<EngineStatus>> {
        return hybridEngineRepository.getEngineStatus()
    }

    suspend operator fun invoke(engineType: HybridEngineType): Result<EngineStatus> {
        return hybridEngineRepository.getEngineStatus(engineType)
    }
}
