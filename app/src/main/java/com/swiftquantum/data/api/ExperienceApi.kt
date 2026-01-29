package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.ArtDataDto
import com.swiftquantum.data.dto.ArtGenerateResponseDto
import com.swiftquantum.data.dto.DailyExperienceDto
import com.swiftquantum.data.dto.DailyPatternDto
import com.swiftquantum.data.dto.OracleConsultRequest
import com.swiftquantum.data.dto.OracleResultDto
import com.swiftquantum.data.dto.OracleStatisticsRequest
import com.swiftquantum.data.dto.OracleStatisticsDto
import com.swiftquantum.data.dto.PersonalSignatureDto
import com.swiftquantum.data.dto.PersonalSignatureRequest
import com.swiftquantum.data.dto.QubitStateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * QuantumExperience API
 *
 * Entertainment Modules:
 * - Daily Pulse: 일일 양자 패턴
 * - Quantum Oracle: 양자 운세
 * - Quantum Art: 양자 상태 기반 아트
 */
interface ExperienceApi {

    // ============================================================================
    // Daily Pulse Endpoints
    // ============================================================================

    @GET("experience/daily/today")
    suspend fun getTodayPattern(): Response<DailyPatternDto>

    @GET("experience/daily/pattern/{dateSeed}")
    suspend fun getPatternByDate(@Path("dateSeed") dateSeed: String): Response<DailyPatternDto>

    // ============================================================================
    // Oracle Endpoints
    // ============================================================================

    @POST("experience/oracle/consult")
    suspend fun consultOracle(@Body request: OracleConsultRequest): Response<OracleResultDto>

    @GET("experience/oracle/quick")
    suspend fun quickOracle(@Query("question") question: String): Response<OracleResultDto>

    @POST("experience/oracle/statistics")
    suspend fun oracleStatistics(@Body request: OracleStatisticsRequest): Response<OracleStatisticsDto>

    // ============================================================================
    // Art Mapping Endpoints
    // ============================================================================

    @POST("experience/art/from-qubit")
    suspend fun artFromQubit(@Body request: QubitStateRequest): Response<ArtDataDto>

    @GET("experience/art/from-superposition")
    suspend fun artFromSuperposition(): Response<ArtDataDto>

    @POST("experience/art/generate")
    suspend fun generateArt(@Query("resolution") resolution: String = "hd"): Response<ArtGenerateResponseDto>

    // ============================================================================
    // Combined Experience Endpoints
    // ============================================================================

    @GET("experience/combined/daily")
    suspend fun getDailyExperience(): Response<DailyExperienceDto>

    @POST("experience/combined/signature")
    suspend fun createPersonalSignature(@Body request: PersonalSignatureRequest): Response<PersonalSignatureDto>
}
