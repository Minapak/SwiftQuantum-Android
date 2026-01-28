package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.CircuitDto
import com.swiftquantum.data.dto.CreateCircuitRequest
import com.swiftquantum.data.dto.ExecutionResultDto
import com.swiftquantum.data.dto.MessageResponse
import com.swiftquantum.data.dto.SimulationRequest
import com.swiftquantum.data.dto.UpdateCircuitRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface QuantumApi {
    // Circuit endpoints
    @POST("circuits")
    suspend fun createCircuit(@Body request: CreateCircuitRequest): Response<ApiResponse<CircuitDto>>

    @GET("circuits")
    suspend fun getMyCircuits(): Response<ApiResponse<List<CircuitDto>>>

    @GET("circuits/{id}")
    suspend fun getCircuit(@Path("id") id: String): Response<ApiResponse<CircuitDto>>

    @PATCH("circuits/{id}")
    suspend fun updateCircuit(
        @Path("id") id: String,
        @Body request: UpdateCircuitRequest
    ): Response<ApiResponse<CircuitDto>>

    @DELETE("circuits/{id}")
    suspend fun deleteCircuit(@Path("id") id: String): Response<ApiResponse<MessageResponse>>

    // Simulation endpoints
    @POST("simulate")
    suspend fun runSimulation(@Body request: SimulationRequest): Response<ApiResponse<ExecutionResultDto>>

    @GET("simulate/{id}")
    suspend fun getSimulationResult(@Path("id") id: String): Response<ApiResponse<ExecutionResultDto>>

    @GET("simulate/history")
    suspend fun getExecutionHistory(): Response<ApiResponse<List<ExecutionResultDto>>>
}
