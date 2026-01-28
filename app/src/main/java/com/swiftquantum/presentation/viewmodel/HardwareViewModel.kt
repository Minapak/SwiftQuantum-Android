package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumConnection
import com.swiftquantum.domain.model.IBMQuantumJob
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.CancelJobUseCase
import com.swiftquantum.domain.usecase.ConnectToIBMQuantumUseCase
import com.swiftquantum.domain.usecase.DisconnectFromIBMQuantumUseCase
import com.swiftquantum.domain.usecase.GetActiveJobsUseCase
import com.swiftquantum.domain.usecase.GetAvailableBackendsUseCase
import com.swiftquantum.domain.usecase.GetJobResultUseCase
import com.swiftquantum.domain.usecase.ObserveIBMConnectionUseCase
import com.swiftquantum.domain.usecase.ObserveJobStatusUseCase
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase
import com.swiftquantum.domain.usecase.SubmitHardwareJobUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HardwareUiState(
    val connection: IBMQuantumConnection = IBMQuantumConnection(),
    val availableBackends: List<IBMQuantumBackend> = emptyList(),
    val activeJobs: List<IBMQuantumJob> = emptyList(),
    val selectedBackend: IBMQuantumBackend? = null,
    val currentResult: ExecutionResult? = null,
    val isConnecting: Boolean = false,
    val isSubmittingJob: Boolean = false,
    val apiToken: String = "",
    val userTier: UserTier = UserTier.FREE,
    val hasHardwareAccess: Boolean = false,
    val error: String? = null
)

sealed class HardwareEvent {
    data object Connected : HardwareEvent()
    data object Disconnected : HardwareEvent()
    data class JobSubmitted(val job: IBMQuantumJob) : HardwareEvent()
    data class JobCompleted(val result: ExecutionResult) : HardwareEvent()
    data class Error(val message: String) : HardwareEvent()
}

@HiltViewModel
class HardwareViewModel @Inject constructor(
    private val connectToIBMQuantumUseCase: ConnectToIBMQuantumUseCase,
    private val disconnectFromIBMQuantumUseCase: DisconnectFromIBMQuantumUseCase,
    private val getAvailableBackendsUseCase: GetAvailableBackendsUseCase,
    private val submitHardwareJobUseCase: SubmitHardwareJobUseCase,
    private val cancelJobUseCase: CancelJobUseCase,
    private val getJobResultUseCase: GetJobResultUseCase,
    private val getActiveJobsUseCase: GetActiveJobsUseCase,
    private val observeIBMConnectionUseCase: ObserveIBMConnectionUseCase,
    private val observeJobStatusUseCase: ObserveJobStatusUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HardwareUiState())
    val uiState: StateFlow<HardwareUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HardwareEvent>()
    val events: SharedFlow<HardwareEvent> = _events.asSharedFlow()

    init {
        observeConnection()
        observeUserTier()
    }

    private fun observeConnection() {
        viewModelScope.launch {
            observeIBMConnectionUseCase().collectLatest { connection ->
                _uiState.value = _uiState.value.copy(
                    connection = connection,
                    availableBackends = connection.availableBackends,
                    activeJobs = connection.activeJobs
                )
            }
        }
    }

    private fun observeUserTier() {
        viewModelScope.launch {
            observeUserTierUseCase().collectLatest { tier ->
                _uiState.value = _uiState.value.copy(
                    userTier = tier,
                    hasHardwareAccess = tier.hasHardwareAccess
                )
            }
        }
    }

    fun setApiToken(token: String) {
        _uiState.value = _uiState.value.copy(apiToken = token)
    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConnecting = true, error = null)

            connectToIBMQuantumUseCase(_uiState.value.apiToken)
                .onSuccess { connection ->
                    _uiState.value = _uiState.value.copy(
                        isConnecting = false,
                        connection = connection,
                        availableBackends = connection.availableBackends
                    )
                    _events.emit(HardwareEvent.Connected)
                    loadBackends()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isConnecting = false,
                        error = error.message
                    )
                    _events.emit(HardwareEvent.Error(error.message ?: "Connection failed"))
                }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            disconnectFromIBMQuantumUseCase()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        connection = IBMQuantumConnection(),
                        availableBackends = emptyList(),
                        activeJobs = emptyList(),
                        selectedBackend = null,
                        apiToken = ""
                    )
                    _events.emit(HardwareEvent.Disconnected)
                }
                .onFailure { error ->
                    _events.emit(HardwareEvent.Error(error.message ?: "Disconnect failed"))
                }
        }
    }

    fun loadBackends() {
        viewModelScope.launch {
            getAvailableBackendsUseCase()
                .onSuccess { backends ->
                    _uiState.value = _uiState.value.copy(availableBackends = backends)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun selectBackend(backend: IBMQuantumBackend) {
        _uiState.value = _uiState.value.copy(selectedBackend = backend)
    }

    fun submitJob(circuit: Circuit, shots: Int = 1024) {
        viewModelScope.launch {
            val backend = _uiState.value.selectedBackend
            if (backend == null) {
                _events.emit(HardwareEvent.Error("Please select a backend"))
                return@launch
            }

            _uiState.value = _uiState.value.copy(isSubmittingJob = true, error = null)

            submitHardwareJobUseCase(circuit, backend.name, shots)
                .onSuccess { job ->
                    _uiState.value = _uiState.value.copy(isSubmittingJob = false)
                    _events.emit(HardwareEvent.JobSubmitted(job))
                    observeJob(job.id)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSubmittingJob = false,
                        error = error.message
                    )
                    _events.emit(HardwareEvent.Error(error.message ?: "Job submission failed"))
                }
        }
    }

    private fun observeJob(jobId: String) {
        viewModelScope.launch {
            observeJobStatusUseCase(jobId).collectLatest { job ->
                val currentJobs = _uiState.value.activeJobs.toMutableList()
                val index = currentJobs.indexOfFirst { it.id == job.id }
                if (index >= 0) {
                    currentJobs[index] = job
                } else {
                    currentJobs.add(job)
                }
                _uiState.value = _uiState.value.copy(activeJobs = currentJobs)

                if (job.result != null) {
                    _uiState.value = _uiState.value.copy(currentResult = job.result)
                    _events.emit(HardwareEvent.JobCompleted(job.result))
                }
            }
        }
    }

    fun cancelJob(jobId: String) {
        viewModelScope.launch {
            cancelJobUseCase(jobId)
                .onSuccess {
                    val currentJobs = _uiState.value.activeJobs.filter { it.id != jobId }
                    _uiState.value = _uiState.value.copy(activeJobs = currentJobs)
                }
                .onFailure { error ->
                    _events.emit(HardwareEvent.Error(error.message ?: "Failed to cancel job"))
                }
        }
    }

    fun getJobResult(jobId: String) {
        viewModelScope.launch {
            getJobResultUseCase(jobId)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(currentResult = result)
                }
                .onFailure { error ->
                    _events.emit(HardwareEvent.Error(error.message ?: "Failed to get result"))
                }
        }
    }

    fun loadActiveJobs() {
        viewModelScope.launch {
            getActiveJobsUseCase()
                .onSuccess { jobs ->
                    _uiState.value = _uiState.value.copy(activeJobs = jobs)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(currentResult = null)
    }
}
