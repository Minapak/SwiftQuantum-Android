package com.swiftquantum.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supported QASM versions
 */
@Serializable
enum class QASMVersion(
    val displayName: String,
    val fileExtension: String,
    val header: String
) {
    @SerialName("qasm2")
    QASM2(
        displayName = "OpenQASM 2.0",
        fileExtension = ".qasm",
        header = "OPENQASM 2.0;"
    ),

    @SerialName("qasm3")
    QASM3(
        displayName = "OpenQASM 3.0",
        fileExtension = ".qasm",
        header = "OPENQASM 3.0;"
    );

    companion object {
        fun fromString(version: String): QASMVersion? = entries.find {
            it.name.equals(version, ignoreCase = true) ||
            version.contains(it.name.last().toString())
        }

        fun detectFromCode(code: String): QASMVersion {
            return when {
                code.contains("OPENQASM 3") -> QASM3
                else -> QASM2
            }
        }
    }
}

/**
 * QASM qubit register definition
 */
@Serializable
data class QASMQubitRegister(
    val name: String,
    val size: Int
) {
    fun toQASM2(): String = "qreg $name[$size];"
    fun toQASM3(): String = "qubit[$size] $name;"
}

/**
 * QASM classical register definition
 */
@Serializable
data class QASMClassicalRegister(
    val name: String,
    val size: Int
) {
    fun toQASM2(): String = "creg $name[$size];"
    fun toQASM3(): String = "bit[$size] $name;"
}

/**
 * QASM gate instruction
 */
@Serializable
data class QASMGateInstruction(
    @SerialName("gate_name")
    val gateName: String,

    val parameters: List<Double> = emptyList(),

    @SerialName("qubit_args")
    val qubitArgs: List<String> // e.g., ["q[0]", "q[1]"]
) {
    fun toQASM(): String {
        val paramStr = if (parameters.isNotEmpty()) {
            "(${parameters.joinToString(", ") { formatParameter(it) }})"
        } else ""
        return "$gateName$paramStr ${qubitArgs.joinToString(", ")};"
    }

    private fun formatParameter(value: Double): String {
        return when {
            value == Math.PI -> "pi"
            value == Math.PI / 2 -> "pi/2"
            value == Math.PI / 4 -> "pi/4"
            value == -Math.PI -> "-pi"
            value == -Math.PI / 2 -> "-pi/2"
            value == -Math.PI / 4 -> "-pi/4"
            else -> String.format("%.6f", value)
        }
    }
}

/**
 * Complete QASM circuit representation
 */
