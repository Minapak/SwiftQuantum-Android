package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.BackendsResponse
import com.swiftquantum.data.dto.IBMConnectRequest
import com.swiftquantum.data.dto.IBMQuantumBackendDto
import com.swiftquantum.data.dto.IBMQuantumConnectionDto
import com.swiftquantum.data.dto.IBMQuantumJobDto
import com.swiftquantum.data.dto.MessageResponse
import com.swiftquantum.data.dto.SubmitJobRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BridgeApi {
    // IBM Quantum connection
    @POST("ibm/connect")
    suspend fun connectToIBM(@Body request: IBMConnectRequest): Response<ApiResponse<IBMQuantumConnectionDto>>

    @POST("ibm/disconnect")
    suspend fun disconnectFromIBM(): Response<ApiResponse<MessageResponse>>

    @GET("ibm/status")
    suspend fun getConnectionStatus(): Response<ApiResponse<IBMQuantumConnectionDto>>

    // Backend information
    @GET("ibm/backends")
    suspend fun getAvailableBackends(): Response<ApiResponse<BackendsResponse>>

    @GET("ibm/backends/{name}")
    suspend fun getBackendStatus(@Path("name") name: String): Response<ApiResponse<IBMQuantumBackendDto>>

    // Job management
    @POST("ibm/jobs")
    suspend fun submitJob(@Body request: SubmitJobRequest): Response<ApiResponse<IBMQuantumJobDto>>

    @GET("ibm/jobs/{id}")
    suspend fun getJobStatus(@Path("id") id: String): Response<ApiResponse<IBMQuantumJobDto>>

    @DELETE("ibm/jobs/{id}")
    suspend fun cancelJob(@Path("id") id: String): Response<ApiResponse<MessageResponse>>

    @GET("ibm/jobs/{id}/result")
    suspend fun getJobResult(@Path("id") id: String): Response<ApiResponse<IBMQuantumJobDto>>

    @GET("ibm/jobs")
    suspend fun getActiveJobs(): Response<ApiResponse<List<IBMQuantumJobDto>>>

    @GET("ibm/jobs/history")
    suspend fun getJobHistory(): Response<ApiResponse<List<IBMQuantumJobDto>>>
}
