package com.swiftquantum.data.repository

import com.swiftquantum.data.api.QuantumApi
import com.swiftquantum.data.dto.CircuitDto
import com.swiftquantum.data.dto.CreateCircuitRequest
import com.swiftquantum.data.dto.GateDto
import com.swiftquantum.data.dto.SimulationRequest
import com.swiftquantum.data.dto.UpdateCircuitRequest
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ComplexNumber
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.ExecutionStatus
import com.swiftquantum.domain.repository.QuantumRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@Singleton
class QuantumRepositoryImpl @Inject constructor(
    private val quantumApi: QuantumApi
) : QuantumRepository {

    override suspend fun saveCircuit(circuit: Circuit): Result<Circuit> {
        return try {
            val request = CreateCircuitRequest(
                name = circuit.name,
                description = circuit.description,
                numQubits = circuit.numQubits,
                gates = circuit.gates.map { GateDto.fromDomain(it) }
            )
            val response = quantumApi.createCircuit(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to save circuit"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to save circuit")
            Result.failure(e)
        }
    }

    override suspend fun getCircuit(id: String): Result<Circuit> {
        return try {
            val response = quantumApi.getCircuit(id)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Circuit not found"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get circuit")
            Result.failure(e)
        }
    }

    override suspend fun getMyCircuits(): Result<List<Circuit>> {
        return try {
            val response = quantumApi.getMyCircuits()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.map { it.toDomain() } ?: emptyList())
            } else {
                // Return empty list for guest/offline mode
                Timber.w("Failed to get circuits from API, returning empty list")
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get circuits, returning empty list")
            // Return empty list on API failure
            Result.success(emptyList())
        }
    }

    override suspend fun updateCircuit(circuit: Circuit): Result<Circuit> {
        return try {
            val id = circuit.id ?: return Result.failure(Exception("Circuit ID is required"))
            val request = UpdateCircuitRequest(
                name = circuit.name,
                description = circuit.description,
                gates = circuit.gates.map { GateDto.fromDomain(it) }
            )
            val response = quantumApi.updateCircuit(id, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to update circuit"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update circuit")
            Result.failure(e)
        }
    }

    override suspend fun deleteCircuit(id: String): Result<Unit> {
        return try {
            val response = quantumApi.deleteCircuit(id)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to delete circuit"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete circuit")
            Result.failure(e)
        }
    }

    override suspend fun runSimulation(
        circuit: Circuit,
        shots: Int,
        backend: ExecutionBackend
    ): Result<ExecutionResult> {
        return try {
            val request = SimulationRequest(
                circuit = CircuitDto.fromDomain(circuit),
                shots = shots,
                backend = backend.name.lowercase()
            )
            val response = quantumApi.runSimulation(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Simulation failed"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulation failed")
            Result.failure(e)
        }
    }

    override fun observeSimulationProgress(executionId: String): Flow<ExecutionResult> = flow {
        var completed = false
        while (!completed) {
            try {
                val response = quantumApi.getSimulationResult(executionId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val result = response.body()?.data?.toDomain()!!
                    emit(result)
                    completed = result.status in listOf(
                        ExecutionStatus.COMPLETED,
                        ExecutionStatus.FAILED,
                        ExecutionStatus.CANCELLED
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to get simulation progress")
            }
            if (!completed) {
                delay(1000)
            }
        }
    }

    override suspend fun getExecutionHistory(): Result<List<ExecutionResult>> {
        return try {
            val response = quantumApi.getExecutionHistory()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.map { it.toDomain() } ?: emptyList())
            } else {
                // Return empty list for guest/offline mode
                Timber.w("Failed to get execution history from API, returning empty list")
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get execution history, returning empty list")
            // Return empty list on API failure
            Result.success(emptyList())
        }
    }

    override suspend fun getExecutionResult(id: String): Result<ExecutionResult> {
        return try {
            val response = quantumApi.getSimulationResult(id)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Result not found"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get execution result")
            Result.failure(e)
        }
    }

    override suspend fun runLocalSimulation(circuit: Circuit, shots: Int): Result<ExecutionResult> {
        return try {
            val startTime = System.currentTimeMillis()

            // Simplified local quantum simulation
            val numStates = 1 shl circuit.numQubits
            val stateVector = simulateCircuit(circuit)

            // Sample from state vector
            val counts = mutableMapOf<String, Int>()
            val probabilities = stateVector.map { it.probability }

            repeat(shots) {
                val state = sampleState(probabilities)
                val stateString = state.toString(2).padStart(circuit.numQubits, '0')
                counts[stateString] = counts.getOrDefault(stateString, 0) + 1
            }

            val probabilitiesMap = counts.mapValues { it.value.toDouble() / shots }
            val executionTime = System.currentTimeMillis() - startTime

            val result = ExecutionResult(
                id = "local_${System.currentTimeMillis()}",
                circuitId = circuit.id,
                status = ExecutionStatus.COMPLETED,
                backend = ExecutionBackend.RUST_SIMULATOR,
                counts = counts,
                probabilities = probabilitiesMap,
                stateVector = stateVector,
                shots = shots,
                executionTimeMs = executionTime,
                fidelity = 1.0
            )

            Result.success(result)
        } catch (e: Exception) {
            Timber.e(e, "Local simulation failed")
            Result.failure(e)
        }
    }

    private fun simulateCircuit(circuit: Circuit): List<ComplexNumber> {
        val numStates = 1 shl circuit.numQubits
        var stateVector = MutableList(numStates) { i ->
            if (i == 0) ComplexNumber(1.0, 0.0) else ComplexNumber(0.0, 0.0)
        }

        // Apply gates in order
        circuit.gates.sortedBy { it.position }.forEach { gate ->
            stateVector = applyGate(stateVector, gate, circuit.numQubits)
        }

        return stateVector
    }

    private fun applyGate(
        state: MutableList<ComplexNumber>,
        gate: com.swiftquantum.domain.model.Gate,
        numQubits: Int
    ): MutableList<ComplexNumber> {
        val newState = state.toMutableList()
        val target = gate.targetQubits.firstOrNull() ?: return newState

        when (gate.type) {
            com.swiftquantum.domain.model.GateType.H -> {
                val factor = 1.0 / sqrt(2.0)
                for (i in state.indices) {
                    val bitValue = (i shr target) and 1
                    val partner = i xor (1 shl target)
                    if (bitValue == 0 && i < partner) {
                        val a = state[i]
                        val b = state[partner]
                        newState[i] = ComplexNumber(
                            (a.real + b.real) * factor,
                            (a.imaginary + b.imaginary) * factor
                        )
                        newState[partner] = ComplexNumber(
                            (a.real - b.real) * factor,
                            (a.imaginary - b.imaginary) * factor
                        )
                    }
                }
            }
            com.swiftquantum.domain.model.GateType.X -> {
                for (i in state.indices) {
                    val partner = i xor (1 shl target)
                    if (i < partner) {
                        val temp = newState[i]
                        newState[i] = newState[partner]
                        newState[partner] = temp
                    }
                }
            }
            com.swiftquantum.domain.model.GateType.Z -> {
                for (i in state.indices) {
                    if ((i shr target) and 1 == 1) {
                        newState[i] = ComplexNumber(-state[i].real, -state[i].imaginary)
                    }
                }
            }
            com.swiftquantum.domain.model.GateType.CNOT -> {
                val control = gate.controlQubits.firstOrNull() ?: return newState
                for (i in state.indices) {
                    if ((i shr control) and 1 == 1) {
                        val partner = i xor (1 shl target)
                        if (i < partner) {
                            val temp = newState[i]
                            newState[i] = newState[partner]
                            newState[partner] = temp
                        }
                    }
                }
            }
            else -> {
                // For other gates, just return state unchanged (simplified)
            }
        }

        return newState
    }

    private fun sampleState(probabilities: List<Double>): Int {
        val random = Random.nextDouble()
        var cumulative = 0.0
        for (i in probabilities.indices) {
            cumulative += probabilities[i]
            if (random <= cumulative) {
                return i
            }
        }
        return probabilities.lastIndex
    }
}
