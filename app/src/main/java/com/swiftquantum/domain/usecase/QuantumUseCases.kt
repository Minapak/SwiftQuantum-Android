package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.repository.BillingRepository
import com.swiftquantum.domain.repository.QuantumRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RunSimulationUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository,
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(
        circuit: Circuit,
        shots: Int = 1024,
        backend: ExecutionBackend = ExecutionBackend.RUST_SIMULATOR
    ): Result<ExecutionResult> {
        // Validate circuit
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(IllegalArgumentException(validation.errors.joinToString(", ")))
        }

        // Check qubit limit based on user tier
        val userTier = billingRepository.getUserTier().first()
        if (circuit.numQubits > userTier.maxQubits) {
            return Result.failure(
                IllegalArgumentException(
                    "Your ${userTier.name} tier allows up to ${userTier.maxQubits} qubits. " +
                    "Upgrade to simulate ${circuit.numQubits} qubits."
                )
            )
        }

        // Check hardware access
        if (backend.isHardware && !userTier.hasHardwareAccess) {
            return Result.failure(
                IllegalArgumentException("Hardware access requires MASTER tier")
            )
        }

        // Validate shots
        val validatedShots = shots.coerceIn(1, 100000)

        return if (backend == ExecutionBackend.RUST_SIMULATOR) {
            quantumRepository.runLocalSimulation(circuit, validatedShots)
        } else {
            quantumRepository.runSimulation(circuit, validatedShots, backend)
        }
    }
}

class SaveCircuitUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository
) {
    suspend operator fun invoke(circuit: Circuit): Result<Circuit> {
        if (circuit.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Circuit name cannot be empty"))
        }
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(IllegalArgumentException(validation.errors.joinToString(", ")))
        }
        return quantumRepository.saveCircuit(circuit)
    }
}

class GetMyCircuitsUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository
) {
    suspend operator fun invoke(): Result<List<Circuit>> {
        return quantumRepository.getMyCircuits()
    }
}

class GetCircuitUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository
) {
    suspend operator fun invoke(id: String): Result<Circuit> {
        return quantumRepository.getCircuit(id)
    }
}

class DeleteCircuitUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return quantumRepository.deleteCircuit(id)
    }
}

class GetExecutionHistoryUseCase @Inject constructor(
    private val quantumRepository: QuantumRepository
) {
    suspend operator fun invoke(): Result<List<ExecutionResult>> {
        return quantumRepository.getExecutionHistory()
    }
}

class GetMaxQubitsUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(): Int {
        return billingRepository.getUserTier().first().maxQubits
    }
}