@Serializable
data class QASMCircuit(
    val version: QASMVersion = QASMVersion.QASM2,

    val includes: List<String> = listOf("qelib1.inc"),

    @SerialName("qubit_registers")
    val qubitRegisters: List<QASMQubitRegister> = emptyList(),

    @SerialName("classical_registers")
    val classicalRegisters: List<QASMClassicalRegister> = emptyList(),

    val gates: List<QASMGateInstruction> = emptyList(),

    val measurements: List<String> = emptyList(), // e.g., ["measure q -> c;"]

    val comments: Map<Int, String> = emptyMap() // Line number to comment
) {
    val totalQubits: Int
        get() = qubitRegisters.sumOf { it.size }

    val totalClassicalBits: Int
        get() = classicalRegisters.sumOf { it.size }

    fun toQASMCode(options: ExportOptions = ExportOptions()): String {
        val builder = StringBuilder()

        // Header
        builder.appendLine(version.header)

        // Includes
        if (version == QASMVersion.QASM2) {
            includes.forEach { builder.appendLine("include \"$it\";") }
        }

        if (options.prettyPrint) builder.appendLine()

        // Comments
        if (options.includeComments) {
            builder.appendLine("// Circuit exported from SwiftQuantum")
            builder.appendLine("// Qubits: $totalQubits, Classical bits: $totalClassicalBits")
            builder.appendLine()
        }

        // Registers
        qubitRegisters.forEach { reg ->
            builder.appendLine(if (version == QASMVersion.QASM2) reg.toQASM2() else reg.toQASM3())
        }
        classicalRegisters.forEach { reg ->
            builder.appendLine(if (version == QASMVersion.QASM2) reg.toQASM2() else reg.toQASM3())
        }

        if (options.prettyPrint) builder.appendLine()

        // Gates
        gates.forEachIndexed { index, gate ->
            if (options.includeComments && comments.containsKey(index)) {
                builder.appendLine("// ${comments[index]}")
            }
            builder.appendLine(gate.toQASM())
        }

        if (options.prettyPrint) builder.appendLine()

        // Measurements
        measurements.forEach { builder.appendLine(it) }

        return builder.toString().trim()
    }

    companion object {
        fun fromCircuit(circuit: Circuit, version: QASMVersion = QASMVersion.QASM2): QASMCircuit {
            val qubitReg = QASMQubitRegister("q", circuit.numQubits)
            val classicalReg = QASMClassicalRegister("c", circuit.numQubits)

            val gates = circuit.gates.map { gate ->
                val gateName = mapGateToQASM(gate.type)
                val params = gate.parameters?.let {
                    listOfNotNull(it.theta, it.phi, it.lambda)
                } ?: emptyList()

                val qubits = mutableListOf<String>()
                gate.controlQubits.forEach { qubits.add("q[$it]") }
                gate.targetQubits.forEach { qubits.add("q[$it]") }

                QASMGateInstruction(
                    gateName = gateName,
                    parameters = params,
                    qubitArgs = qubits
                )
            }

            val measurements = (0 until circuit.numQubits).map {
                "measure q[$it] -> c[$it];"
            }

            return QASMCircuit(
                version = version,
                qubitRegisters = listOf(qubitReg),
                classicalRegisters = listOf(classicalReg),
                gates = gates,
                measurements = measurements
            )
        }

        private fun mapGateToQASM(gateType: GateType): String = when (gateType) {
            GateType.H -> "h"
            GateType.X -> "x"
            GateType.Y -> "y"
            GateType.Z -> "z"
            GateType.S -> "s"
            GateType.T -> "t"
            GateType.RX -> "rx"
            GateType.RY -> "ry"
            GateType.RZ -> "rz"
            GateType.U1 -> "u1"
            GateType.U2 -> "u2"
            GateType.U3 -> "u3"
            GateType.CNOT -> "cx"
            GateType.CZ -> "cz"
            GateType.CY -> "cy"
            GateType.SWAP -> "swap"
            GateType.ISWAP -> "iswap"
            GateType.CRX -> "crx"
            GateType.CRY -> "cry"
            GateType.CRZ -> "crz"
            GateType.TOFFOLI -> "ccx"
            GateType.FREDKIN -> "cswap"
            GateType.CCZ -> "ccz"
        }
    }
}

/**
 * Options for exporting circuits to QASM
 */
@Serializable
data class ExportOptions(
    val version: QASMVersion = QASMVersion.QASM2,

    @SerialName("include_comments")
    val includeComments: Boolean = true,

    @SerialName("pretty_print")
    val prettyPrint: Boolean = true,

    @SerialName("include_measurements")
    val includeMeasurements: Boolean = true,

    @SerialName("optimize_gates")
    val optimizeGates: Boolean = false
)

/**
 * Result of importing QASM code
 */
@Serializable
data class ImportResult(
    val success: Boolean,

    val circuit: Circuit? = null,

    @SerialName("qasm_circuit")
    val qasmCircuit: QASMCircuit? = null,

    val warnings: List<String> = emptyList(),

    val errors: List<String> = emptyList(),

    @SerialName("detected_version")
    val detectedVersion: QASMVersion? = null,

    @SerialName("line_count")
    val lineCount: Int = 0,

    @SerialName("gate_count")
    val gateCount: Int = 0
) {
    companion object {
        fun success(circuit: Circuit, qasmCircuit: QASMCircuit, warnings: List<String> = emptyList()) = ImportResult(
            success = true,
            circuit = circuit,
            qasmCircuit = qasmCircuit,
            warnings = warnings,
            detectedVersion = qasmCircuit.version,
            lineCount = 0,
            gateCount = circuit.gates.size
        )

        fun failure(errors: List<String>, warnings: List<String> = emptyList()) = ImportResult(
            success = false,
            errors = errors,
            warnings = warnings
        )
    }
}

