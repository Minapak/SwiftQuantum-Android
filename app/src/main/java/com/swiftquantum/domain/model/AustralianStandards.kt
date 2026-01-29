package com.swiftquantum.domain.model

/**
 * Australian Quantum Computing Standards v5.2.0
 *
 * Integration with Australian university quantum computing frameworks:
 * - Q-CTRL (University of Sydney): Error suppression
 * - MicroQiskit (UTS): Mobile optimization
 * - SQC (UNSW Sydney): Fidelity benchmarking
 * - LabScript (Monash/Melbourne): Hardware protocols
 */

// =============================================================================
// Q-CTRL Error Suppression (University of Sydney)
// =============================================================================

enum class DecouplingSequence(val value: String, val displayName: String) {
    NONE("none", "None"),
    CPMG("cpmg", "CPMG"),
    XY4("xy4", "XY-4"),
    XY8("xy8", "XY-8"),
    UDD("udd", "UDD")
}

data class QCTRLConfig(
    val enableDD: Boolean = true,
    val ddSequence: DecouplingSequence = DecouplingSequence.XY4,
    val enableRobustControl: Boolean = true
)

data class FidelityImprovement(
    val originalFidelity: Double,
    val optimizedFidelity: Double,
    val improvementFactor: Double
)

data class QCTRLResult(
    val success: Boolean,
    val fidelityImprovement: FidelityImprovement?,
    val ddInsertions: Int,
    val optimizedGates: List<Map<String, Any>>? = null,
    val technique: String = "Q-CTRL Open Controls",
    val reference: String = "University of Sydney"
)

// =============================================================================
// MicroQiskit Mobile Optimization (UTS)
// =============================================================================

enum class OptimizationLevel(val value: String, val displayName: String) {
    NONE("none", "None"),
    BASIC("basic", "Basic"),
    MODERATE("moderate", "Moderate"),
    AGGRESSIVE("aggressive", "Aggressive")
}

data class MobileOptimizationConfig(
    val level: OptimizationLevel = OptimizationLevel.MODERATE,
    val enableFidelityBenchmark: Boolean = true
)

data class OptimizationStats(
    val originalGates: Int,
    val optimizedGates: Int,
    val reductionPercentage: Double,
    val estimatedMemoryMb: Double,
    val estimatedTimeMs: Double,
    val transformationsApplied: List<String>
)

data class MobileCompatibility(
    val compatible: Boolean,
    val numQubits: Int,
    val numGates: Int,
    val estimatedMemoryMb: Double,
    val estimatedTimeMs: Double,
    val issues: List<String>,
    val recommendations: List<String>
)

data class MicroQiskitResult(
    val success: Boolean,
    val optimization: OptimizationStats?,
    val compatibility: MobileCompatibility? = null,
    val technique: String = "MicroQiskit Mobile Optimization",
    val reference: String = "University of Technology Sydney"
)

// =============================================================================
// SQC Fidelity Benchmarking (UNSW Sydney)
// =============================================================================

enum class FidelityGrade(val value: String, val displayName: String, val threshold: Double) {
    PLATINUM("platinum", "Platinum", 0.999),
    GOLD("gold", "Gold", 0.995),
    SILVER("silver", "Silver", 0.990),
    BRONZE("bronze", "Bronze", 0.980),
    STANDARD("standard", "Standard", 0.950),
    DEVELOPING("developing", "Developing", 0.0);

    companion object {
        fun fromFidelity(fidelity: Double): FidelityGrade {
            return values().firstOrNull { fidelity >= it.threshold } ?: DEVELOPING
        }
    }
}

data class GateFidelityResult(
    val gateType: String,
    val targetQubits: List<Int>,
    val fidelity: Double,
    val errorRate: Double,
    val confidenceInterval: Pair<Double, Double>,
    val numShots: Int,
    val benchmarkType: String
)

data class SQCComparison(
    val singleQubit: SQCQubitComparison?,
    val twoQubit: SQCQubitComparison?,
    val sqcReference: SQCReference
)

data class SQCQubitComparison(
    val yourAverage: Double,
    val sqcBenchmark: Double,
    val gap: Double,
    val percentageOfSQC: Double,
    val meetsSQCStandard: Boolean
)

