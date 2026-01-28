package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.BlochSphereState
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.Gate
import com.swiftquantum.domain.model.GateType
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.GetMaxQubitsUseCase
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase
import com.swiftquantum.domain.usecase.RunSimulationUseCase
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

data class SimulatorUiState(
    val circuit: Circuit = Circuit.empty(),
    val numQubits: Int = 2,
    val shots: Int = 1024,
    val selectedBackend: ExecutionBackend = ExecutionBackend.RUST_SIMULATOR,
    val isRunning: Boolean = false,
    val result: ExecutionResult? = null,
    val lastResult: ExecutionResult? = null,
    val blochStates: List<BlochSphereState> = emptyList(),
    val error: String? = null,
    val userTier: UserTier = UserTier.FREE,
    val maxQubits: Int = 20,
    val selectedQubit: Int = 0
)

sealed class SimulatorEvent {
    data object SimulationStarted : SimulatorEvent()
    data class SimulationCompleted(val result: ExecutionResult) : SimulatorEvent()
    data class Error(val message: String) : SimulatorEvent()
}

@HiltViewModel
class SimulatorViewModel @Inject constructor(
    private val runSimulationUseCase: RunSimulationUseCase,
    private val getMaxQubitsUseCase: GetMaxQubitsUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimulatorUiState())
    val uiState: StateFlow<SimulatorUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SimulatorEvent>()
    val events: SharedFlow<SimulatorEvent> = _events.asSharedFlow()

    init {
        observeUserTier()
        loadMaxQubits()
    }

    private fun observeUserTier() {
        viewModelScope.launch {
            observeUserTierUseCase().collectLatest { tier ->
                _uiState.value = _uiState.value.copy(
                    userTier = tier,
                    maxQubits = tier.maxQubits
                )
            }
        }
    }

    private fun loadMaxQubits() {
        viewModelScope.launch {
            val maxQubits = getMaxQubitsUseCase()
            _uiState.value = _uiState.value.copy(maxQubits = maxQubits)
        }
    }

    fun setNumQubits(qubits: Int) {
        val clampedQubits = qubits.coerceIn(1, _uiState.value.maxQubits)
        val newCircuit = _uiState.value.circuit.copy(numQubits = clampedQubits)
        _uiState.value = _uiState.value.copy(
            numQubits = clampedQubits,
            circuit = newCircuit,
            result = null
        )
    }

    fun setShots(shots: Int) {
        val clampedShots = shots.coerceIn(1, 100000)
        _uiState.value = _uiState.value.copy(shots = clampedShots)
    }

    fun setBackend(backend: ExecutionBackend) {
        _uiState.value = _uiState.value.copy(selectedBackend = backend)
    }

    fun selectQubit(qubit: Int) {
        _uiState.value = _uiState.value.copy(selectedQubit = qubit)
    }

    fun addGate(gateType: GateType, targetQubit: Int, controlQubit: Int? = null) {
        val currentCircuit = _uiState.value.circuit
        val position = currentCircuit.depth

        val gate = Gate(
            type = gateType,
            targetQubits = listOf(targetQubit),
            controlQubits = controlQubit?.let { listOf(it) } ?: emptyList(),
            position = position
        )

        val newCircuit = currentCircuit.addGate(gate)
        _uiState.value = _uiState.value.copy(circuit = newCircuit, result = null)
    }

    fun removeLastGate() {
        val currentCircuit = _uiState.value.circuit
        if (currentCircuit.gates.isNotEmpty()) {
            val newCircuit = currentCircuit.removeGate(currentCircuit.gates.lastIndex)
            _uiState.value = _uiState.value.copy(circuit = newCircuit, result = null)
        }
    }

    fun clearCircuit() {
        _uiState.value = _uiState.value.copy(
            circuit = Circuit.empty(_uiState.value.numQubits),
            result = null
        )
    }

    fun loadPresetCircuit(preset: CircuitPreset) {
        val circuit = when (preset) {
            CircuitPreset.BELL_STATE -> Circuit.bellState()
            CircuitPreset.GHZ_STATE -> Circuit.ghzState(_uiState.value.numQubits.coerceIn(3, _uiState.value.maxQubits))
            CircuitPreset.SUPERPOSITION -> createSuperpositionCircuit()
            CircuitPreset.ENTANGLEMENT -> createEntanglementCircuit()
        }
        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            numQubits = circuit.numQubits,
            result = null
        )
    }

    private fun createSuperpositionCircuit(): Circuit {
        val numQubits = _uiState.value.numQubits
        val gates = (0 until numQubits).map { qubit ->
            Gate(type = GateType.H, targetQubits = listOf(qubit), position = 0)
        }
        return Circuit(
            name = "Superposition",
            description = "Creates equal superposition of all states",
            numQubits = numQubits,
            gates = gates
        )
    }

    private fun createEntanglementCircuit(): Circuit {
        val numQubits = _uiState.value.numQubits.coerceAtLeast(2)
        val gates = mutableListOf<Gate>()
        gates.add(Gate(type = GateType.H, targetQubits = listOf(0), position = 0))
        for (i in 1 until numQubits) {
            gates.add(
                Gate(
                    type = GateType.CNOT,
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

    fun runSimulation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRunning = true, error = null)
            _events.emit(SimulatorEvent.SimulationStarted)

            runSimulationUseCase(
                circuit = _uiState.value.circuit,
                shots = _uiState.value.shots,
                backend = _uiState.value.selectedBackend
            ).onSuccess { result ->
                val blochStates = calculateBlochStates(result)
                _uiState.value = _uiState.value.copy(
                    isRunning = false,
                    result = result,
                    lastResult = result,
                    blochStates = blochStates
                )
                _events.emit(SimulatorEvent.SimulationCompleted(result))
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isRunning = false,
                    error = error.message
                )
                _events.emit(SimulatorEvent.Error(error.message ?: "Simulation failed"))
            }
        }
    }

    private fun calculateBlochStates(result: ExecutionResult): List<BlochSphereState> {
        return result.stateVector?.let { stateVector ->
            if (stateVector.size >= 2) {
                listOf(BlochSphereState.fromStateVector(stateVector[0], stateVector[1]))
            } else {
                listOf(BlochSphereState.ZERO)
            }
        } ?: listOf(BlochSphereState.ZERO)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun updateCircuit(circuit: Circuit) {
        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            numQubits = circuit.numQubits,
            result = null
        )
    }
}

enum class CircuitPreset {
    BELL_STATE,
    GHZ_STATE,
    SUPERPOSITION,
    ENTANGLEMENT
}
