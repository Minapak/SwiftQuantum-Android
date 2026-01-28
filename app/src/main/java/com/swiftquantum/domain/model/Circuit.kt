package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Circuit(
    val id: String? = null,
    val name: String,
    val description: String = "",
    val numQubits: Int,
    val gates: List<Gate> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val userId: String? = null
) {
    val depth: Int
        get() = if (gates.isEmpty()) 0 else gates.maxOf { it.position } + 1

    val gateCount: Int
        get() = gates.size

    fun addGate(gate: Gate): Circuit {
        return copy(gates = gates + gate)
    }

    fun removeGate(index: Int): Circuit {
        return copy(gates = gates.toMutableList().apply { removeAt(index) })
    }

    fun clear(): Circuit {
        return copy(gates = emptyList())
    }

    fun validate(): CircuitValidationResult {
        val errors = mutableListOf<String>()

        if (numQubits <= 0) {
            errors.add("Number of qubits must be positive")
        }

        gates.forEach { gate ->
            val allQubits = gate.targetQubits + gate.controlQubits
            if (allQubits.any { it < 0 || it >= numQubits }) {
                errors.add("Gate ${gate.type.displayName} references invalid qubit indices")
            }
            if (allQubits.size != allQubits.toSet().size) {
                errors.add("Gate ${gate.type.displayName} has duplicate qubit references")
            }
        }

        return CircuitValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    companion object {
        fun empty(numQubits: Int = 2, name: String = "New Circuit") = Circuit(
            name = name,
            numQubits = numQubits
        )

        fun bellState() = Circuit(
            name = "Bell State",
            description = "Creates an entangled Bell state |00⟩ + |11⟩",
            numQubits = 2,
            gates = listOf(
                Gate(type = GateType.H, targetQubits = listOf(0), position = 0),
                Gate(type = GateType.CNOT, targetQubits = listOf(1), controlQubits = listOf(0), position = 1)
            )
        )

        fun ghzState(qubits: Int = 3) = Circuit(
            name = "GHZ State",
            description = "Creates a $qubits-qubit GHZ state",
            numQubits = qubits,
            gates = listOf(
                Gate(type = GateType.H, targetQubits = listOf(0), position = 0)
            ) + (1 until qubits).map { i ->
                Gate(type = GateType.CNOT, targetQubits = listOf(i), controlQubits = listOf(0), position = i)
            }
        )
    }
}

data class CircuitValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)
