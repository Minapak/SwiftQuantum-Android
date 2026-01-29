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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
                Timber.w("Failed to get today's pattern from API, returning default")
                Result.success(getDefaultDailyPattern())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get today's pattern, returning default")
            Result.success(getDefaultDailyPattern())
        }
    }

    override suspend fun getPatternByDate(dateSeed: String): Result<DailyPattern> {
        return try {
            val response = experienceApi.getPatternByDate(dateSeed)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Timber.w("Failed to get pattern by date from API, returning default")
                Result.success(getDefaultDailyPattern(dateSeed))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get pattern by date, returning default")
            Result.success(getDefaultDailyPattern(dateSeed))
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
                Timber.w("Failed to consult oracle from API, returning default")
                Result.success(getDefaultOracleResult(question))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to consult oracle, returning default")
            Result.success(getDefaultOracleResult(question))
        }
    }

    override suspend fun quickOracle(question: String): Result<OracleResult> {
        return try {
            val response = experienceApi.quickOracle(question)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Timber.w("Failed to get quick oracle from API, returning default")
                Result.success(getDefaultOracleResult(question))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get quick oracle, returning default")
            Result.success(getDefaultOracleResult(question))
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
                Timber.w("Failed to get oracle statistics from API, returning default")
                Result.success(getDefaultOracleStatistics(question, shots))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get oracle statistics, returning default")
            Result.success(getDefaultOracleStatistics(question, shots))
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
                Timber.w("Failed to get art from qubit from API, returning default")
                Result.success(getDefaultQuantumArtData())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get art from qubit, returning default")
            Result.success(getDefaultQuantumArtData())
        }
    }

    override suspend fun getArtFromSuperposition(): Result<QuantumArtData> {
        return try {
            val response = experienceApi.artFromSuperposition()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(dto.toDomain())
            } else {
                Timber.w("Failed to get art from superposition from API, returning default")
                Result.success(getDefaultQuantumArtData())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get art from superposition, returning default")
            Result.success(getDefaultQuantumArtData())
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
                Timber.w("Failed to generate art from API, returning default")
                Result.success(getDefaultArtGenerateResult(resolution))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate art, returning default")
            Result.success(getDefaultArtGenerateResult(resolution))
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
                Timber.w("Failed to get daily experience from API, returning default")
                Result.success(getDefaultDailyExperience())
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get daily experience, returning default")
            Result.success(getDefaultDailyExperience())
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
                Timber.w("Failed to create personal signature from API, returning default")
                Result.success(getDefaultPersonalSignature(userIdentifier))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to create personal signature, returning default")
            Result.success(getDefaultPersonalSignature(userIdentifier))
        }
    }

    // ============================================================================
    // Default/Fallback Data Generators
    // ============================================================================

    private fun getDefaultDailyPattern(dateSeed: String? = null): DailyPattern {
        val seed = dateSeed ?: SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val seedHash = seed.hashCode()
        val random = Random(seedHash.toLong())

        val amplitude = 0.5 + random.nextDouble() * 0.5
        val phase = random.nextDouble() * 2 * Math.PI

        return DailyPattern(
            amplitude = amplitude,
            phase = phase,
            entanglementStrength = random.nextDouble(),
            coherenceTime = 50.0 + random.nextDouble() * 50.0,
            interferencePattern = List(10) { random.nextDouble() },
            luckyQuantumState = if (random.nextBoolean()) "|0>" else "|1>",
            dateSeed = seed,
            blochCoordinates = BlochCoordinates(
                x = sin(phase) * amplitude,
                y = cos(phase) * amplitude,
                z = 1.0 - 2.0 * amplitude * amplitude
            )
        )
    }

    private fun getDefaultOracleResult(question: String): OracleResult {
        val random = Random(question.hashCode().toLong() + System.currentTimeMillis())
        val answer = random.nextBoolean()
        val confidence = 0.5 + random.nextDouble() * 0.5

        return OracleResult(
            answer = answer,
            confidence = confidence,
            collapsedCoordinate = random.nextDouble(),
            question = question,
            timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date()),
            quantumState = if (answer) "|1>" else "|0>",
            answerString = if (answer) "Yes" else "No",
            confidencePercentage = confidence * 100,
            remainingTokens = null
        )
    }

    private fun getDefaultOracleStatistics(question: String, shots: Int): OracleStatistics {
        val random = Random(question.hashCode().toLong())
        val yesCount = random.nextInt(shots)
        val noCount = shots - yesCount

        return OracleStatistics(
            question = question,
            totalShots = shots,
            yesCount = yesCount,
            noCount = noCount,
            averageConfidence = 0.5 + random.nextDouble() * 0.3,
            yesPercentage = yesCount.toDouble() / shots * 100,
            noPercentage = noCount.toDouble() / shots * 100
        )
    }

    private fun getDefaultQuantumArtData(): QuantumArtData {
        val random = Random(System.currentTimeMillis())
        val hue = random.nextDouble() * 360
        val r = (128 + random.nextInt(128))
        val g = (128 + random.nextInt(128))
        val b = (128 + random.nextInt(128))

        return QuantumArtData(
            primaryHue = hue / 360.0,
            complexity = random.nextInt(10) + 1,
            contrast = 0.5 + random.nextDouble() * 0.5,
            saturation = 0.5 + random.nextDouble() * 0.5,
            brightness = 0.5 + random.nextDouble() * 0.5,
            quantumSignature = "local_${System.currentTimeMillis().toString(16)}",
            hueDegrees = hue,
            hexColor = String.format("#%02X%02X%02X", r, g, b),
            rgbColor = RgbColor(r, g, b)
        )
    }

    private fun getDefaultArtGenerateResult(resolution: String): ArtGenerateResult {
        return ArtGenerateResult(
            success = true,
            resolution = resolution,
            watermark = true,
            tier = "free",
            message = "Generated locally (offline mode)"
        )
    }

    private fun getDefaultDailyExperience(): DailyExperience {
        return DailyExperience(
            pattern = getDefaultDailyPattern(),
            art = getDefaultQuantumArtData(),
            fortune = getDefaultOracleResult("What does today hold?"),
            generatedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date())
        )
    }

    private fun getDefaultPersonalSignature(userIdentifier: String): PersonalSignature {
        val random = Random(userIdentifier.hashCode().toLong())
        val phase = random.nextDouble() * 2 * Math.PI
        val amplitude = 0.5 + random.nextDouble() * 0.5

        return PersonalSignature(
            userIdentifier = userIdentifier,
            artParameters = getDefaultQuantumArtData(),
            dailyAlignment = random.nextDouble(),
            blochCoordinates = BlochCoordinates(
                x = sin(phase) * amplitude,
                y = cos(phase) * amplitude,
                z = 1.0 - 2.0 * amplitude * amplitude
            ),
            quantumStateDescription = "Locally generated quantum signature"
        )
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