/**
 * QASM template for quick start
 */
@Serializable
data class QASMTemplate(
    val id: String,
    val name: String,
    val description: String,
    val category: QASMTemplateCategory,
    val code: String,
    val numQubits: Int,
    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel = DifficultyLevel.BEGINNER
)

@Serializable
enum class QASMTemplateCategory {
    @SerialName("basics")
    BASICS,

    @SerialName("entanglement")
    ENTANGLEMENT,

    @SerialName("algorithms")
    ALGORITHMS,

    @SerialName("error_correction")
    ERROR_CORRECTION,

    @SerialName("variational")
    VARIATIONAL
}

@Serializable
enum class DifficultyLevel {
    @SerialName("beginner")
    BEGINNER,

    @SerialName("intermediate")
    INTERMEDIATE,

    @SerialName("advanced")
    ADVANCED
}

/**
 * QASM syntax error
 */
@Serializable
data class QASMSyntaxError(
    val line: Int,
    val column: Int,
    val message: String,
    val severity: ErrorSeverity = ErrorSeverity.ERROR
)

@Serializable
enum class ErrorSeverity {
    @SerialName("warning")
    WARNING,

    @SerialName("error")
    ERROR
}

/**
 * QASM validation result
 */
@Serializable
data class QASMValidationResult(
    @SerialName("is_valid")
    val isValid: Boolean,
    val errors: List<QASMSyntaxError> = emptyList(),
    val warnings: List<QASMSyntaxError> = emptyList()
)

// Extension function for local QASM parsing
fun String.parseQASMToCircuit(): ImportResult {
    return try {
        val lines = this.lines().map { it.trim() }.filter { it.isNotEmpty() && !it.startsWith("//") }
        val warnings = mutableListOf<String>()
        val errors = mutableListOf<String>()

        // Detect version
        val version = QASMVersion.detectFromCode(this)

        // Parse registers
        var numQubits = 0
        val qubitRegisters = mutableListOf<QASMQubitRegister>()
        val classicalRegisters = mutableListOf<QASMClassicalRegister>()
        val gateInstructions = mutableListOf<QASMGateInstruction>()
        val measurements = mutableListOf<String>()

        val qregPattern = Regex("""qreg\s+(\w+)\[(\d+)\];?""")
        val cregPattern = Regex("""creg\s+(\w+)\[(\d+)\];?""")
        val qubitPattern = Regex("""qubit\[(\d+)\]\s+(\w+);?""")
        val bitPattern = Regex("""bit\[(\d+)\]\s+(\w+);?""")
        val gatePattern = Regex("""(\w+)(?:\(([^)]*)\))?\s+([^;]+);?""")
        val measurePattern = Regex("""measure\s+(.+);?""")

        for (line in lines) {
            when {
                line.startsWith("OPENQASM") || line.startsWith("include") -> continue

                qregPattern.matches(line) -> {
                    qregPattern.find(line)?.let {
                        val name = it.groupValues[1]
                        val size = it.groupValues[2].toInt()
                        qubitRegisters.add(QASMQubitRegister(name, size))
                        numQubits += size
                    }
                }

                cregPattern.matches(line) -> {
                    cregPattern.find(line)?.let {
                        classicalRegisters.add(
                            QASMClassicalRegister(it.groupValues[1], it.groupValues[2].toInt())
                        )
                    }
                }

                qubitPattern.matches(line) -> {
                    qubitPattern.find(line)?.let {
                        val size = it.groupValues[1].toInt()
                        qubitRegisters.add(QASMQubitRegister(it.groupValues[2], size))
                        numQubits += size
                    }
                }

                bitPattern.matches(line) -> {
                    bitPattern.find(line)?.let {
                        classicalRegisters.add(
                            QASMClassicalRegister(it.groupValues[2], it.groupValues[1].toInt())
                        )
                    }
                }

                measurePattern.matches(line) -> {
                    measurements.add(line)
                }

                gatePattern.matches(line) && !line.startsWith("measure") -> {
                    gatePattern.find(line)?.let {
                        val gateName = it.groupValues[1].lowercase()
                        val paramStr = it.groupValues[2]
                        val qubitsStr = it.groupValues[3]

                        val params = if (paramStr.isNotEmpty()) {
                            paramStr.split(",").map { p ->
                                parseParameter(p.trim())
                            }
                        } else emptyList()

                        val qubits = qubitsStr.split(",").map { it.trim() }

                        gateInstructions.add(QASMGateInstruction(gateName, params, qubits))
                    }
                }
            }
        }

        if (numQubits == 0) {
            errors.add("No qubit registers found")
            return ImportResult.failure(errors, warnings)
        }

        // Convert to Circuit
        val gates = gateInstructions.mapNotNull { instruction ->
            val gateType = mapQASMToGateType(instruction.gateName)
            if (gateType == null) {
                warnings.add("Unknown gate: ${instruction.gateName}")
                return@mapNotNull null
            }

            val qubitIndices = instruction.qubitArgs.map { parseQubitIndex(it) }
            val (controlQubits, targetQubits) = if (gateType.qubitCount > 1 && qubitIndices.size > 1) {
                qubitIndices.dropLast(1) to listOf(qubitIndices.last())
            } else {
                emptyList<Int>() to qubitIndices
            }

            val parameters = if (instruction.parameters.isNotEmpty()) {
                GateParameters(
                    theta = instruction.parameters.getOrNull(0),
                    phi = instruction.parameters.getOrNull(1),
                    lambda = instruction.parameters.getOrNull(2)
                )
            } else null

            Gate(
                type = gateType,
                targetQubits = targetQubits,
                controlQubits = controlQubits,
                parameters = parameters,
                position = gateInstructions.indexOf(instruction)
            )
        }

        val circuit = Circuit(
            name = "Imported QASM Circuit",
            description = "Imported from QASM code",
            numQubits = numQubits,
            gates = gates
        )

        val qasmCircuit = QASMCircuit(
            version = version,
            qubitRegisters = qubitRegisters,
            classicalRegisters = classicalRegisters,
            gates = gateInstructions,
            measurements = measurements
        )

        ImportResult.success(circuit, qasmCircuit, warnings)
    } catch (e: Exception) {
        ImportResult.failure(listOf("Parse error: ${e.message}"))
    }
}

