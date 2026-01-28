package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.ExportQASMRequestDto
import com.swiftquantum.data.dto.ExportQASMResponseDto
import com.swiftquantum.data.dto.ImportQASMRequestDto
import com.swiftquantum.data.dto.ImportResultDto
import com.swiftquantum.data.dto.QASMTemplateDto
import com.swiftquantum.data.dto.QASMValidationResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API interface for QASM import/export operations
 */
interface QASMApi {

    /**
     * Import QASM code and convert to circuit
     */
    @POST("qasm/import")
    suspend fun importQASM(
        @Body request: ImportQASMRequestDto
    ): Response<ApiResponse<ImportResultDto>>

    /**
     * Export circuit to QASM code
     */
    @POST("qasm/export")
    suspend fun exportQASM(
        @Body request: ExportQASMRequestDto
    ): Response<ApiResponse<ExportQASMResponseDto>>

    /**
     * Get available QASM templates
     */
    @GET("qasm/templates")
    suspend fun getTemplates(
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): Response<ApiResponse<List<QASMTemplateDto>>>

    /**
     * Get a specific QASM template by ID
     */
    @GET("qasm/templates/{id}")
    suspend fun getTemplate(
        @Path("id") templateId: String
    ): Response<ApiResponse<QASMTemplateDto>>

    /**
     * Validate QASM code without importing
     */
    @POST("qasm/validate")
    suspend fun validateQASM(
        @Body request: ImportQASMRequestDto
    ): Response<ApiResponse<QASMValidationResultDto>>
}
