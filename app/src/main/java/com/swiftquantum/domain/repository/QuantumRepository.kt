package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.ExecutionResult
import kotlinx.coroutines.flow.Flow

interface QuantumRepository {
    // Circuit Operations
    suspend fun saveCircuit(circuit: Circuit): Result<Circuit>
    suspend fun getCircuit(id: String): Result<Circuit>
    suspend fun getMyCircuits(): Result<List<Circuit>>
    suspend fun updateCircuit(circuit: Circuit): Result<Circuit>
    suspend fun deleteCircuit(id: String): Result<Unit>

    // Simulation
    suspend fun runSimulation(
        circuit: Circuit,
        shots: Int = 1024,
        backend: ExecutionBackend = ExecutionBackend.RUST_SIMULATOR
    ): Result<ExecutionResult>

    fun observeSimulationProgress(executionId: String): Flow<ExecutionResult>

    // Execution History
    suspend fun getExecutionHistory(): Result<List<ExecutionResult>>
    suspend fun getExecutionResult(id: String): Result<ExecutionResult>

    // Local Simulation (Rust-based)
    suspend fun runLocalSimulation(
        circuit: Circuit,
        shots: Int = 1024
    ): Result<ExecutionResult>
}
