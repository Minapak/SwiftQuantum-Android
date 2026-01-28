package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExecutionResult(
    val id: String? = null,
    val circuitId: String? = null,
    val status: ExecutionStatus = ExecutionStatus.PENDING,
    val backend: ExecutionBackend = ExecutionBackend.RUST_SIMULATOR,
    val counts: Map<String, Int> = emptyMap(),
    val probabilities: Map<String, Double> = emptyMap(),
    val stateVector: List<ComplexNumber>? = null,
    val shots: Int = 1024,
    val executionTimeMs: Long = 0,
    val fidelity: Double? = null,
    val error: String? = null,
    val createdAt: String? = null,
    val metadata: ExecutionMetadata? = null
) {
    val isSuccess: Boolean
        get() = status == ExecutionStatus.COMPLETED && error == null

    val totalCounts: Int
        get() = counts.values.sum()

    fun getProbability(state: String): Double {
        return probabilities[state] ?: (counts[state]?.toDouble()?.div(totalCounts) ?: 0.0)
    }

    fun getTopResults(limit: Int = 10): List<Pair<String, Double>> {
        return probabilities.entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }
    }
}

@Serializable
enum class ExecutionStatus {
    PENDING,
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}

@Serializable
enum class ExecutionBackend(
    val displayName: String,
    val description: String,
    val isHardware: Boolean = false
) {
    RUST_SIMULATOR("Rust Simulator", "High-performance local simulation (900x faster)", false),
    IBM_QASM_SIMULATOR("IBM QASM Simulator", "IBM Quantum cloud simulator", false),
    IBM_BRISBANE("IBM Brisbane", "127-qubit IBM Quantum processor", true),
    IBM_OSAKA("IBM Osaka", "127-qubit IBM Quantum processor", true),
    IBM_KYOTO("IBM Kyoto", "127-qubit IBM Quantum processor", true),
    IBM_SHERBROOKE("IBM Sherbrooke", "127-qubit IBM Quantum processor", true);

    companion object {
        val simulators = entries.filter { !it.isHardware }
        val hardware = entries.filter { it.isHardware }
    }
}

@Serializable
data class ComplexNumber(
    val real: Double,
    val imaginary: Double
) {
    val magnitude: Double
        get() = kotlin.math.sqrt(real * real + imaginary * imaginary)

    val phase: Double
        get() = kotlin.math.atan2(imaginary, real)

    val probability: Double
        get() = magnitude * magnitude

    override fun toString(): String {
        return when {
            imaginary == 0.0 -> String.format("%.4f", real)
            real == 0.0 -> String.format("%.4fi", imaginary)
            imaginary > 0 -> String.format("%.4f + %.4fi", real, imaginary)
            else -> String.format("%.4f - %.4fi", real, -imaginary)
        }
    }
}

@Serializable
data class ExecutionMetadata(
    val queuePosition: Int? = null,
    val estimatedWaitTime: Long? = null,
    val hardwareBackend: String? = null,
    val calibrationData: String? = null
)

@Serializable
data class BlochSphereState(
    val theta: Double,
    val phi: Double
) {
    val x: Double
        get() = kotlin.math.sin(theta) * kotlin.math.cos(phi)

    val y: Double
        get() = kotlin.math.sin(theta) * kotlin.math.sin(phi)

    val z: Double
        get() = kotlin.math.cos(theta)

    companion object {
        val ZERO = BlochSphereState(0.0, 0.0)
        val ONE = BlochSphereState(kotlin.math.PI, 0.0)
        val PLUS = BlochSphereState(kotlin.math.PI / 2, 0.0)
        val MINUS = BlochSphereState(kotlin.math.PI / 2, kotlin.math.PI)
        val PLUS_I = BlochSphereState(kotlin.math.PI / 2, kotlin.math.PI / 2)
        val MINUS_I = BlochSphereState(kotlin.math.PI / 2, -kotlin.math.PI / 2)

        fun fromStateVector(alpha: ComplexNumber, beta: ComplexNumber): BlochSphereState {
            val theta = 2 * kotlin.math.acos(alpha.magnitude.coerceIn(0.0, 1.0))
            val phi = beta.phase - alpha.phase
            return BlochSphereState(theta, phi)
        }
    }
}
