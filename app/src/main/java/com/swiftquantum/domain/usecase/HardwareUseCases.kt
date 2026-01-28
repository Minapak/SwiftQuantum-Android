package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumConnection
import com.swiftquantum.domain.model.IBMQuantumJob
import com.swiftquantum.domain.repository.BillingRepository
import com.swiftquantum.domain.repository.HardwareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ConnectToIBMQuantumUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository,
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(apiToken: String): Result<IBMQuantumConnection> {
        // Check if user has hardware access
        val userTier = billingRepository.getUserTier().first()
        if (!userTier.hasHardwareAccess) {
            return Result.failure(
                IllegalAccessException("IBM Quantum hardware access requires MASTER tier")
            )
        }

        if (apiToken.isBlank()) {
            return Result.failure(IllegalArgumentException("API token cannot be empty"))
        }

        return hardwareRepository.connectToIBM(apiToken)
    }
}

class DisconnectFromIBMQuantumUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return hardwareRepository.disconnectFromIBM()
    }
}

class GetAvailableBackendsUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(): Result<List<IBMQuantumBackend>> {
        return hardwareRepository.getAvailableBackends()
    }
}

class SubmitHardwareJobUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository,
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(
        circuit: Circuit,
        backend: String,
        shots: Int = 1024
    ): Result<IBMQuantumJob> {
        // Validate user tier
        val userTier = billingRepository.getUserTier().first()
        if (!userTier.hasHardwareAccess) {
            return Result.failure(
                IllegalAccessException("IBM Quantum hardware access requires MASTER tier")
            )
        }

        // Validate circuit
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(IllegalArgumentException(validation.errors.joinToString(", ")))
        }

        // Validate shots
        val validatedShots = shots.coerceIn(1, 100000)

        return hardwareRepository.submitJob(circuit, backend, validatedShots)
    }
}

class GetJobStatusUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(jobId: String): Result<IBMQuantumJob> {
        return hardwareRepository.getJobStatus(jobId)
    }
}

class CancelJobUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(jobId: String): Result<Unit> {
        return hardwareRepository.cancelJob(jobId)
    }
}

class GetJobResultUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(jobId: String): Result<ExecutionResult> {
        return hardwareRepository.getJobResult(jobId)
    }
}

class ObserveIBMConnectionUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    operator fun invoke(): Flow<IBMQuantumConnection> {
        return hardwareRepository.connectionState
    }
}

class ObserveJobStatusUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    operator fun invoke(jobId: String): Flow<IBMQuantumJob> {
        return hardwareRepository.observeJobStatus(jobId)
    }
}

class GetActiveJobsUseCase @Inject constructor(
    private val hardwareRepository: HardwareRepository
) {
    suspend operator fun invoke(): Result<List<IBMQuantumJob>> {
        return hardwareRepository.getActiveJobs()
    }
}
