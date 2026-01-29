package com.swiftquantum.data.dto

import com.google.gson.annotations.SerializedName

// ============================================================================
// Daily Pulse DTOs
// ============================================================================

data class DailyPatternDto(
    @SerializedName("amplitude") val amplitude: Double,
    @SerializedName("phase") val phase: Double,
    @SerializedName("entanglementStrength") val entanglementStrength: Double,
    @SerializedName("coherenceTime") val coherenceTime: Double,
    @SerializedName("interferencePattern") val interferencePattern: List<Double>,
    @SerializedName("luckyQuantumState") val luckyQuantumState: String,
    @SerializedName("dateSeed") val dateSeed: String,
    @SerializedName("blochCoordinates") val blochCoordinates: BlochCoordinatesDto
)

data class BlochCoordinatesDto(
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double,
    @SerializedName("z") val z: Double
)

// ============================================================================
// Oracle DTOs
// ============================================================================

data class OracleConsultRequest(
    @SerializedName("question") val question: String
)

data class OracleResultDto(
    @SerializedName("answer") val answer: Boolean,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("collapsedCoordinate") val collapsedCoordinate: Double,
    @SerializedName("question") val question: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("quantumState") val quantumState: String,
    @SerializedName("answerString") val answerString: String,
    @SerializedName("confidencePercentage") val confidencePercentage: Double,
    @SerializedName("remaining_tokens") val remainingTokens: Int? = null
)

data class OracleStatisticsRequest(
    @SerializedName("question") val question: String,
    @SerializedName("shots") val shots: Int = 100
)

data class OracleStatisticsDto(
    @SerializedName("question") val question: String,
    @SerializedName("totalShots") val totalShots: Int,
    @SerializedName("yesCount") val yesCount: Int,
    @SerializedName("noCount") val noCount: Int,
    @SerializedName("averageConfidence") val averageConfidence: Double,
    @SerializedName("yesPercentage") val yesPercentage: Double,
    @SerializedName("noPercentage") val noPercentage: Double
)

// ============================================================================
// Art DTOs
// ============================================================================

data class QubitStateRequest(
    @SerializedName("amplitude_0_real") val amplitude0Real: Double,
    @SerializedName("amplitude_0_imag") val amplitude0Imag: Double,
    @SerializedName("amplitude_1_real") val amplitude1Real: Double,
    @SerializedName("amplitude_1_imag") val amplitude1Imag: Double,
    @SerializedName("config") val config: ArtMappingConfigDto? = null
)

data class ArtMappingConfigDto(
    @SerializedName("max_complexity") val maxComplexity: Int = 10,
    @SerializedName("min_complexity") val minComplexity: Int = 1,
    @SerializedName("hue_mode") val hueMode: String = "phase",
    @SerializedName("contrast_sensitivity") val contrastSensitivity: Double = 1.0
)

data class ArtDataDto(
    @SerializedName("primaryHue") val primaryHue: Double,
    @SerializedName("complexity") val complexity: Int,
    @SerializedName("contrast") val contrast: Double,
    @SerializedName("saturation") val saturation: Double,
    @SerializedName("brightness") val brightness: Double,
    @SerializedName("quantumSignature") val quantumSignature: String,
    @SerializedName("hueDegrees") val hueDegrees: Double,
    @SerializedName("hexColor") val hexColor: String,
    @SerializedName("rgbColor") val rgbColor: RgbColorDto
)

data class RgbColorDto(
    @SerializedName("r") val r: Int,
    @SerializedName("g") val g: Int,
    @SerializedName("b") val b: Int
)

data class ArtGenerateResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("resolution") val resolution: String,
    @SerializedName("watermark") val watermark: Boolean,
    @SerializedName("tier") val tier: String,
    @SerializedName("message") val message: String
)

// ============================================================================
// Combined Experience DTOs
// ============================================================================

data class DailyExperienceDto(
    @SerializedName("pattern") val pattern: DailyPatternDto,
    @SerializedName("art") val art: ArtDataDto,
    @SerializedName("fortune") val fortune: OracleResultDto,
    @SerializedName("generatedAt") val generatedAt: String
)

data class PersonalSignatureRequest(
    @SerializedName("user_identifier") val userIdentifier: String
)

data class PersonalSignatureDto(
    @SerializedName("userIdentifier") val userIdentifier: String,
    @SerializedName("artParameters") val artParameters: ArtDataDto,
    @SerializedName("dailyAlignment") val dailyAlignment: Double,
    @SerializedName("blochCoordinates") val blochCoordinates: BlochCoordinatesDto,
    @SerializedName("quantumStateDescription") val quantumStateDescription: String
)
