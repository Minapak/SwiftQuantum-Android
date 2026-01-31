package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.Gate
import com.swiftquantum.domain.model.GateParameters
import com.swiftquantum.domain.model.GateType
import com.swiftquantum.domain.usecase.DeleteCircuitUseCase
import com.swiftquantum.domain.usecase.GetCircuitUseCase
import com.swiftquantum.domain.usecase.GetMaxQubitsUseCase
import com.swiftquantum.domain.usecase.GetMyCircuitsUseCase
import com.swiftquantum.domain.usecase.SaveCircuitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CircuitUiState(
    val circuit: Circuit = Circuit.empty(),
    val savedCircuits: List<Circuit> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val selectedGateType: GateType? = null,
    val selectedQubit: Int = 0,
    val showGatePalette: Boolean = false,
    val showSaveDialog: Boolean = false,
    val showLoadDialog: Boolean = false,
    val circuitName: String = "",
    val circuitDescription: String = "",
    val maxQubits: Int = 20,
    val error: String? = null
)

sealed class CircuitEvent {
    data object CircuitSaved : CircuitEvent()
    data object CircuitLoaded : CircuitEvent()
    data object CircuitDeleted : CircuitEvent()
    data class Error(val message: String) : CircuitEvent()
}

@HiltViewModel
class CircuitViewModel @Inject constructor(
    private val saveCircuitUseCase: SaveCircuitUseCase,
    private val getCircuitUseCase: GetCircuitUseCase,
    private val getMyCircuitsUseCase: GetMyCircuitsUseCase,
    private val deleteCircuitUseCase: DeleteCircuitUseCase,
    private val getMaxQubitsUseCase: GetMaxQubitsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CircuitUiState())
    val uiState: StateFlow<CircuitUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CircuitEvent>()
    val events: SharedFlow<CircuitEvent> = _events.asSharedFlow()

    init {
        loadMaxQubits()
        loadSavedCircuits()
    }

    private fun loadMaxQubits() {
        viewModelScope.launch {
            val maxQubits = getMaxQubitsUseCase()
            _uiState.value = _uiState.value.copy(maxQubits = maxQubits)
        }
    }

    fun loadSavedCircuits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getMyCircuitsUseCase()
                .onSuccess { circuits ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        savedCircuits = circuits
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun createNewCircuit(numQubits: Int = 2, name: String = "New Circuit") {
        _uiState.value = _uiState.value.copy(
            circuit = Circuit.empty(numQubits, name),
            circuitName = name,
            circuitDescription = ""
        )
    }

    fun setNumQubits(numQubits: Int) {
        val clampedQubits = numQubits.coerceIn(1, _uiState.value.maxQubits)
        val currentCircuit = _uiState.value.circuit
        _uiState.value = _uiState.value.copy(
            circuit = currentCircuit.copy(numQubits = clampedQubits)
        )
    }

    fun selectGateType(gateType: GateType?) {
        _uiState.value = _uiState.value.copy(selectedGateType = gateType)
    }

    fun selectQubit(qubit: Int) {
        _uiState.value = _uiState.value.copy(selectedQubit = qubit)
    }

    fun addGate(
        gateType: GateType,
        targetQubit: Int,
        controlQubit: Int? = null,
        parameters: GateParameters? = null
    ) {
        val currentCircuit = _uiState.value.circuit
        val position = currentCircuit.depth

        val controlQubits = when {
            gateType.qubitCount == 3 && controlQubit != null -> {
                // For 3-qubit gates, we need 2 control qubits
                val secondControl = (0 until currentCircuit.numQubits)
                    .filter { it != targetQubit && it != controlQubit }
                    .firstOrNull() ?: controlQubit
                listOf(controlQubit, secondControl)
            }
            controlQubit != null -> listOf(controlQubit)
            else -> emptyList()
        }

        val gate = Gate(
            type = gateType,
            targetQubits = listOf(targetQubit),
            controlQubits = controlQubits,
            parameters = parameters,
            position = position
        )

        val newCircuit = currentCircuit.addGate(gate)
        _uiState.value = _uiState.value.copy(
            circuit = newCircuit,
            selectedGateType = null
        )
    }

    fun removeGate(index: Int) {
        val currentCircuit = _uiState.value.circuit
        if (index in currentCircuit.gates.indices) {
            val newCircuit = currentCircuit.removeGate(index)
            _uiState.value = _uiState.value.copy(circuit = newCircuit)
        }
    }

    fun clearCircuit() {
        _uiState.value = _uiState.value.copy(
            circuit = _uiState.value.circuit.clear()
        )
    }

    fun setCircuitName(name: String) {
        _uiState.value = _uiState.value.copy(circuitName = name)
    }

    fun setCircuitDescription(description: String) {
        _uiState.value = _uiState.value.copy(circuitDescription = description)
    }

    fun showGatePalette(show: Boolean) {
        _uiState.value = _uiState.value.copy(showGatePalette = show)
    }

    fun showSaveDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showSaveDialog = show)
    }

    fun showLoadDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLoadDialog = show)
    }

    fun saveCircuit() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)

            val circuitToSave = _uiState.value.circuit.copy(
                name = _uiState.value.circuitName.ifBlank { "Untitled Circuit" },
                description = _uiState.value.circuitDescription
            )

            saveCircuitUseCase(circuitToSave)
                .onSuccess { savedCircuit ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        circuit = savedCircuit,
                        showSaveDialog = false
                    )
                    loadSavedCircuits()
                    _events.emit(CircuitEvent.CircuitSaved)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = error.message
                    )
                    _events.emit(CircuitEvent.Error(error.message ?: "Failed to save circuit"))
                }
        }
    }

    fun loadCircuit(circuitId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getCircuitUseCase(circuitId)
                .onSuccess { circuit ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        circuit = circuit,
                        circuitName = circuit.name,
                        circuitDescription = circuit.description,
                        showLoadDialog = false
                    )
                    _events.emit(CircuitEvent.CircuitLoaded)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(CircuitEvent.Error(error.message ?: "Failed to load circuit"))
                }
        }
    }

    fun loadCircuitFromList(circuit: Circuit) {
        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            circuitName = circuit.name,
            circuitDescription = circuit.description,
            showLoadDialog = false
        )
    }

    fun deleteCircuit(circuitId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            deleteCircuitUseCase(circuitId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadSavedCircuits()
                    _events.emit(CircuitEvent.CircuitDeleted)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(CircuitEvent.Error(error.message ?: "Failed to delete circuit"))
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun getCircuit(): Circuit = _uiState.value.circuit

    // MARK: - Circuit Presets (iOS Parity)

    /**
     * Load Bell State circuit: |00⟩ → (|00⟩ + |11⟩)/√2
     * H on qubit 0, CNOT with control 0 and target 1
     */
    fun loadBellState() {
        val circuit = Circuit.empty(2, "Bell State").let { c ->
            val h = Gate(
                type = GateType.H,
                targetQubits = listOf(0),
                controlQubits = emptyList(),
                parameters = null,
                position = 0
            )
            val cnot = Gate(
                type = GateType.CNOT,
                targetQubits = listOf(1),
                controlQubits = listOf(0),
                parameters = null,
                position = 1
            )
            c.addGate(h).addGate(cnot)
        }
        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            circuitName = "Bell State",
            circuitDescription = "Creates entangled Bell state: (|00⟩ + |11⟩)/√2"
        )
    }

    /**
     * Load GHZ State circuit: Creates maximally entangled state
     * H on qubit 0, CNOT cascade to all other qubits
     */
    fun loadGHZState() {
        val numQubits = minOf(3, _uiState.value.maxQubits)
        var circuit = Circuit.empty(numQubits, "GHZ State")

        // H gate on first qubit
        val h = Gate(
            type = GateType.H,
            targetQubits = listOf(0),
            controlQubits = emptyList(),
            parameters = null,
            position = 0
        )
        circuit = circuit.addGate(h)

        // CNOT gates from qubit 0 to all others
        for (i in 1 until numQubits) {
            val cnot = Gate(
                type = GateType.CNOT,
                targetQubits = listOf(i),
                controlQubits = listOf(0),
                parameters = null,
                position = i
            )
            circuit = circuit.addGate(cnot)
        }

        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            circuitName = "GHZ State",
            circuitDescription = "Creates GHZ (Greenberger-Horne-Zeilinger) entangled state"
        )
    }

    /**
     * Load QFT (Quantum Fourier Transform) circuit
     */
    fun loadQFT() {
        val numQubits = minOf(3, _uiState.value.maxQubits)
        var circuit = Circuit.empty(numQubits, "QFT")
        var position = 0

        for (i in 0 until numQubits) {
            // H gate
            val h = Gate(
                type = GateType.H,
                targetQubits = listOf(i),
                controlQubits = emptyList(),
                parameters = null,
                position = position++
            )
            circuit = circuit.addGate(h)

            // Controlled rotation gates
            for (j in (i + 1) until numQubits) {
                val angle = kotlin.math.PI / (1 shl (j - i))
                val crz = Gate(
                    type = GateType.CRZ,
                    targetQubits = listOf(j),
                    controlQubits = listOf(i),
                    parameters = GateParameters(theta = angle),
                    position = position++
                )
                circuit = circuit.addGate(crz)
            }
        }

        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            circuitName = "QFT",
            circuitDescription = "Quantum Fourier Transform circuit"
        )
    }

    /**
     * Load a random circuit with various gates
     */
    fun loadRandomCircuit() {
        val numQubits = minOf(3, _uiState.value.maxQubits)
        val depth = 4
        var circuit = Circuit.empty(numQubits, "Random Circuit")
        var position = 0

        val singleQubitGates = listOf(GateType.H, GateType.X, GateType.Y, GateType.Z, GateType.S, GateType.T)

        for (d in 0 until depth) {
            for (q in 0 until numQubits) {
                if (kotlin.random.Random.nextFloat() < 0.7) {
                    val gateType = singleQubitGates.random()
                    val gate = Gate(
                        type = gateType,
                        targetQubits = listOf(q),
                        controlQubits = emptyList(),
                        parameters = null,
                        position = position++
                    )
                    circuit = circuit.addGate(gate)
                }
            }
            // Add CNOT between adjacent qubits
            if (numQubits > 1 && kotlin.random.Random.nextFloat() < 0.5) {
                val control = kotlin.random.Random.nextInt(numQubits - 1)
                val cnot = Gate(
                    type = GateType.CNOT,
                    targetQubits = listOf(control + 1),
                    controlQubits = listOf(control),
                    parameters = null,
                    position = position++
                )
                circuit = circuit.addGate(cnot)
            }
        }

        _uiState.value = _uiState.value.copy(
            circuit = circuit,
            circuitName = "Random Circuit",
            circuitDescription = "Randomly generated quantum circuit"
        )
    }
}
