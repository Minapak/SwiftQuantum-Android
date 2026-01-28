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
}
