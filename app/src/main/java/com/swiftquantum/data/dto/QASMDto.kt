package com.swiftquantum.data.dto

import com.swiftquantum.domain.model.DifficultyLevel
import com.swiftquantum.domain.model.ErrorSeverity
import com.swiftquantum.domain.model.ExportOptions
import com.swiftquantum.domain.model.ImportResult
import com.swiftquantum.domain.model.QASMCircuit
import com.swiftquantum.domain.model.QASMClassicalRegister
import com.swiftquantum.domain.model.QASMGateInstruction
import com.swiftquantum.domain.model.QASMQubitRegister
import com.swiftquantum.domain.model.QASMSyntaxError
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMValidationResult
import com.swiftquantum.domain.model.QASMVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request DTO for importing QASM code
 */
@Serializable
data class ImportQASMRequestDto(
    val code: String,

    val version: String? = null, // null = auto-detect

    @SerialName("validate_only")
    val validateOnly: Boolean = false
)

/**
 * Response DTO for import result
 */
@Serializable
data class ImportResultDto(
    val success: Boolean,

    val circuit: CircuitDto? = null,

    @SerialName("qasm_circuit")
    val qasmCircuit: QASMCircuitDto? = null,

    val warnings: List<String> = emptyList(),

    val errors: List<String> = emptyList(),

    @SerialName("detected_version")
    val detectedVersion: String? = null,

    @SerialName("line_count")
    val lineCount: Int = 0,

    @SerialName("gate_count")
    val gateCount: Int = 0
) {
    fun toDomain(): ImportResult = ImportResult(
        success = success,
        circuit = circuit?.toDomain(),
        qasmCircuit = qasmCircuit?.toDomain(),
        warnings = warnings,
        errors = errors,
        detectedVersion = detectedVersion?.let { QASMVersion.fromString(it) },
        lineCount = lineCount,
        gateCount = gateCount
    )
}

/**
 * QASM Circuit DTO
 */
@Serializable
data class QASMCircuitDto(
    val version: String = "qasm2",

    val includes: List<String> = listOf("qelib1.inc"),

    @SerialName("qubit_registers")
    val qubitRegisters: List<QASMQubitRegisterDto> = emptyList(),

    @SerialName("classical_registers")
    val classicalRegisters: List<QASMClassicalRegisterDto> = emptyList(),

    val gates: List<QASMGateInstructionDto> = emptyList(),

    val measurements: List<String> = emptyList()
) {
    fun toDomain(): QASMCircuit = QASMCircuit(
        version = QASMVersion.fromString(version) ?: QASMVersion.QASM2,
        includes = includes,
        qubitRegisters = qubitRegisters.map { it.toDomain() },
        classicalRegisters = classicalRegisters.map { it.toDomain() },
        gates = gates.map { it.toDomain() },
        measurements = measurements
    )

    companion object {
        fun fromDomain(circuit: QASMCircuit): QASMCircuitDto = QASMCircuitDto(
            version = circuit.version.name.lowercase(),
            includes = circuit.includes,
            qubitRegisters = circuit.qubitRegisters.map { QASMQubitRegisterDto.fromDomain(it) },
            classicalRegisters = circuit.classicalRegisters.map { QASMClassicalRegisterDto.fromDomain(it) },
            gates = circuit.gates.map { QASMGateInstructionDto.fromDomain(it) },
            measurements = circuit.measurements
        )
    }
}

@Serializable
data class QASMQubitRegisterDto(
    val name: String,
    val size: Int
) {
    fun toDomain() = QASMQubitRegister(name, size)

    companion object {
        fun fromDomain(reg: QASMQubitRegister) = QASMQubitRegisterDto(reg.name, reg.size)
    }
}

@Serializable
data class QASMClassicalRegisterDto(
    val name: String,
    val size: Int
) {
    fun toDomain() = QASMClassicalRegister(name, size)

    companion object {
        fun fromDomain(reg: QASMClassicalRegister) = QASMClassicalRegisterDto(reg.name, reg.size)
    }
}

