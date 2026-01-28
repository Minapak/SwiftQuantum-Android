package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumConnection
import com.swiftquantum.domain.model.IBMQuantumJob
import kotlinx.coroutines.flow.Flow

interface HardwareRepository {
    val connectionState: Flow<IBMQuantumConnection>

    suspend fun connectToIBM(apiToken: String): Result<IBMQuantumConnection>
    suspend fun disconnectFromIBM(): Result<Unit>
    suspend fun getAvailableBackends(): Result<List<IBMQuantumBackend>>
    suspend fun getBackendStatus(backendName: String): Result<IBMQuantumBackend>
    suspend fun submitJob(circuit: Circuit, backend: String, shots: Int): Result<IBMQuantumJob>
    suspend fun getJobStatus(jobId: String): Result<IBMQuantumJob>
    suspend fun cancelJob(jobId: String): Result<Unit>
    suspend fun getJobResult(jobId: String): Result<ExecutionResult>
    suspend fun getActiveJobs(): Result<List<IBMQuantumJob>>
    suspend fun getJobHistory(): Result<List<IBMQuantumJob>>
    fun observeJobStatus(jobId: String): Flow<IBMQuantumJob>
}
