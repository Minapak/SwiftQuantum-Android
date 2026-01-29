package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.ArtGenerateResult
import com.swiftquantum.domain.model.DailyExperience
import com.swiftquantum.domain.model.DailyPattern
import com.swiftquantum.domain.model.OracleResult
import com.swiftquantum.domain.model.OracleStatistics
import com.swiftquantum.domain.model.PersonalSignature
import com.swiftquantum.domain.model.QuantumArtData

/**
 * Repository for QuantumExperience Entertainment Modules
 */
interface ExperienceRepository {

    // Daily Pulse
    suspend fun getTodayPattern(): Result<DailyPattern>
    suspend fun getPatternByDate(dateSeed: String): Result<DailyPattern>

    // Oracle
    suspend fun consultOracle(question: String): Result<OracleResult>
    suspend fun quickOracle(question: String): Result<OracleResult>
    suspend fun getOracleStatistics(question: String, shots: Int = 100): Result<OracleStatistics>

    // Art
    suspend fun getArtFromQubit(
        amplitude0Real: Double,
        amplitude0Imag: Double,
        amplitude1Real: Double,
        amplitude1Imag: Double
    ): Result<QuantumArtData>
    suspend fun getArtFromSuperposition(): Result<QuantumArtData>
    suspend fun generateArt(resolution: String = "hd"): Result<ArtGenerateResult>

    // Combined Experience
    suspend fun getDailyExperience(): Result<DailyExperience>
    suspend fun createPersonalSignature(userIdentifier: String): Result<PersonalSignature>
}
