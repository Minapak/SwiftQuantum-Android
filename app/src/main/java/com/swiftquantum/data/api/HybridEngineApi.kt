package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.BenchmarkResultDto
import com.swiftquantum.data.dto.EngineStatusDto
import com.swiftquantum.data.dto.HybridExecutionRequestDto
import com.swiftquantum.data.dto.HybridExecutionResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API interface for Cloud Hybrid Engine operations
 */
interface HybridEngineApi {

    /**
     * Execute a circuit using the specified hybrid engine
     */
    @POST("engine/execute")
    suspend fun executeWithEngine(
        @Body request: HybridExecutionRequestDto
    ): Response<ApiResponse<HybridExecutionResultDto>>

    /**
     * Get benchmark results comparing different engines
     */
    @GET("engine/benchmarks")
    suspend fun getBenchmarks(
        @Query("num_qubits") numQubits: Int? = null,
        @Query("limit") limit: Int? = 10
    ): Response<ApiResponse<List<BenchmarkResultDto>>>

    /**
     * Run a new benchmark with specific parameters
     */
    @POST("engine/benchmarks/run")
    suspend fun runBenchmark(
        @Body request: HybridExecutionRequestDto
    ): Response<ApiResponse<BenchmarkResultDto>>

    /**
     * Get status of all hybrid engines
     */
    @GET("engine/status")
    suspend fun getEngineStatus(): Response<ApiResponse<List<EngineStatusDto>>>

    /**
     * Get status of a specific engine
     */
    @GET("engine/status")
    suspend fun getEngineStatus(
        @Query("engine") engineType: String
    ): Response<ApiResponse<EngineStatusDto>>
}