private fun parseParameter(param: String): Double {
    return when (param.lowercase()) {
        "pi" -> Math.PI
        "-pi" -> -Math.PI
        "pi/2" -> Math.PI / 2
        "-pi/2" -> -Math.PI / 2
        "pi/4" -> Math.PI / 4
        "-pi/4" -> -Math.PI / 4
        "pi/8" -> Math.PI / 8
        else -> param.toDoubleOrNull() ?: 0.0
    }
}

private fun parseQubitIndex(qubitArg: String): Int {
    val match = Regex("""\w+\[(\d+)\]""").find(qubitArg)
    return match?.groupValues?.get(1)?.toInt() ?: 0
}

private fun mapQASMToGateType(gateName: String): GateType? = when (gateName.lowercase()) {
    "h", "hadamard" -> GateType.H
    "x", "not" -> GateType.X
    "y" -> GateType.Y
    "z" -> GateType.Z
    "s", "sdg" -> GateType.S
    "t", "tdg" -> GateType.T
    "rx" -> GateType.RX
    "ry" -> GateType.RY
    "rz" -> GateType.RZ
    "u1", "p", "phase" -> GateType.U1
    "u2" -> GateType.U2
    "u3", "u" -> GateType.U3
    "cx", "cnot" -> GateType.CNOT
    "cz" -> GateType.CZ
    "cy" -> GateType.CY
    "swap" -> GateType.SWAP
    "iswap" -> GateType.ISWAP
    "crx" -> GateType.CRX
    "cry" -> GateType.CRY
    "crz" -> GateType.CRZ
    "ccx", "toffoli" -> GateType.TOFFOLI
    "cswap", "fredkin" -> GateType.FREDKIN
    "ccz" -> GateType.CCZ
    else -> null
}
