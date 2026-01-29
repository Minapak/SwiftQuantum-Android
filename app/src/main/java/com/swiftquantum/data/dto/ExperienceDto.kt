package com.swiftquantum.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================================================
// Daily Pulse DTOs
// ============================================================================

@Serializable
data class DailyPatternDto(
    @SerialName("amplitude") val amplitude: Double,
    @SerialName("phase") val phase: Double,
    @SerialName("entanglementStrength") val entanglementStrength: Double,
    @SerialName("coherenceTime") val coherenceTime: Double,
    @SerialName("interferencePattern") val interferencePattern: List<Double>,
    @SerialName("luckyQuantumState") val luckyQuantumState: String,
    @SerialName("dateSeed") val dateSeed: String,
    @SerialName("blochCoordinates") val blochCoordinates: BlochCoordinatesDto
)

@Serializable
data class BlochCoordinatesDto(
    @SerialName("x") val x: Double,
    @SerialName("y") val y: Double,
    @SerialName("z") val z: Double
)

// ============================================================================
// Oracle DTOs
// ============================================================================

@Serializable
data class OracleConsultRequest(
    @SerialName("question") val question: String
)

@Serializable
data class OracleResultDto(
    @SerialName("answer") val answer: Boolean,
    @SerialName("confidence") val confidence: Double,
    @SerialName("collapsedCoordinate") val collapsedCoordinate: Double,
    @SerialName("question") val question: String,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("quantumState") val quantumState: String,
    @SerialName("answerString") val answerString: String,
    @SerialName("confidencePercentage") val confidencePercentage: Double,
    @SerialName("remaining_tokens") val remainingTokens: Int? = null
)

@Serializable
data class OracleStatisticsRequest(
    @SerialName("question") val question: String,
    @SerialName("shots") val shots: Int = 100
)

@Serializable
data class OracleStatisticsDto(
    @SerialName("question") val question: String,
    @SerialName("totalShots") val totalShots: Int,
    @SerialName("yesCount") val yesCount: Int,
    @SerialName("noCount") val noCount: Int,
    @SerialName("averageConfidence") val averageConfidence: Double,
    @SerialName("yesPercentage") val yesPercentage: Double,
    @SerialName("noPercentage") val noPercentage: Double
)

// ============================================================================
// Art DTOs
// ============================================================================

@Serializable
data class QubitStateRequest(
    @SerialName("amplitude_0_real") val amplitude0Real: Double,
    @SerialName("amplitude_0_imag") val amplitude0Imag: Double,
    @SerialName("amplitude_1_real") val amplitude1Real: Double,
    @SerialName("amplitude_1_imag") val amplitude1Imag: Double,
    @SerialName("config") val config: ArtMappingConfigDto? = null
)

@Serializable
data class ArtMappingConfigDto(
    @SerialName("max_complexity") val maxComplexity: Int = 10,
    @SerialName("min_complexity") val minComplexity: Int = 1,
    @SerialName("hue_mode") val hueMode: String = "phase",
    @SerialName("contrast_sensitivity") val contrastSensitivity: Double = 1.0
)

@Serializable
data class ArtDataDto(
    @SerialName("primaryHue") val primaryHue: Double,
    @SerialName("complexity") val complexity: Int,
    @SerialName("contrast") val contrast: Double,
    @SerialName("saturation") val saturation: Double,
    @SerialName("brightness") val brightness: Double,
    @SerialName("quantumSignature") val quantumSignature: String,
    @SerialName("hueDegrees") val hueDegrees: Double,
    @SerialName("hexColor") val hexColor: String,
    @SerialName("rgbColor") val rgbColor: RgbColorDto
)

@Serializable
data class RgbColorDto(
    @SerialName("r") val r: Int,
    @SerialName("g") val g: Int,
    @SerialName("b") val b: Int
)

@Serializable
data class ArtGenerateResponseDto(
    @SerialName("success") val success: Boolean,
    @SerialName("resolution") val resolution: String,
    @SerialName("watermark") val watermark: Boolean,
    @SerialName("tier") val tier: String,
    @SerialName("message") val message: String
)

// ============================================================================
// Combined Experience DTOs
// ============================================================================

@Serializable
data class DailyExperienceDto(
    @SerialName("pattern") val pattern: DailyPatternDto,
    @SerialName("art") val art: ArtDataDto,
    @SerialName("fortune") val fortune: OracleResultDto,
    @SerialName("generatedAt") val generatedAt: String
)

@Serializable
data class PersonalSignatureRequest(
    @SerialName("user_identifier") val userIdentifier: String
)

@Serializable
data class PersonalSignatureDto(
    @SerialName("userIdentifier") val userIdentifier: String,
    @SerialName("artParameters") val artParameters: ArtDataDto,
    @SerialName("dailyAlignment") val dailyAlignment: Double,
    @SerialName("blochCoordinates") val blochCoordinates: BlochCoordinatesDto,
    @SerialName("quantumStateDescription") val quantumStateDescription: String
)
