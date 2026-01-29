package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Australian Quantum Computing Standards API (v5.2.0)
 *
 * Integrates standards from Australian universities:
 * - Q-CTRL (University of Sydney): Error suppression and noise mitigation
 * - MicroQiskit (UTS): Mobile-optimized quantum simulation
 * - SQC (UNSW Sydney): Fidelity benchmarking
 * - LabScript (Monash/Melbourne): Hardware control protocols
 */
interface AustralianStandardsApi {

    // ==================== Q-CTRL Error Suppression ====================

    @POST("australian-standards/qctrl/apply")
    suspend fun applyErrorSuppression(
        @Body request: QCTRLApplyRequest
    ): Response<ApiResponse<QCTRLApplyResponse>>

    @GET("australian-standards/qctrl/info")
    suspend fun getQCTRLInfo(): Response<ApiResponse<QCTRLInfoResponse>>

    // ==================== MicroQiskit Mobile Optimization ====================

    @POST("australian-standards/microqiskit/optimize")
    suspend fun optimizeForMobile(
        @Body request: MobileOptimizeRequest
    ): Response<ApiResponse<MobileOptimizeResponse>>

    @POST("australian-standards/microqiskit/check")
    suspend fun checkMobileCompatibility(
        @Body request: MobileCheckRequest
    ): Response<ApiResponse<MobileCompatibilityResponse>>

    @GET("australian-standards/microqiskit/info")
    suspend fun getMicroQiskitInfo(): Response<ApiResponse<MicroQiskitInfoResponse>>

    // ==================== SQC Fidelity Benchmarking ====================

    @POST("australian-standards/sqc/benchmark")
    suspend fun benchmarkFidelity(
        @Body request: SQCBenchmarkRequest
    ): Response<ApiResponse<SQCBenchmarkResponse>>

    @GET("australian-standards/sqc/benchmarks")
    suspend fun getSQCBenchmarks(): Response<ApiResponse<SQCBenchmarksResponse>>

    // ==================== LabScript Protocol ====================

    @POST("australian-standards/labscript/export")
    suspend fun exportToLabScript(
        @Body request: LabScriptExportRequest
    ): Response<ApiResponse<LabScriptExportResponse>>

    @POST("australian-standards/labscript/validate")
    suspend fun validateLabScript(
        @Body request: LabScriptValidateRequest
    ): Response<ApiResponse<LabScriptValidateResponse>>

    // ==================== Combined Analysis ====================

    @POST("australian-standards/analyze")
    suspend fun analyzeWithAllStandards(
        @Body request: AustralianAnalysisRequest
    ): Response<ApiResponse<AustralianAnalysisResponse>>

    @GET("australian-standards/credits")
    suspend fun getCredits(): Response<ApiResponse<AustralianCreditsResponse>>
}

// ==================== Q-CTRL DTOs ====================

data class QCTRLApplyRequest(
    val circuit: CircuitData,
    val ddSequence: String = "XY4",
    val enableRobustControl: Boolean = true,
    val noiseProfile: NoiseProfile? = null
)

data class QCTRLApplyResponse(
    val optimizedCircuit: CircuitData,
    val originalFidelity: Double,
    val optimizedFidelity: Double,
    val improvementFactor: Double,
    val appliedTechniques: List<String>
)

data class QCTRLInfoResponse(
    val version: String,
    val availableSequences: List<DDSequenceInfo>,
    val features: List<String>
)

data class DDSequenceInfo(
    val name: String,
    val description: String,
    val recommendedFor: List<String>
)

data class NoiseProfile(
    val t1: Double? = null,
    val t2: Double? = null,
    val gateErrorRate: Double? = null,
    val readoutErrorRate: Double? = null
)

// ==================== MicroQiskit DTOs ====================

data class MobileOptimizeRequest(
    val circuit: CircuitData,
    val optimizationLevel: String = "MODERATE",
    val targetMemoryMb: Int = 256
)

