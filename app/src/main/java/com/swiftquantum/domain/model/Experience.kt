package com.swiftquantum.domain.model

import androidx.compose.ui.graphics.Color

// ============================================================================
// Daily Pulse Models
// ============================================================================

data class DailyPattern(
    val amplitude: Double,
    val phase: Double,
    val entanglementStrength: Double,
    val coherenceTime: Double,
    val interferencePattern: List<Double>,
    val luckyQuantumState: String,
    val dateSeed: String,
    val blochCoordinates: BlochCoordinates
)

data class BlochCoordinates(
    val x: Double,
    val y: Double,
    val z: Double
)

// ============================================================================
// Oracle Models
// ============================================================================

data class OracleResult(
    val answer: Boolean,
    val confidence: Double,
    val collapsedCoordinate: Double,
    val question: String,
    val timestamp: String,
    val quantumState: String,
    val answerString: String,
    val confidencePercentage: Double,
    val remainingTokens: Int? = null
) {
    val isYes: Boolean get() = answer
    val isNo: Boolean get() = !answer
    val confidenceLevel: ConfidenceLevel get() = when {
        confidencePercentage >= 80 -> ConfidenceLevel.HIGH
        confidencePercentage >= 50 -> ConfidenceLevel.MEDIUM
        else -> ConfidenceLevel.LOW
    }
}

enum class ConfidenceLevel {
    HIGH, MEDIUM, LOW
}

data class OracleStatistics(
    val question: String,
    val totalShots: Int,
    val yesCount: Int,
    val noCount: Int,
    val averageConfidence: Double,
    val yesPercentage: Double,
    val noPercentage: Double
)

// ============================================================================
// Art Models
// ============================================================================

data class QuantumArtData(
    val primaryHue: Double,
    val complexity: Int,
    val contrast: Double,
    val saturation: Double,
    val brightness: Double,
    val quantumSignature: String,
    val hueDegrees: Double,
    val hexColor: String,
    val rgbColor: RgbColor
) {
    val composeColor: Color get() = Color(rgbColor.r, rgbColor.g, rgbColor.b)

    fun getComplexityLevel(): ArtComplexity = when {
        complexity >= 8 -> ArtComplexity.VERY_HIGH
        complexity >= 6 -> ArtComplexity.HIGH
        complexity >= 4 -> ArtComplexity.MEDIUM
        complexity >= 2 -> ArtComplexity.LOW
        else -> ArtComplexity.MINIMAL
    }
}

data class RgbColor(
    val r: Int,
    val g: Int,
    val b: Int
)

enum class ArtComplexity {
    MINIMAL, LOW, MEDIUM, HIGH, VERY_HIGH
}

data class ArtGenerateResult(
    val success: Boolean,
    val resolution: String,
    val watermark: Boolean,
    val tier: String,
    val message: String
)

// ============================================================================
// Combined Experience Models
// ============================================================================

data class DailyExperience(
    val pattern: DailyPattern,
    val art: QuantumArtData,
    val fortune: OracleResult,
    val generatedAt: String
)

data class PersonalSignature(
    val userIdentifier: String,
    val artParameters: QuantumArtData,
    val dailyAlignment: Double,
    val blochCoordinates: BlochCoordinates,
    val quantumStateDescription: String
)

// ============================================================================
// Qubit Puzzle Game Models
// ============================================================================

data class QubitPuzzleState(
    val level: Int,
    val targetState: QubitState,
    val currentState: QubitState,
    val moves: List<GateMove>,
    val maxMoves: Int,
    val score: Int,
    val isComplete: Boolean,
    val stars: Int // 1-3 stars based on moves used
)

data class QubitState(
    val alpha: Complex,
    val beta: Complex
) {
    companion object {
        val ZERO = QubitState(Complex(1.0, 0.0), Complex(0.0, 0.0))
        val ONE = QubitState(Complex(0.0, 0.0), Complex(1.0, 0.0))
        val PLUS = QubitState(
            Complex(1.0 / kotlin.math.sqrt(2.0), 0.0),
            Complex(1.0 / kotlin.math.sqrt(2.0), 0.0)
        )
        val MINUS = QubitState(
            Complex(1.0 / kotlin.math.sqrt(2.0), 0.0),
            Complex(-1.0 / kotlin.math.sqrt(2.0), 0.0)
        )
    }

    fun probability0(): Double = alpha.magnitude() * alpha.magnitude()
    fun probability1(): Double = beta.magnitude() * beta.magnitude()

    fun isCloseTo(other: QubitState, tolerance: Double = 0.01): Boolean {
        return kotlin.math.abs(probability0() - other.probability0()) < tolerance &&
                kotlin.math.abs(probability1() - other.probability1()) < tolerance
    }
}

data class Complex(
    val real: Double,
    val imag: Double
) {
    fun magnitude(): Double = kotlin.math.sqrt(real * real + imag * imag)
    fun phase(): Double = kotlin.math.atan2(imag, real)

    operator fun plus(other: Complex) = Complex(real + other.real, imag + other.imag)
    operator fun minus(other: Complex) = Complex(real - other.real, imag - other.imag)
    operator fun times(other: Complex) = Complex(
        real * other.real - imag * other.imag,
        real * other.imag + imag * other.real
    )
    operator fun times(scalar: Double) = Complex(real * scalar, imag * scalar)
}

data class GateMove(
    val gate: PuzzleGate,
    val timestamp: Long
)

enum class PuzzleGate(val symbol: String, val description: String) {
    H("H", "Hadamard"),
    X("X", "Pauli-X"),
    Y("Y", "Pauli-Y"),
    Z("Z", "Pauli-Z"),
    S("S", "S Gate"),
    T("T", "T Gate")
}

data class PuzzleLevel(
    val levelNumber: Int,
    val name: String,
    val description: String,
    val initialState: QubitState,
    val targetState: QubitState,
    val allowedGates: List<PuzzleGate>,
    val maxMoves: Int,
    val difficulty: PuzzleDifficulty
)

enum class PuzzleDifficulty {
    BEGINNER, EASY, MEDIUM, HARD, EXPERT
}
