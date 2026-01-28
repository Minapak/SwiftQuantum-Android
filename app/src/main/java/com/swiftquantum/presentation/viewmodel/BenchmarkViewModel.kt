package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.BenchmarkResult
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.EngineStatus
import com.swiftquantum.domain.model.HybridEngineType
import com.swiftquantum.domain.model.HybridExecutionResult
import com.swiftquantum.domain.model.OptimizationLevel
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.ExecuteWithHybridEngineUseCase
import com.swiftquantum.domain.usecase.GetBenchmarkHistoryUseCase
import com.swiftquantum.domain.usecase.GetEngineStatusUseCase
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase
import com.swiftquantum.domain.usecase.RunBenchmarkUseCase
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

data class BenchmarkUiState(
    val circuit: Circuit = Circuit.empty(),
    val selectedEngine: HybridEngineType = HybridEngineType.RUST,
    val selectedOptimization: OptimizationLevel = OptimizationLevel.BASIC,
    val shots: Int = 1024,
    val isExecuting: Boolean = false,
    val isBenchmarking: Boolean = false,
    val executionResult: HybridExecutionResult? = null,
    val benchmarkResult: BenchmarkResult? = null,
    val benchmarkHistory: List<BenchmarkResult> = emptyList(),
    val engineStatuses: List<EngineStatus> = emptyList(),
    val isLoadingStatus: Boolean = false,
    val userTier: UserTier = UserTier.FREE,
    val error: String? = null
)

sealed class BenchmarkEvent {
    data class ExecutionComplete(val result: HybridExecutionResult) : BenchmarkEvent()
    data class BenchmarkComplete(val result: BenchmarkResult) : BenchmarkEvent()
    data class Error(val message: String) : BenchmarkEvent()
    data object TierRequired : BenchmarkEvent()
}