data class SQCReference(
    val institution: String = "Silicon Quantum Computing Pty Ltd",
    val location: String = "UNSW Sydney, Australia",
    val website: String = "https://sqc.com.au/",
    val keyAchievement: String = "World's first integrated circuit manufactured at atomic scale"
)

data class SQCBenchmarkResult(
    val circuitName: String,
    val numQubits: Int,
    val numGates: Int,
    val gateFidelities: List<GateFidelityResult>,
    val overallFidelity: Double,
    val grade: FidelityGrade,
    val sqcComparison: SQCComparison,
    val recommendations: List<String>,
    val benchmarkHash: String
)

data class SQCGradeBadge(
    val name: String,
    val icon: String,
    val color: String,
    val description: String,
    val threshold: String
)

// =============================================================================
// LabScript Protocol (Monash/Melbourne)
// =============================================================================

data class LabScriptChannel(
    val name: String,
    val channelType: String,
    val device: String,
    val connection: String,
    val defaultValue: Double = 0.0,
    val units: String = "V"
)

data class TimingInstruction(
    val time: Double,
    val channel: String,
    val value: Any,
    val rampTime: Double = 0.0,
    val units: String = "s"
)

data class LabScriptSequence(
    val name: String,
    val description: String,
    val durationSeconds: Double,
    val numChannels: Int,
    val numInstructions: Int,
    val valid: Boolean,
    val validationErrors: List<String>
)

data class LabScriptResult(
    val success: Boolean,
    val sequence: LabScriptSequence?,
    val technique: String = "LabScript Hardware Protocol",
    val reference: String = "Monash/Melbourne Universities"
)

// =============================================================================
// Complete Australian Standards Analysis
// =============================================================================

data class AustralianStandardsAnalysis(
    val circuitName: String,
    val numQubits: Int,
    val qctrl: QCTRLResult?,
    val microqiskit: MicroQiskitResult?,
    val sqc: SQCBenchmarkResult?,
    val labscript: LabScriptResult?,
    val o1VisaRelevance: O1VisaRelevance
)

data class O1VisaRelevance(
    val extraordinaryAbilityEvidence: List<String>,
    val academicCollaborations: List<String>
)

// =============================================================================
// Open Source Credits
// =============================================================================

data class OpenSourceCredit(
    val name: String,
    val institution: String,
    val website: String?,
    val github: String?,
    val license: String,
    val description: String,
    val technique: String
)

object AustralianQuantumCredits {
    val credits = listOf(
        OpenSourceCredit(
            name = "Q-CTRL Open Controls",
            institution = "University of Sydney",
            website = "https://q-ctrl.com/",
            github = "https://github.com/qctrl/open-controls",
            license = "Apache 2.0",
            description = "Quantum control infrastructure for reducing hardware errors",
            technique = "Dynamical Decoupling, Robust Control, Filter Functions"
        ),
        OpenSourceCredit(
            name = "LabScript Suite",
            institution = "Monash University / University of Melbourne",
            website = "https://labscript-suite.org/",
            github = "https://github.com/labscript-suite/labscript",
            license = "BSD-2-Clause",
            description = "Experiment control and automation for quantum physics labs",
            technique = "HDF5 Protocol, Hardware Timing, Shot-based Execution"
        ),
        OpenSourceCredit(
            name = "MicroQiskit",
            institution = "University of Technology Sydney (UTS)",
            website = null,
            github = "https://github.com/qiskit-community/MicroQiskit",
            license = "Apache 2.0",
            description = "Minimal Qiskit implementation for resource-constrained devices",
            technique = "Gate Fusion, Dead Code Elimination, Memory Optimization"
        ),
        OpenSourceCredit(
            name = "SQC Fidelity Standards",
            institution = "UNSW Sydney / Silicon Quantum Computing",
            website = "https://sqc.com.au/",
            github = null,
            license = "Proprietary (Reference Only)",
            description = "World-leading silicon qubit fidelity benchmarks",
            technique = "Randomized Benchmarking, Gate Set Tomography"
        )
    )
}
