package com.swiftquantum.data.dto

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ComplexNumber
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.ExecutionMetadata
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.domain.model.ExecutionStatus
import com.swiftquantum.domain.model.Gate
import com.swiftquantum.domain.model.GateParameters
import com.swiftquantum.domain.model.GateType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CircuitDto(
    val id: String? = null,
    val name: String,
    val description: String = "",
    @SerialName("num_qubits")
    val numQubits: Int,
    val gates: List<GateDto> = emptyList(),
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("user_id")
    val userId: String? = null
) {
    fun toDomain(): Circuit = Circuit(
        id = id,
        name = name,
        description = description,
        numQubits = numQubits,
        gates = gates.map { it.toDomain() },
        createdAt = createdAt,
        updatedAt = updatedAt,
        userId = userId
    )

    companion object {
        fun fromDomain(circuit: Circuit): CircuitDto = CircuitDto(
            id = circuit.id,
            name = circuit.name,
            description = circuit.description,
            numQubits = circuit.numQubits,
            gates = circuit.gates.map { GateDto.fromDomain(it) },
            createdAt = circuit.createdAt,
            updatedAt = circuit.updatedAt,
            userId = circuit.userId
        )
    }
}

@Serializable
data class GateDto(
    val type: String,
    @SerialName("target_qubits")
    val targetQubits: List<Int>,
    @SerialName("control_qubits")
    val controlQubits: List<Int> = emptyList(),
    val parameters: GateParametersDto? = null,
    val position: Int = 0
) {
    fun toDomain(): Gate = Gate(
        type = GateType.fromString(type) ?: GateType.H,
        targetQubits = targetQubits,
        controlQubits = controlQubits,
        parameters = parameters?.toDomain(),
        position = position
    )

    companion object {
        fun fromDomain(gate: Gate): GateDto = GateDto(
            type = gate.type.name,
            targetQubits = gate.targetQubits,
            controlQubits = gate.controlQubits,
            parameters = gate.parameters?.let { GateParametersDto.fromDomain(it) },
            position = gate.position
        )
    }
}

@Serializable
data class GateParametersDto(
    val theta: Double? = null,
    val phi: Double? = null,
    val lambda: Double? = null
) {
    fun toDomain(): GateParameters = GateParameters(
        theta = theta,
        phi = phi,
        lambda = lambda
    )

    companion object {
        fun fromDomain(params: GateParameters): GateParametersDto = GateParametersDto(
            theta = params.theta,
            phi = params.phi,
            lambda = params.lambda
        )
    }
}

@Serializable
data class SimulationRequest(
    val circuit: CircuitDto,
    val shots: Int = 1024,
    val backend: String = "rust_simulator"
)

@Serializable
data class ExecutionResultDto(
    val id: String? = null,
    @SerialName("circuit_id")
    val circuitId: String? = null,
    val status: String = "pending",
    val backend: String = "rust_simulator",
    val counts: Map<String, Int> = emptyMap(),
    val probabilities: Map<String, Double> = emptyMap(),
    @SerialName("state_vector")
    val stateVector: List<ComplexNumberDto>? = null,
    val shots: Int = 1024,
    @SerialName("execution_time_ms")
    val executionTimeMs: Long = 0,
    val fidelity: Double? = null,
    val error: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    val metadata: ExecutionMetadataDto? = null
) {
    fun toDomain(): ExecutionResult = ExecutionResult(
        id = id,
        circuitId = circuitId,
        status = try { ExecutionStatus.valueOf(status.uppercase()) } catch (e: Exception) { ExecutionStatus.PENDING },
        backend = try { ExecutionBackend.valueOf(backend.uppercase()) } catch (e: Exception) { ExecutionBackend.RUST_SIMULATOR },
        counts = counts,
        probabilities = probabilities,
        stateVector = stateVector?.map { it.toDomain() },
        shots = shots,
        executionTimeMs = executionTimeMs,
        fidelity = fidelity,
        error = error,
        createdAt = createdAt,
        metadata = metadata?.toDomain()
    )
}

@Serializable
data class ComplexNumberDto(
    val real: Double,
    val imaginary: Double
) {
    fun toDomain(): ComplexNumber = ComplexNumber(real = real, imaginary = imaginary)
}

@Serializable
data class ExecutionMetadataDto(
    @SerialName("queue_position")
    val queuePosition: Int? = null,
    @SerialName("estimated_wait_time")
    val estimatedWaitTime: Long? = null,
    @SerialName("hardware_backend")
    val hardwareBackend: String? = null,
    @SerialName("calibration_data")
    val calibrationData: String? = null
) {
    fun toDomain(): ExecutionMetadata = ExecutionMetadata(
        queuePosition = queuePosition,
        estimatedWaitTime = estimatedWaitTime,
        hardwareBackend = hardwareBackend,
        calibrationData = calibrationData
    )
}

@Serializable
data class CreateCircuitRequest(
    val name: String,
    val description: String = "",
    @SerialName("num_qubits")
    val numQubits: Int,
    val gates: List<GateDto> = emptyList()
)

@Serializable
data class UpdateCircuitRequest(
    val name: String? = null,
    val description: String? = null,
    val gates: List<GateDto>? = null
)
