package com.swiftquantum.data.dto

import com.swiftquantum.domain.model.BackendStatus
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumConnection
import com.swiftquantum.domain.model.IBMQuantumJob
import com.swiftquantum.domain.model.JobStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IBMConnectRequest(
    @SerialName("api_token")
    val apiToken: String
)

@Serializable
data class IBMQuantumBackendDto(
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("num_qubits")
    val numQubits: Int,
    val status: String = "unknown",
    @SerialName("pending_jobs")
    val pendingJobs: Int = 0,
    @SerialName("max_shots")
    val maxShots: Int = 100000,
    @SerialName("basis_gates")
    val basisGates: List<String> = emptyList(),
    @SerialName("coupling_map")
    val couplingMap: List<List<Int>>? = null,
    @SerialName("is_simulator")
    val isSimulator: Boolean = false,
    val version: String? = null
) {
    fun toDomain(): IBMQuantumBackend = IBMQuantumBackend(
        name = name,
        displayName = displayName,
        numQubits = numQubits,
        status = try { BackendStatus.valueOf(status.uppercase()) } catch (e: Exception) { BackendStatus.UNKNOWN },
        pendingJobs = pendingJobs,
        maxShots = maxShots,
        basisGates = basisGates,
        couplingMap = couplingMap,
        isSimulator = isSimulator,
        version = version
    )
}

@Serializable
data class IBMQuantumConnectionDto(
    @SerialName("is_connected")
    val isConnected: Boolean = false,
    @SerialName("user_id")
    val userId: String? = null,
    val username: String? = null,
    @SerialName("available_backends")
    val availableBackends: List<IBMQuantumBackendDto> = emptyList(),
    @SerialName("active_jobs")
    val activeJobs: List<IBMQuantumJobDto> = emptyList(),
    @SerialName("last_connected")
    val lastConnected: String? = null
) {
    fun toDomain(): IBMQuantumConnection = IBMQuantumConnection(
        isConnected = isConnected,
        userId = userId,
        username = username,
        availableBackends = availableBackends.map { it.toDomain() },
        activeJobs = activeJobs.map { it.toDomain() },
        lastConnected = lastConnected
    )
}

@Serializable
data class IBMQuantumJobDto(
    val id: String,
    @SerialName("circuit_id")
    val circuitId: String? = null,
    val backend: String,
    val status: String = "initializing",
    @SerialName("queue_position")
    val queuePosition: Int? = null,
    @SerialName("estimated_start_time")
    val estimatedStartTime: String? = null,
    @SerialName("estimated_completion_time")
    val estimatedCompletionTime: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("completed_at")
    val completedAt: String? = null,
    val result: ExecutionResultDto? = null,
    val error: String? = null
) {
    fun toDomain(): IBMQuantumJob = IBMQuantumJob(
        id = id,
        circuitId = circuitId,
        backend = backend,
        status = try { JobStatus.valueOf(status.uppercase()) } catch (e: Exception) { JobStatus.INITIALIZING },
        queuePosition = queuePosition,
        estimatedStartTime = estimatedStartTime,
        estimatedCompletionTime = estimatedCompletionTime,
        createdAt = createdAt,
        updatedAt = updatedAt,
        completedAt = completedAt,
        result = result?.toDomain(),
        error = error
    )
}

@Serializable
data class SubmitJobRequest(
    val circuit: CircuitDto,
    val backend: String,
    val shots: Int = 1024
)

@Serializable
data class JobStatusResponse(
    val job: IBMQuantumJobDto
)

@Serializable
data class BackendsResponse(
    val backends: List<IBMQuantumBackendDto>
)
