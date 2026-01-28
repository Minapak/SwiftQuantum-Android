package com.swiftquantum.data.repository

import com.swiftquantum.data.api.BridgeApi
import com.swiftquantum.data.dto.CircuitDto
import com.swiftquantum.data.dto.IBMConnectRequest
import com.swiftquantum.data.dto.SubmitJobRequest
import com.swiftquantum.data.local.TokenManager
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumConnection
import com.swiftquantum.domain.model.IBMQuantumJob
import com.swiftquantum.domain.model.JobStatus
import com.swiftquantum.domain.repository.HardwareRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HardwareRepositoryImpl @Inject constructor(
    private val bridgeApi: BridgeApi,
    private val tokenManager: TokenManager
) : HardwareRepository {

    private val _connectionState = MutableStateFlow(IBMQuantumConnection())
    override val connectionState: Flow<IBMQuantumConnection> = _connectionState.asStateFlow()

    override suspend fun connectToIBM(apiToken: String): Result<IBMQuantumConnection> {
        return try {
            val response = bridgeApi.connectToIBM(IBMConnectRequest(apiToken))
            if (response.isSuccessful && response.body()?.success == true) {
                val connection = response.body()?.data?.toDomain()?.copy(apiToken = apiToken)!!
                tokenManager.saveIBMApiToken(apiToken)
                _connectionState.value = connection
                Result.success(connection)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to connect to IBM Quantum"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to connect to IBM Quantum")
            Result.failure(e)
        }
    }

    override suspend fun disconnectFromIBM(): Result<Unit> {
        return try {
            bridgeApi.disconnectFromIBM()
            tokenManager.clearIBMApiToken()
            _connectionState.value = IBMQuantumConnection()
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to disconnect from IBM Quantum")
            tokenManager.clearIBMApiToken()
            _connectionState.value = IBMQuantumConnection()
            Result.success(Unit)
        }
    }

    override suspend fun getAvailableBackends(): Result<List<IBMQuantumBackend>> {
        return try {
            val response = bridgeApi.getAvailableBackends()
            if (response.isSuccessful && response.body()?.success == true) {
                val backends = response.body()?.data?.backends?.map { it.toDomain() } ?: emptyList()
                _connectionState.value = _connectionState.value.copy(availableBackends = backends)
                Result.success(backends)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to get backends"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get available backends")
            Result.failure(e)
        }
    }

    override suspend fun getBackendStatus(backendName: String): Result<IBMQuantumBackend> {
        return try {
            val response = bridgeApi.getBackendStatus(backendName)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.toDomain()!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Backend not found"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get backend status")
            Result.failure(e)
        }
    }

    override suspend fun submitJob(circuit: Circuit, backend: String, shots: Int): Result<IBMQuantumJob> {
        return try {
            val request = SubmitJobRequest(
                circuit = CircuitDto.fromDomain(circuit),
                backend = backend,
                shots = shots
            )
            val response = bridgeApi.submitJob(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val job = response.body()?.data?.toDomain()!!
                updateActiveJobs(job)
                Result.success(job)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to submit job"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to submit job")
            Result.failure(e)
        }
    }

    override suspend fun getJobStatus(jobId: String): Result<IBMQuantumJob> {
        return try {
            val response = bridgeApi.getJobStatus(jobId)
            if (response.isSuccessful && response.body()?.success == true) {
                val job = response.body()?.data?.toDomain()!!
                updateActiveJobs(job)
                Result.success(job)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Job not found"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get job status")
            Result.failure(e)
        }
    }

    override suspend fun cancelJob(jobId: String): Result<Unit> {
        return try {
            val response = bridgeApi.cancelJob(jobId)
            if (response.isSuccessful && response.body()?.success == true) {
                val currentJobs = _connectionState.value.activeJobs.toMutableList()
                currentJobs.removeAll { it.id == jobId }
                _connectionState.value = _connectionState.value.copy(activeJobs = currentJobs)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to cancel job"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to cancel job")
            Result.failure(e)
        }
    }

    override suspend fun getJobResult(jobId: String): Result<ExecutionResult> {
        return try {
            val response = bridgeApi.getJobResult(jobId)
            if (response.isSuccessful && response.body()?.success == true) {
                val job = response.body()?.data?.toDomain()!!
                job.result?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No result available for this job"))
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to get job result"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get job result")
            Result.failure(e)
        }
    }

    override suspend fun getActiveJobs(): Result<List<IBMQuantumJob>> {
        return try {
            val response = bridgeApi.getActiveJobs()
            if (response.isSuccessful && response.body()?.success == true) {
                val jobs = response.body()?.data?.map { it.toDomain() } ?: emptyList()
                _connectionState.value = _connectionState.value.copy(activeJobs = jobs)
                Result.success(jobs)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to get active jobs"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get active jobs")
            Result.failure(e)
        }
    }

    override suspend fun getJobHistory(): Result<List<IBMQuantumJob>> {
        return try {
            val response = bridgeApi.getJobHistory()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to get job history"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get job history")
            Result.failure(e)
        }
    }

    override fun observeJobStatus(jobId: String): Flow<IBMQuantumJob> = flow {
        var completed = false
        while (!completed) {
            try {
                val response = bridgeApi.getJobStatus(jobId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val job = response.body()?.data?.toDomain()!!
                    emit(job)
                    completed = job.status in listOf(
                        JobStatus.COMPLETED,
                        JobStatus.FAILED,
                        JobStatus.CANCELLED
                    )
                    updateActiveJobs(job)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to observe job status")
            }
            if (!completed) {
                delay(5000) // Poll every 5 seconds for hardware jobs
            }
        }
    }

    private fun updateActiveJobs(job: IBMQuantumJob) {
        val currentJobs = _connectionState.value.activeJobs.toMutableList()
        val index = currentJobs.indexOfFirst { it.id == job.id }
        if (index >= 0) {
            if (job.status in listOf(JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.CANCELLED)) {
                currentJobs.removeAt(index)
            } else {
                currentJobs[index] = job
            }
        } else if (job.status !in listOf(JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.CANCELLED)) {
            currentJobs.add(job)
        }
        _connectionState.value = _connectionState.value.copy(activeJobs = currentJobs)
    }
}