data class MobileOptimizeResponse(
    val optimizedCircuit: CircuitData,
    val originalGateCount: Int,
    val optimizedGateCount: Int,
    val estimatedMemoryMb: Double,
    val estimatedTimeMs: Double,
    val optimizationsApplied: List<String>
)

data class MobileCheckRequest(
    val circuit: CircuitData,
    val maxQubits: Int = 20,
    val maxMemoryMb: Int = 512
)

data class MobileCompatibilityResponse(
    val isCompatible: Boolean,
    val estimatedMemoryMb: Double,
    val estimatedTimeMs: Double,
    val warnings: List<String>,
    val recommendations: List<String>
)

data class MicroQiskitInfoResponse(
    val version: String,
    val optimizationLevels: List<OptimizationLevelInfo>,
    val features: List<String>
)

data class OptimizationLevelInfo(
    val level: String,
    val description: String,
    val techniques: List<String>
)

// ==================== SQC DTOs ====================

data class SQCBenchmarkRequest(
    val circuit: CircuitData,
    val shots: Int = 1000,
    val includeDetailedMetrics: Boolean = true
)

data class SQCBenchmarkResponse(
    val overallFidelity: Double,
    val grade: String,
    val gradeColor: String,
    val singleQubitFidelity: Double,
    val twoQubitFidelity: Double,
    val meetsSQCStandard: Boolean,
    val detailedMetrics: FidelityMetrics?,
    val recommendations: List<String>
)

data class FidelityMetrics(
    val gateErrorRates: Map<String, Double>,
    val readoutErrorRates: Map<Int, Double>,
    val coherenceTimes: CoherenceTimes?
)

data class CoherenceTimes(
    val t1: Double,
    val t2: Double,
    val tPhi: Double
)

data class SQCBenchmarksResponse(
    val grades: List<GradeInfo>,
    val sqcReferenceValue: Double,
    val lastUpdated: String
)

data class GradeInfo(
    val grade: String,
    val displayName: String,
    val threshold: Double,
    val color: String
)

// ==================== LabScript DTOs ====================

data class LabScriptExportRequest(
    val circuit: CircuitData,
    val shotDuration: Double = 1e-6,
    val includeComments: Boolean = true
)

data class LabScriptExportResponse(
    val sequenceData: String,
    val hdf5Available: Boolean,
    val hdf5Url: String?,
    val totalDuration: Double,
    val channelCount: Int,
    val instructionCount: Int
)

data class LabScriptValidateRequest(
    val sequenceData: String
)

data class LabScriptValidateResponse(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

// ==================== Combined Analysis DTOs ====================

data class AustralianAnalysisRequest(
    val circuit: CircuitData,
    val enableQCTRL: Boolean = true,
    val enableMicroQiskit: Boolean = true,
    val enableSQC: Boolean = true,
    val enableLabScript: Boolean = false
)

data class AustralianAnalysisResponse(
    val qctrlResult: QCTRLApplyResponse?,
    val mobileOptimization: MobileOptimizeResponse?,
    val fidelityBenchmark: SQCBenchmarkResponse?,
    val labscriptExport: LabScriptExportResponse?,
    val summary: AnalysisSummary
)

data class AnalysisSummary(
    val overallScore: Double,
    val recommendations: List<String>,
    val standardsCompliance: Map<String, Boolean>
)

data class AustralianCreditsResponse(
    val credits: List<CreditInfo>
)

data class CreditInfo(
    val name: String,
    val institution: String,
    val description: String,
    val license: String,
    val technique: String,
    val github: String?,
    val website: String?
)

// ==================== Shared DTOs ====================

data class CircuitData(
    val qubits: Int,
    val gates: List<GateData>
)

data class GateData(
    val type: String,
    val targets: List<Int>,
    val controls: List<Int>? = null,
    val parameters: List<Double>? = null
)
