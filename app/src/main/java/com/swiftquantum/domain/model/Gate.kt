package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Gate(
    val type: GateType,
    val targetQubits: List<Int>,
    val controlQubits: List<Int> = emptyList(),
    val parameters: GateParameters? = null,
    val position: Int = 0
)

@Serializable
enum class GateType(
    val displayName: String,
    val symbol: String,
    val description: String,
    val category: GateCategory,
    val qubitCount: Int = 1,
    val hasParameters: Boolean = false
) {
    // Single Qubit Gates
    H("Hadamard", "H", "Creates superposition state", GateCategory.SINGLE_QUBIT),
    X("Pauli-X", "X", "Bit flip gate (NOT)", GateCategory.SINGLE_QUBIT),
    Y("Pauli-Y", "Y", "Bit and phase flip gate", GateCategory.SINGLE_QUBIT),
    Z("Pauli-Z", "Z", "Phase flip gate", GateCategory.SINGLE_QUBIT),
    S("S Gate", "S", "Phase gate (sqrt of Z)", GateCategory.SINGLE_QUBIT),
    T("T Gate", "T", "Pi/8 gate (sqrt of S)", GateCategory.SINGLE_QUBIT),

    // Rotation Gates
    RX("RX Rotation", "RX", "Rotation around X-axis", GateCategory.ROTATION, hasParameters = true),
    RY("RY Rotation", "RY", "Rotation around Y-axis", GateCategory.ROTATION, hasParameters = true),
    RZ("RZ Rotation", "RZ", "Rotation around Z-axis", GateCategory.ROTATION, hasParameters = true),

    // Universal Gates
    U1("U1 Gate", "U1", "Single parameter universal gate", GateCategory.ROTATION, hasParameters = true),
    U2("U2 Gate", "U2", "Two parameter universal gate", GateCategory.ROTATION, hasParameters = true),
    U3("U3 Gate", "U3", "Three parameter universal gate", GateCategory.ROTATION, hasParameters = true),

    // Two Qubit Gates
    CNOT("CNOT", "CX", "Controlled NOT gate", GateCategory.MULTI_QUBIT, qubitCount = 2),
    CZ("CZ Gate", "CZ", "Controlled Z gate", GateCategory.MULTI_QUBIT, qubitCount = 2),
    CY("CY Gate", "CY", "Controlled Y gate", GateCategory.MULTI_QUBIT, qubitCount = 2),
    SWAP("SWAP", "SW", "Swaps two qubit states", GateCategory.MULTI_QUBIT, qubitCount = 2),
    ISWAP("iSWAP", "iSW", "Imaginary SWAP gate", GateCategory.MULTI_QUBIT, qubitCount = 2),

    // Controlled Rotation Gates
    CRX("CRX Gate", "CRX", "Controlled RX rotation", GateCategory.CONTROLLED, qubitCount = 2, hasParameters = true),
    CRY("CRY Gate", "CRY", "Controlled RY rotation", GateCategory.CONTROLLED, qubitCount = 2, hasParameters = true),
    CRZ("CRZ Gate", "CRZ", "Controlled RZ rotation", GateCategory.CONTROLLED, qubitCount = 2, hasParameters = true),

    // Three Qubit Gates
    TOFFOLI("Toffoli", "CCX", "Controlled-controlled NOT", GateCategory.MULTI_QUBIT, qubitCount = 3),
    FREDKIN("Fredkin", "CSW", "Controlled SWAP", GateCategory.MULTI_QUBIT, qubitCount = 3),
    CCZ("CCZ Gate", "CCZ", "Controlled-controlled Z", GateCategory.MULTI_QUBIT, qubitCount = 3);

    companion object {
        val singleQubitGates = entries.filter { it.category == GateCategory.SINGLE_QUBIT }
        val rotationGates = entries.filter { it.category == GateCategory.ROTATION }
        val multiQubitGates = entries.filter { it.category == GateCategory.MULTI_QUBIT }
        val controlledGates = entries.filter { it.category == GateCategory.CONTROLLED }

        fun fromString(name: String): GateType? = entries.find {
            it.name.equals(name, ignoreCase = true) || it.symbol.equals(name, ignoreCase = true)
        }
    }
}

@Serializable
enum class GateCategory {
    SINGLE_QUBIT,
    ROTATION,
    MULTI_QUBIT,
    CONTROLLED
}

@Serializable
data class GateParameters(
    val theta: Double? = null,
    val phi: Double? = null,
    val lambda: Double? = null
) {
    companion object {
        fun forRotation(angle: Double) = GateParameters(theta = angle)
        fun forU1(lambda: Double) = GateParameters(lambda = lambda)
        fun forU2(phi: Double, lambda: Double) = GateParameters(phi = phi, lambda = lambda)
        fun forU3(theta: Double, phi: Double, lambda: Double) = GateParameters(
            theta = theta, phi = phi, lambda = lambda
        )
    }
}