@HiltViewModel
class BenchmarkViewModel @Inject constructor(
    private val executeWithHybridEngineUseCase: ExecuteWithHybridEngineUseCase,
    private val runBenchmarkUseCase: RunBenchmarkUseCase,
    private val getBenchmarkHistoryUseCase: GetBenchmarkHistoryUseCase,
    private val getEngineStatusUseCase: GetEngineStatusUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BenchmarkUiState())
    val uiState: StateFlow<BenchmarkUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BenchmarkEvent>()
    val events: SharedFlow<BenchmarkEvent> = _events.asSharedFlow()

    init {
        observeUserTier()
        loadEngineStatus()
        loadBenchmarkHistory()
    }

    private fun observeUserTier() {
        viewModelScope.launch {
            observeUserTierUseCase().collectLatest { tier ->
                _uiState.value = _uiState.value.copy(userTier = tier)
            }
        }
    }

    fun loadEngineStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingStatus = true)

            getEngineStatusUseCase().onSuccess { statuses ->
                _uiState.value = _uiState.value.copy(
                    isLoadingStatus = false,
                    engineStatuses = statuses
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoadingStatus = false,
                    error = error.message
                )
            }
        }
    }

    fun loadBenchmarkHistory() {
        viewModelScope.launch {
            getBenchmarkHistoryUseCase().onSuccess { history ->
                _uiState.value = _uiState.value.copy(benchmarkHistory = history)
            }
        }
    }

    fun setCircuit(circuit: Circuit) {
        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            executionResult = null,
            benchmarkResult = null,
            error = null
        )
    }

    fun setEngine(engine: HybridEngineType) {
        _uiState.value = _uiState.value.copy(
            selectedEngine = engine,
            executionResult = null
        )
    }

    fun setOptimizationLevel(level: OptimizationLevel) {
        _uiState.value = _uiState.value.copy(
            selectedOptimization = level,
            executionResult = null
        )
    }

    fun setShots(shots: Int) {
        _uiState.value = _uiState.value.copy(
            shots = shots.coerceIn(1, 100000),
            executionResult = null
        )
    }

    fun executeWithEngine() {
        viewModelScope.launch {
            val state = _uiState.value

            // Check tier for C++ HPC
            if (state.selectedEngine == HybridEngineType.CPP_HPC &&
                state.userTier != UserTier.MASTER) {
                _events.emit(BenchmarkEvent.TierRequired)
                _uiState.value = state.copy(error = "C++ HPC Engine requires MASTER tier")
                return@launch
            }

            _uiState.value = state.copy(isExecuting = true, error = null)

            executeWithHybridEngineUseCase(
                circuit = state.circuit,
                engineType = state.selectedEngine,
                shots = state.shots,
                optimizationLevel = state.selectedOptimization
            ).onSuccess { result ->
                _uiState.value = _uiState.value.copy(
                    isExecuting = false,
                    executionResult = result
                )
                _events.emit(BenchmarkEvent.ExecutionComplete(result))
            }.onFailure { error ->
                val errorMessage = when {
                    error is IllegalAccessException -> error.message ?: "Access denied"
                    error.message?.contains("tier") == true -> {
                        viewModelScope.launch { _events.emit(BenchmarkEvent.TierRequired) }
                        error.message
                    }
                    else -> error.message
                }
                _uiState.value = _uiState.value.copy(
                    isExecuting = false,
                    error = errorMessage
                )
                _events.emit(BenchmarkEvent.Error(errorMessage ?: "Execution failed"))
            }
        }
    }

    fun runBenchmark() {
        viewModelScope.launch {
            val state = _uiState.value

            // Check tier for benchmarks
            if (state.userTier == UserTier.FREE) {
                _events.emit(BenchmarkEvent.TierRequired)
                _uiState.value = state.copy(error = "Benchmarks require PRO or MASTER tier")
                return@launch
            }

            _uiState.value = state.copy(isBenchmarking = true, error = null)

            runBenchmarkUseCase(
                circuit = state.circuit,
                shots = state.shots
            ).onSuccess { result ->
                _uiState.value = _uiState.value.copy(
                    isBenchmarking = false,
                    benchmarkResult = result,
                    benchmarkHistory = listOf(result) + _uiState.value.benchmarkHistory
                )
                _events.emit(BenchmarkEvent.BenchmarkComplete(result))
            }.onFailure { error ->
                val errorMessage = when {
                    error is IllegalAccessException -> {
                        viewModelScope.launch { _events.emit(BenchmarkEvent.TierRequired) }
                        error.message
                    }
                    else -> error.message
                }
                _uiState.value = _uiState.value.copy(
                    isBenchmarking = false,
                    error = errorMessage
                )
                _events.emit(BenchmarkEvent.Error(errorMessage ?: "Benchmark failed"))
            }
        }
    }

    fun loadPresetCircuit(preset: CircuitPreset) {
        val circuit = when (preset) {
            CircuitPreset.BELL_STATE -> Circuit.bellState()
            CircuitPreset.GHZ_STATE -> Circuit.ghzState(3)
            CircuitPreset.SUPERPOSITION -> createSuperpositionCircuit(4)
            CircuitPreset.ENTANGLEMENT -> createEntanglementCircuit(4)
        }
        setCircuit(circuit)
    }

    private fun createSuperpositionCircuit(numQubits: Int): Circuit {
        val gates = (0 until numQubits).map { qubit ->
            com.swiftquantum.domain.model.Gate(
                type = com.swiftquantum.domain.model.GateType.H,
                targetQubits = listOf(qubit),
                position = 0
            )
        }
        return Circuit(
            name = "Superposition",
            description = "Creates equal superposition of all states",
            numQubits = numQubits,
            gates = gates
        )
    }

    private fun createEntanglementCircuit(numQubits: Int): Circuit {
        val gates = mutableListOf<com.swiftquantum.domain.model.Gate>()
        gates.add(
            com.swiftquantum.domain.model.Gate(
                type = com.swiftquantum.domain.model.GateType.H,
                targetQubits = listOf(0),
                position = 0
            )
        )
        for (i in 1 until numQubits) {
            gates.add(
                com.swiftquantum.domain.model.Gate(
                    type = com.swiftquantum.domain.model.GateType.CNOT,
                    targetQubits = listOf(i),
                    controlQubits = listOf(i - 1),
                    position = i
                )
            )
        }
        return Circuit(
            name = "Entanglement Chain",
            description = "Creates a chain of entangled qubits",
            numQubits = numQubits,
            gates = gates
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearResults() {
        _uiState.value = _uiState.value.copy(
            executionResult = null,
            benchmarkResult = null
        )
    }
}
