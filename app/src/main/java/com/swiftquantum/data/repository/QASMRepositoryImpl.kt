package com.swiftquantum.data.repository

import com.swiftquantum.data.api.QASMApi
import com.swiftquantum.data.dto.CircuitDto
import com.swiftquantum.data.dto.ExportOptionsDto
import com.swiftquantum.data.dto.ExportQASMRequestDto
import com.swiftquantum.data.dto.ImportQASMRequestDto
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.DifficultyLevel
import com.swiftquantum.domain.model.ExportOptions
import com.swiftquantum.domain.model.ImportResult
import com.swiftquantum.domain.model.QASMCircuit
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMValidationResult
import com.swiftquantum.domain.model.QASMVersion
import com.swiftquantum.domain.model.parseQASMToCircuit
import com.swiftquantum.domain.repository.QASMRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QASMRepositoryImpl @Inject constructor(
    private val qasmApi: QASMApi
) : QASMRepository {

    override suspend fun importQASM(
        code: String,
        version: QASMVersion?
    ): Result<ImportResult> = withContext(Dispatchers.IO) {
        try {
            // Try API first
            val response = qasmApi.importQASM(
                ImportQASMRequestDto(
                    code = code,
                    version = version?.name?.lowercase()
                )
            )

            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { dto ->
                    return@withContext Result.success(dto.toDomain())
                }
            }

            // Fall back to local parsing
            val result = code.parseQASMToCircuit()
            Result.success(result)
        } catch (e: Exception) {
            // Fall back to local parsing on network error
            try {
                val result = code.parseQASMToCircuit()
                Result.success(result)
            } catch (parseError: Exception) {
                Result.failure(parseError)
            }
        }
    }

    override suspend fun exportQASM(
        circuit: Circuit,
        options: ExportOptions
    ): Result<Pair<String, QASMCircuit>> = withContext(Dispatchers.IO) {
        try {
            // Try API first
            val response = qasmApi.exportQASM(
                ExportQASMRequestDto(
                    circuit = CircuitDto.fromDomain(circuit),
                    options = ExportOptionsDto.fromDomain(options)
                )
            )

            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { dto ->
                    val qasmCircuit = dto.qasmCircuit?.toDomain()
                        ?: QASMCircuit.fromCircuit(circuit, options.version)
                    return@withContext Result.success(dto.code to qasmCircuit)
                }
            }

            // Fall back to local export
            val qasmCircuit = QASMCircuit.fromCircuit(circuit, options.version)
            val code = qasmCircuit.toQASMCode(options)
            Result.success(code to qasmCircuit)
        } catch (e: Exception) {
            // Fall back to local export on network error
            try {
                val qasmCircuit = QASMCircuit.fromCircuit(circuit, options.version)
                val code = qasmCircuit.toQASMCode(options)
                Result.success(code to qasmCircuit)
            } catch (exportError: Exception) {
                Result.failure(exportError)
            }
        }
    }

    override suspend fun validateQASM(code: String): Result<QASMValidationResult> =
        withContext(Dispatchers.IO) {
            try {
                val response = qasmApi.validateQASM(
                    ImportQASMRequestDto(code = code, validateOnly = true)
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { dto ->
                        return@withContext Result.success(dto.toDomain())
                    }
                }

                // Local validation fallback
                val result = code.parseQASMToCircuit()
                Result.success(
                    QASMValidationResult(
                        isValid = result.success,
                        errors = result.errors.map {
                            com.swiftquantum.domain.model.QASMSyntaxError(
                                line = 0, column = 0, message = it
                            )
                        },
                        warnings = result.warnings.map {
                            com.swiftquantum.domain.model.QASMSyntaxError(
                                line = 0, column = 0, message = it,
                                severity = com.swiftquantum.domain.model.ErrorSeverity.WARNING
                            )
                        }
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getTemplates(
        category: QASMTemplateCategory?
    ): Result<List<QASMTemplate>> = withContext(Dispatchers.IO) {
        try {
            val response = qasmApi.getTemplates(category = category?.name?.lowercase())

            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { dtos ->
                    return@withContext Result.success(dtos.map { it.toDomain() })
                }
            }

            // Return built-in templates as fallback
            Result.success(getBuiltInTemplates(category))
        } catch (e: Exception) {
            // Return built-in templates on network error
            Result.success(getBuiltInTemplates(category))
        }
    }

    override suspend fun getTemplate(templateId: String): Result<QASMTemplate> =
        withContext(Dispatchers.IO) {
            try {
                val response = qasmApi.getTemplate(templateId)

                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { dto ->
                        return@withContext Result.success(dto.toDomain())
                    }
                }

                // Check built-in templates
                getBuiltInTemplates(null).find { it.id == templateId }?.let {
                    return@withContext Result.success(it)
                }

                Result.failure(Exception("Template not found"))
            } catch (e: Exception) {
                // Check built-in templates
                getBuiltInTemplates(null).find { it.id == templateId }?.let {
                    return@withContext Result.success(it)
                }
                Result.failure(e)
            }
        }

    private fun getBuiltInTemplates(category: QASMTemplateCategory?): List<QASMTemplate> {
        val templates = listOf(
            QASMTemplate(
                id = "bell_state",
                name = "Bell State",
                description = "Creates a maximally entangled Bell state",
                category = QASMTemplateCategory.ENTANGLEMENT,
                numQubits = 2,
                difficultyLevel = DifficultyLevel.BEGINNER,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// Bell State Circuit
                    |qreg q[2];
                    |creg c[2];
                    |
                    |h q[0];
                    |cx q[0], q[1];
                    |
                    |measure q -> c;
                """.trimMargin()
            ),
            QASMTemplate(
                id = "ghz_3",
                name = "GHZ State (3 qubits)",
                description = "Creates a 3-qubit GHZ entangled state",
                category = QASMTemplateCategory.ENTANGLEMENT,
                numQubits = 3,
                difficultyLevel = DifficultyLevel.BEGINNER,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// GHZ State Circuit
                    |qreg q[3];
                    |creg c[3];
                    |
                    |h q[0];
                    |cx q[0], q[1];
                    |cx q[0], q[2];
                    |
                    |measure q -> c;
                """.trimMargin()
            ),
            QASMTemplate(
                id = "superposition",
                name = "Equal Superposition",
                description = "Creates equal superposition of all states",
                category = QASMTemplateCategory.BASICS,
                numQubits = 3,
                difficultyLevel = DifficultyLevel.BEGINNER,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// Equal Superposition
                    |qreg q[3];
                    |creg c[3];
                    |
                    |h q[0];
                    |h q[1];
                    |h q[2];
                    |
                    |measure q -> c;
                """.trimMargin()
            ),
            QASMTemplate(
                id = "deutsch_jozsa",
                name = "Deutsch-Jozsa Algorithm",
                description = "Demonstrates quantum parallelism",
                category = QASMTemplateCategory.ALGORITHMS,
                numQubits = 3,
                difficultyLevel = DifficultyLevel.INTERMEDIATE,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// Deutsch-Jozsa Algorithm
                    |qreg q[3];
                    |creg c[2];
                    |
                    |// Initialize
                    |x q[2];
                    |h q[0];
                    |h q[1];
                    |h q[2];
                    |
                    |// Oracle (balanced function)
                    |cx q[0], q[2];
                    |cx q[1], q[2];
                    |
                    |// Measure
                    |h q[0];
                    |h q[1];
                    |
                    |measure q[0] -> c[0];
                    |measure q[1] -> c[1];
                """.trimMargin()
            ),
            QASMTemplate(
                id = "grover_2",
                name = "Grover's Search (2 qubits)",
                description = "Quantum search algorithm for 4 elements",
                category = QASMTemplateCategory.ALGORITHMS,
                numQubits = 2,
                difficultyLevel = DifficultyLevel.INTERMEDIATE,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// Grover's Search - finds |11>
                    |qreg q[2];
                    |creg c[2];
                    |
                    |// Initialize superposition
                    |h q[0];
                    |h q[1];
                    |
                    |// Oracle for |11>
                    |cz q[0], q[1];
                    |
                    |// Diffusion operator
                    |h q[0];
                    |h q[1];
                    |z q[0];
                    |z q[1];
                    |cz q[0], q[1];
                    |h q[0];
                    |h q[1];
                    |
                    |measure q -> c;
                """.trimMargin()
            ),
            QASMTemplate(
                id = "quantum_teleportation",
                name = "Quantum Teleportation",
                description = "Teleports quantum state using entanglement",
                category = QASMTemplateCategory.ALGORITHMS,
                numQubits = 3,
                difficultyLevel = DifficultyLevel.ADVANCED,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// Quantum Teleportation
                    |qreg q[3];
                    |creg c[3];
                    |
                    |// Prepare state to teleport on q[0]
                    |h q[0];
                    |t q[0];
                    |
                    |// Create entanglement between q[1] and q[2]
                    |h q[1];
                    |cx q[1], q[2];
                    |
                    |// Bell measurement on q[0], q[1]
                    |cx q[0], q[1];
                    |h q[0];
                    |
                    |measure q[0] -> c[0];
                    |measure q[1] -> c[1];
                    |
                    |// Classical controlled operations would go here
                    |// (simulated by measuring q[2])
                    |measure q[2] -> c[2];
                """.trimMargin()
            ),
            QASMTemplate(
                id = "qft_3",
                name = "Quantum Fourier Transform",
                description = "3-qubit QFT circuit",
                category = QASMTemplateCategory.ALGORITHMS,
                numQubits = 3,
                difficultyLevel = DifficultyLevel.ADVANCED,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// 3-qubit Quantum Fourier Transform
                    |qreg q[3];
                    |creg c[3];
                    |
                    |// Initialize with some state
                    |x q[0];
                    |
                    |// QFT
                    |h q[0];
                    |crz(pi/2) q[1], q[0];
                    |crz(pi/4) q[2], q[0];
                    |h q[1];
                    |crz(pi/2) q[2], q[1];
                    |h q[2];
                    |
                    |// Swap
                    |swap q[0], q[2];
                    |
                    |measure q -> c;
                """.trimMargin()
            ),
            QASMTemplate(
                id = "vqe_ansatz",
                name = "VQE Ansatz",
                description = "Variational quantum eigensolver ansatz",
                category = QASMTemplateCategory.VARIATIONAL,
                numQubits = 2,
                difficultyLevel = DifficultyLevel.ADVANCED,
                code = """
                    |OPENQASM 2.0;
                    |include "qelib1.inc";
                    |
                    |// VQE Ansatz for H2 molecule
                    |qreg q[2];
                    |creg c[2];
                    |
                    |// Parametrized ansatz
                    |ry(0.5) q[0];
                    |ry(0.5) q[1];
                    |cx q[0], q[1];
                    |ry(0.3) q[0];
                    |ry(0.3) q[1];
                    |
                    |measure q -> c;
                """.trimMargin()
            )
        )

        return if (category != null) {
            templates.filter { it.category == category }
        } else {
            templates
        }
    }
}
