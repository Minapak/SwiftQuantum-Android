package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExportOptions
import com.swiftquantum.domain.model.ImportResult
import com.swiftquantum.domain.model.QASMCircuit
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMValidationResult
import com.swiftquantum.domain.model.QASMVersion

/**
 * Repository interface for QASM operations
 */
interface QASMRepository {

    /**
     * Import QASM code and convert to circuit
     */
    suspend fun importQASM(
        code: String,
        version: QASMVersion? = null
    ): Result<ImportResult>

    /**
     * Export circuit to QASM code
     */
    suspend fun exportQASM(
        circuit: Circuit,
        options: ExportOptions = ExportOptions()
    ): Result<Pair<String, QASMCircuit>>

    /**
     * Validate QASM code without importing
     */
    suspend fun validateQASM(code: String): Result<QASMValidationResult>

    /**
     * Get available QASM templates
     */
    suspend fun getTemplates(
        category: QASMTemplateCategory? = null
    ): Result<List<QASMTemplate>>

    /**
     * Get a specific template by ID
     */
    suspend fun getTemplate(templateId: String): Result<QASMTemplate>
}
