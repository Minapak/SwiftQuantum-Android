package com.swiftquantum.data.repository

import com.swiftquantum.data.api.ExperienceApi
import com.swiftquantum.data.dto.OracleConsultRequest
import com.swiftquantum.data.dto.OracleStatisticsRequest
import com.swiftquantum.data.dto.PersonalSignatureRequest
import com.swiftquantum.data.dto.QubitStateRequest
import com.swiftquantum.domain.model.ArtGenerateResult
import com.swiftquantum.domain.model.BlochCoordinates
import com.swiftquantum.domain.model.DailyExperience
import com.swiftquantum.domain.model.DailyPattern
import com.swiftquantum.domain.model.OracleResult
import com.swiftquantum.domain.model.OracleStatistics
import com.swiftquantum.domain.model.PersonalSignature
import com.swiftquantum.domain.model.QuantumArtData
import com.swiftquantum.domain.model.RgbColor
import com.swiftquantum.domain.repository.ExperienceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExperienceRepositoryImpl @Inject constructor(
    private val experienceApi: ExperienceApi
) : ExperienceRepository {

    // ============================================================================
    // Daily Pulse
    // ============================================================================

    override suspend fun getTodayPattern(): Result<DailyPattern> {
        return try {
            val response = experienceApi.getTodayPattern()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPatternByDate(dateSeed: String): Result<DailyPattern> {
        return try {
            val response = experienceApi.getPatternByDate(dateSeed)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================================================
    // Oracle
    // ============================================================================

    override suspend fun consultOracle(question: String): Result<OracleResult> {
        return try {
            val response = experienceApi.consultOracle(OracleConsultRequest(question))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun quickOracle(question: String): Result<OracleResult> {
        return try {
            val response = experienceApi.quickOracle(question)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOracleStatistics(question: String, shots: Int): Result<OracleStatistics> {
        return try {
            val response = experienceApi.oracleStatistics(OracleStatisticsRequest(question, shots))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    OracleStatistics(
                        question = dto.question,
                        totalShots = dto.totalShots,
                        yesCount = dto.yesCount,
                        noCount = dto.noCount,
                        averageConfidence = dto.averageConfidence,
                        yesPercentage = dto.yesPercentage,
                        noPercentage = dto.noPercentage
                    )
                )
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================================================
    // Art
    // ============================================================================

    override suspend fun getArtFromQubit(
        amplitude0Real: Double,
        amplitude0Imag: Double,
        amplitude1Real: Double,
        amplitude1Imag: Double
    ): Result<QuantumArtData> {
        return try {
            val response = experienceApi.artFromQubit(
                QubitStateRequest(
                    amplitude0Real = amplitude0Real,
                    amplitude0Imag = amplitude0Imag,
                    amplitude1Real = amplitude1Real,
                    amplitude1Imag = amplitude1Imag
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtFromSuperposition(): Result<QuantumArtData> {
        return try {
            val response = experienceApi.artFromSuperposition()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateArt(resolution: String): Result<ArtGenerateResult> {
        return try {
            val response = experienceApi.generateArt(resolution)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    ArtGenerateResult(
                        success = dto.success,
                        resolution = dto.resolution,
                        watermark = dto.watermark,
                        tier = dto.tier,
                        message = dto.message
                    )
                )
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================================================
    // Combined Experience
    // ============================================================================

    override suspend fun getDailyExperience(): Result<DailyExperience> {
        return try {
            val response = experienceApi.getDailyExperience()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    DailyExperience(
                        pattern = dto.pattern.toDomain(),
                        art = dto.art.toDomain(),
                        fortune = dto.fortune.toDomain(),
                        generatedAt = dto.generatedAt
                    )
                )
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPersonalSignature(userIdentifier: String): Result<PersonalSignature> {
        return try {
            val response = experienceApi.createPersonalSignature(
                PersonalSignatureRequest(userIdentifier)
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    PersonalSignature(
                        userIdentifier = dto.userIdentifier,
                        artParameters = dto.artParameters.toDomain(),
                        dailyAlignment = dto.dailyAlignment,
                        blochCoordinates = BlochCoordinates(
                            x = dto.blochCoordinates.x,
                            y = dto.blochCoordinates.y,
                            z = dto.blochCoordinates.z
                        ),
                        quantumStateDescription = dto.quantumStateDescription
                    )
                )
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ============================================================================
// Extension Functions for DTO to Domain Mapping
// ============================================================================

private fun com.swiftquantum.data.dto.DailyPatternDto.toDomain() = DailyPattern(
    amplitude = amplitude,
    phase = phase,
    entanglementStrength = entanglementStrength,
    coherenceTime = coherenceTime,
    interferencePattern = interferencePattern,
    luckyQuantumState = luckyQuantumState,
    dateSeed = dateSeed,
    blochCoordinates = BlochCoordinates(
        x = blochCoordinates.x,
        y = blochCoordinates.y,
        z = blochCoordinates.z
    )
)

private fun com.swiftquantum.data.dto.OracleResultDto.toDomain() = OracleResult(
    answer = answer,
    confidence = confidence,
    collapsedCoordinate = collapsedCoordinate,
    question = question,
    timestamp = timestamp,
    quantumState = quantumState,
    answerString = answerString,
    confidencePercentage = confidencePercentage,
    remainingTokens = remainingTokens
)

private fun com.swiftquantum.data.dto.ArtDataDto.toDomain() = QuantumArtData(
    primaryHue = primaryHue,
    complexity = complexity,
    contrast = contrast,
    saturation = saturation,
    brightness = brightness,
    quantumSignature = quantumSignature,
    hueDegrees = hueDegrees,
    hexColor = hexColor,
    rgbColor = RgbColor(
        r = rgbColor.r,
        g = rgbColor.g,
        b = rgbColor.b
    )
)
