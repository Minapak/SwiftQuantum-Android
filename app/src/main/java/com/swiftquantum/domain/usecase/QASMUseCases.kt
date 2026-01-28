package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExportOptions
import com.swiftquantum.domain.model.ImportResult
import com.swiftquantum.domain.model.QASMCircuit
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMValidationResult
import com.swiftquantum.domain.model.QASMVersion
import com.swiftquantum.domain.repository.QASMRepository
import javax.inject.Inject

/**
 * Use case for importing QASM code to circuit
 */
class ImportQASMUseCase @Inject constructor(
    private val qasmRepository: QASMRepository
) {
    suspend operator fun invoke(
        code: String,
        version: QASMVersion? = null
    ): Result<ImportResult> {
        if (code.isBlank()) {
            return Result.failure(IllegalArgumentException("QASM code cannot be empty"))
        }

        return qasmRepository.importQASM(code, version)
    }
}

/**
 * Use case for exporting circuit to QASM code
 */
class ExportQASMUseCase @Inject constructor(
    private val qasmRepository: QASMRepository
) {
    suspend operator fun invoke(
        circuit: Circuit,
        options: ExportOptions = ExportOptions()
    ): Result<Pair<String, QASMCircuit>> {
        // Validate circuit before export
        val validation = circuit.validate()
        if (!validation.isValid) {
            return Result.failure(
                IllegalArgumentException("Invalid circuit: ${validation.errors.joinToString(", ")}")
            )
        }

        return qasmRepository.exportQASM(circuit, options)
    }
}

/**
 * Use case for validating QASM code
 */
class ValidateQASMUseCase @Inject constructor(
    private val qasmRepository: QASMRepository
) {
    suspend operator fun invoke(code: String): Result<QASMValidationResult> {
        if (code.isBlank()) {
            return Result.success(
                QASMValidationResult(
                    isValid = false,
                    errors = listOf(
                        com.swiftquantum.domain.model.QASMSyntaxError(
                            line = 0, column = 0, message = "QASM code cannot be empty"
                        )
                    )
                )
            )
        }

        return qasmRepository.validateQASM(code)
    }
}

/**
 * Use case for loading QASM templates
 */
class LoadQASMTemplatesUseCase @Inject constructor(
    private val qasmRepository: QASMRepository
) {
    suspend operator fun invoke(
        category: QASMTemplateCategory? = null
    ): Result<List<QASMTemplate>> {
        return qasmRepository.getTemplates(category)
    }
}

/**
 * Use case for loading a specific QASM template
 */
class LoadQASMTemplateUseCase @Inject constructor(
    private val qasmRepository: QASMRepository
) {
    suspend operator fun invoke(templateId: String): Result<QASMTemplate> {
        if (templateId.isBlank()) {
            return Result.failure(IllegalArgumentException("Template ID cannot be empty"))
        }

        return qasmRepository.getTemplate(templateId)
    }
}
