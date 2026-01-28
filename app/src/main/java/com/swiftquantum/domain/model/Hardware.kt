package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class IBMQuantumBackend(
    val name: String,
    val displayName: String,
    val numQubits: Int,
    val status: BackendStatus = BackendStatus.UNKNOWN,
    val pendingJobs: Int = 0,
    val maxShots: Int = 100000,
    val basisGates: List<String> = emptyList(),
    val couplingMap: List<List<Int>>? = null,
    val isSimulator: Boolean = false,
    val version: String? = null
)

@Serializable
enum class BackendStatus {
    ONLINE,
    OFFLINE,
    MAINTENANCE,
    UNKNOWN
}

@Serializable
data class IBMQuantumJob(
    val id: String,
    val circuitId: String? = null,
    val backend: String,
    val status: JobStatus = JobStatus.INITIALIZING,
    val queuePosition: Int? = null,
    val estimatedStartTime: String? = null,
    val estimatedCompletionTime: String? = null,
    val createdAt: String,
    val updatedAt: String? = null,
    val completedAt: String? = null,
    val result: ExecutionResult? = null,
    val error: String? = null
)

@Serializable
enum class JobStatus {
    INITIALIZING,
    QUEUED,
    VALIDATING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}

@Serializable
data class IBMQuantumConnection(
    val isConnected: Boolean = false,
    val apiToken: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val availableBackends: List<IBMQuantumBackend> = emptyList(),
    val activeJobs: List<IBMQuantumJob> = emptyList(),
    val lastConnected: String? = null
)