@Serializable
data class QASMGateInstructionDto(
    @SerialName("gate_name")
    val gateName: String,

    val parameters: List<Double> = emptyList(),

    @SerialName("qubit_args")
    val qubitArgs: List<String>
) {
    fun toDomain() = QASMGateInstruction(gateName, parameters, qubitArgs)

    companion object {
        fun fromDomain(instruction: QASMGateInstruction) = QASMGateInstructionDto(
            gateName = instruction.gateName,
            parameters = instruction.parameters,
            qubitArgs = instruction.qubitArgs
        )
    }
}

/**
 * Request DTO for exporting circuit to QASM
 */
@Serializable
data class ExportQASMRequestDto(
    val circuit: CircuitDto,

    val options: ExportOptionsDto = ExportOptionsDto()
)

/**
 * Export options DTO
 */
@Serializable
data class ExportOptionsDto(
    val version: String = "qasm2",

    @SerialName("include_comments")
    val includeComments: Boolean = true,

    @SerialName("pretty_print")
    val prettyPrint: Boolean = true,

    @SerialName("include_measurements")
    val includeMeasurements: Boolean = true,

    @SerialName("optimize_gates")
    val optimizeGates: Boolean = false
) {
    fun toDomain() = ExportOptions(
        version = QASMVersion.fromString(version) ?: QASMVersion.QASM2,
        includeComments = includeComments,
        prettyPrint = prettyPrint,
        includeMeasurements = includeMeasurements,
        optimizeGates = optimizeGates
    )

    companion object {
        fun fromDomain(options: ExportOptions) = ExportOptionsDto(
            version = options.version.name.lowercase(),
            includeComments = options.includeComments,
            prettyPrint = options.prettyPrint,
            includeMeasurements = options.includeMeasurements,
            optimizeGates = options.optimizeGates
        )
    }
}

/**
 * Response DTO for export result
 */
@Serializable
data class ExportQASMResponseDto(
    val code: String,

    val version: String,

    @SerialName("line_count")
    val lineCount: Int,

    @SerialName("gate_count")
    val gateCount: Int,

    @SerialName("qasm_circuit")
    val qasmCircuit: QASMCircuitDto? = null
)

/**
 * QASM Template DTO
 */
@Serializable
data class QASMTemplateDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val code: String,
    @SerialName("num_qubits")
    val numQubits: Int,
    @SerialName("difficulty_level")
    val difficultyLevel: String = "beginner"
) {
    fun toDomain(): QASMTemplate = QASMTemplate(
        id = id,
        name = name,
        description = description,
        category = try {
            QASMTemplateCategory.valueOf(category.uppercase())
        } catch (e: Exception) {
            QASMTemplateCategory.BASICS
        },
        code = code,
        numQubits = numQubits,
        difficultyLevel = try {
            DifficultyLevel.valueOf(difficultyLevel.uppercase())
        } catch (e: Exception) {
            DifficultyLevel.BEGINNER
        }
    )
}

/**
 * QASM Validation Result DTO
 */
@Serializable
data class QASMValidationResultDto(
    @SerialName("is_valid")
    val isValid: Boolean,

    val errors: List<QASMSyntaxErrorDto> = emptyList(),

    val warnings: List<QASMSyntaxErrorDto> = emptyList()
) {
    fun toDomain() = QASMValidationResult(
        isValid = isValid,
        errors = errors.map { it.toDomain() },
        warnings = warnings.map { it.toDomain() }
    )
}

@Serializable
data class QASMSyntaxErrorDto(
    val line: Int,
    val column: Int,
    val message: String,
    val severity: String = "error"
) {
    fun toDomain() = QASMSyntaxError(
        line = line,
        column = column,
        message = message,
        severity = try {
            ErrorSeverity.valueOf(severity.uppercase())
        } catch (e: Exception) {
            ErrorSeverity.ERROR
        }
    )
}
