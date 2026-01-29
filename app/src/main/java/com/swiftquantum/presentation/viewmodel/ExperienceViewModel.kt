package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.Complex
import com.swiftquantum.domain.model.DailyExperience
import com.swiftquantum.domain.model.DailyPattern
import com.swiftquantum.domain.model.GateMove
import com.swiftquantum.domain.model.OracleResult
import com.swiftquantum.domain.model.OracleStatistics
import com.swiftquantum.domain.model.PersonalSignature
import com.swiftquantum.domain.model.PuzzleDifficulty
import com.swiftquantum.domain.model.PuzzleGate
import com.swiftquantum.domain.model.PuzzleLevel
import com.swiftquantum.domain.model.QuantumArtData
import com.swiftquantum.domain.model.QubitPuzzleState
import com.swiftquantum.domain.model.QubitState
import com.swiftquantum.domain.repository.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository
) : ViewModel() {

    // ============================================================================
    // Daily Pulse State
    // ============================================================================

    private val _dailyPattern = MutableStateFlow<DailyPattern?>(null)
    val dailyPattern: StateFlow<DailyPattern?> = _dailyPattern.asStateFlow()

    private val _dailyExperience = MutableStateFlow<DailyExperience?>(null)
    val dailyExperience: StateFlow<DailyExperience?> = _dailyExperience.asStateFlow()

    // ============================================================================
    // Oracle State
    // ============================================================================

    private val _oracleResult = MutableStateFlow<OracleResult?>(null)
    val oracleResult: StateFlow<OracleResult?> = _oracleResult.asStateFlow()

    private val _oracleStatistics = MutableStateFlow<OracleStatistics?>(null)
    val oracleStatistics: StateFlow<OracleStatistics?> = _oracleStatistics.asStateFlow()

    private val _oracleHistory = MutableStateFlow<List<OracleResult>>(emptyList())
    val oracleHistory: StateFlow<List<OracleResult>> = _oracleHistory.asStateFlow()

    // ============================================================================
    // Art State
    // ============================================================================

    private val _artData = MutableStateFlow<QuantumArtData?>(null)
    val artData: StateFlow<QuantumArtData?> = _artData.asStateFlow()

    private val _personalSignature = MutableStateFlow<PersonalSignature?>(null)
    val personalSignature: StateFlow<PersonalSignature?> = _personalSignature.asStateFlow()

    // ============================================================================
    // Game State
    // ============================================================================

    private val _puzzleState = MutableStateFlow<QubitPuzzleState?>(null)
    val puzzleState: StateFlow<QubitPuzzleState?> = _puzzleState.asStateFlow()

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    // ============================================================================
    // Loading / Error State
    // ============================================================================

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ============================================================================
    // Daily Pulse Actions
    // ============================================================================

    fun loadTodayPattern() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.getTodayPattern()
                .onSuccess { _dailyPattern.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadDailyExperience() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.getDailyExperience()
                .onSuccess { _dailyExperience.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    // ============================================================================
    // Oracle Actions
    // ============================================================================

    fun consultOracle(question: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.consultOracle(question)
                .onSuccess { result ->
                    _oracleResult.value = result
                    _oracleHistory.value = _oracleHistory.value + result
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadOracleStatistics(question: String, shots: Int = 100) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.getOracleStatistics(question, shots)
                .onSuccess { _oracleStatistics.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun clearOracleResult() {
        _oracleResult.value = null
    }

    // ============================================================================
    // Art Actions
    // ============================================================================

    fun loadArtFromCurrentState(
        amplitude0Real: Double,
        amplitude0Imag: Double,
        amplitude1Real: Double,
        amplitude1Imag: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.getArtFromQubit(
                amplitude0Real, amplitude0Imag,
                amplitude1Real, amplitude1Imag
            )
                .onSuccess { _artData.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadArtFromSuperposition() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.getArtFromSuperposition()
                .onSuccess { _artData.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun createPersonalSignature(userIdentifier: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            experienceRepository.createPersonalSignature(userIdentifier)
                .onSuccess { _personalSignature.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    // ============================================================================
    // Qubit Puzzle Game Actions
    // ============================================================================

    fun startPuzzle(level: Int = 1) {
        val puzzleLevel = getPuzzleLevel(level)
        _currentLevel.value = level
        _puzzleState.value = QubitPuzzleState(
            level = level,
            targetState = puzzleLevel.targetState,
            currentState = puzzleLevel.initialState,
            moves = emptyList(),
            maxMoves = puzzleLevel.maxMoves,
            score = 0,
            isComplete = false,
            stars = 0
        )
    }

    fun applyGate(gate: PuzzleGate) {
        val currentState = _puzzleState.value ?: return

        val newQubitState = applyGateToQubit(currentState.currentState, gate)
        val newMoves = currentState.moves + GateMove(gate, System.currentTimeMillis())

        val isComplete = newQubitState.isCloseTo(currentState.targetState)
        val stars = if (isComplete) calculateStars(newMoves.size, currentState.maxMoves) else 0
        val score = if (isComplete) calculateScore(stars, currentState.level) else 0

        _puzzleState.value = currentState.copy(
            currentState = newQubitState,
            moves = newMoves,
            isComplete = isComplete,
            stars = stars,
            score = score
        )

        if (isComplete) {
            _totalScore.value += score
        }
    }

    fun resetPuzzle() {
        startPuzzle(_currentLevel.value)
    }

    fun nextLevel() {
        if (_currentLevel.value < MAX_LEVELS) {
            startPuzzle(_currentLevel.value + 1)
        }
    }

    private fun applyGateToQubit(state: QubitState, gate: PuzzleGate): QubitState {
        val sqrt2Inv = 1.0 / kotlin.math.sqrt(2.0)
        return when (gate) {
            PuzzleGate.H -> {
                // Hadamard gate: H|0⟩ = |+⟩, H|1⟩ = |-⟩
                val newAlpha = (state.alpha + state.beta) * sqrt2Inv
                val newBeta = (state.alpha - state.beta) * sqrt2Inv
                QubitState(newAlpha, newBeta)
            }
            PuzzleGate.X -> {
                // Pauli-X (NOT gate): |0⟩ ↔ |1⟩
                QubitState(state.beta, state.alpha)
            }
            PuzzleGate.Y -> {
                // Pauli-Y: |0⟩ → i|1⟩, |1⟩ → -i|0⟩
                val newAlpha = Complex(-state.beta.imag, state.beta.real)
                val newBeta = Complex(state.alpha.imag, -state.alpha.real)
                QubitState(newAlpha, newBeta)
            }
            PuzzleGate.Z -> {
                // Pauli-Z: |0⟩ → |0⟩, |1⟩ → -|1⟩
                QubitState(state.alpha, state.beta * -1.0)
            }
            PuzzleGate.S -> {
                // S gate: |0⟩ → |0⟩, |1⟩ → i|1⟩
                val newBeta = Complex(-state.beta.imag, state.beta.real)
                QubitState(state.alpha, newBeta)
            }
            PuzzleGate.T -> {
                // T gate: |0⟩ → |0⟩, |1⟩ → e^(iπ/4)|1⟩
                val cos45 = kotlin.math.cos(kotlin.math.PI / 4)
                val sin45 = kotlin.math.sin(kotlin.math.PI / 4)
                val newBeta = Complex(
                    state.beta.real * cos45 - state.beta.imag * sin45,
                    state.beta.real * sin45 + state.beta.imag * cos45
                )
                QubitState(state.alpha, newBeta)
            }
        }
    }

    private fun calculateStars(movesUsed: Int, maxMoves: Int): Int {
        return when {
            movesUsed <= maxMoves / 2 -> 3
            movesUsed <= maxMoves * 3 / 4 -> 2
            movesUsed <= maxMoves -> 1
            else -> 0
        }
    }

    private fun calculateScore(stars: Int, level: Int): Int {
        return stars * 100 * level
    }

    private fun getPuzzleLevel(level: Int): PuzzleLevel {
        return puzzleLevels.getOrNull(level - 1) ?: puzzleLevels.first()
    }

    companion object {
        const val MAX_LEVELS = 20

        val puzzleLevels = listOf(
            // Level 1: |0⟩ → |1⟩ (X gate)
            PuzzleLevel(
                levelNumber = 1,
                name = "First Flip",
                description = "Transform |0⟩ to |1⟩",
                initialState = QubitState.ZERO,
                targetState = QubitState.ONE,
                allowedGates = listOf(PuzzleGate.X),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.BEGINNER
            ),
            // Level 2: |0⟩ → |+⟩ (H gate)
            PuzzleLevel(
                levelNumber = 2,
                name = "Superposition",
                description = "Create superposition |+⟩",
                initialState = QubitState.ZERO,
                targetState = QubitState.PLUS,
                allowedGates = listOf(PuzzleGate.H),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.BEGINNER
            ),
            // Level 3: |1⟩ → |0⟩ (X gate)
            PuzzleLevel(
                levelNumber = 3,
                name = "Flip Back",
                description = "Transform |1⟩ back to |0⟩",
                initialState = QubitState.ONE,
                targetState = QubitState.ZERO,
                allowedGates = listOf(PuzzleGate.X),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.BEGINNER
            ),
            // Level 4: |+⟩ → |0⟩ (H gate)
            PuzzleLevel(
                levelNumber = 4,
                name = "Collapse",
                description = "Collapse superposition to |0⟩",
                initialState = QubitState.PLUS,
                targetState = QubitState.ZERO,
                allowedGates = listOf(PuzzleGate.H),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.EASY
            ),
            // Level 5: |0⟩ → |-⟩ (H then Z)
            PuzzleLevel(
                levelNumber = 5,
                name = "Minus State",
                description = "Create |-⟩ state",
                initialState = QubitState.ZERO,
                targetState = QubitState.MINUS,
                allowedGates = listOf(PuzzleGate.H, PuzzleGate.Z),
                maxMoves = 4,
                difficulty = PuzzleDifficulty.EASY
            ),
            // Level 6-20: More complex puzzles
            PuzzleLevel(
                levelNumber = 6,
                name = "Double Flip",
                description = "Return to original state",
                initialState = QubitState.ZERO,
                targetState = QubitState.ZERO,
                allowedGates = listOf(PuzzleGate.X, PuzzleGate.H),
                maxMoves = 4,
                difficulty = PuzzleDifficulty.EASY
            ),
            PuzzleLevel(
                levelNumber = 7,
                name = "Phase Shift",
                description = "Apply Z gate wisely",
                initialState = QubitState.PLUS,
                targetState = QubitState.MINUS,
                allowedGates = listOf(PuzzleGate.Z),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.MEDIUM
            ),
            PuzzleLevel(
                levelNumber = 8,
                name = "Complex Path",
                description = "Multiple gates needed",
                initialState = QubitState.ZERO,
                targetState = QubitState.ONE,
                allowedGates = listOf(PuzzleGate.H, PuzzleGate.Z, PuzzleGate.X),
                maxMoves = 5,
                difficulty = PuzzleDifficulty.MEDIUM
            ),
            PuzzleLevel(
                levelNumber = 9,
                name = "Y Rotation",
                description = "Use Y gate",
                initialState = QubitState.ZERO,
                targetState = QubitState.ONE,
                allowedGates = listOf(PuzzleGate.Y),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.MEDIUM
            ),
            PuzzleLevel(
                levelNumber = 10,
                name = "S Gate",
                description = "Apply S gate",
                initialState = QubitState.PLUS,
                targetState = QubitState(
                    Complex(1.0 / kotlin.math.sqrt(2.0), 0.0),
                    Complex(0.0, 1.0 / kotlin.math.sqrt(2.0))
                ),
                allowedGates = listOf(PuzzleGate.S),
                maxMoves = 3,
                difficulty = PuzzleDifficulty.MEDIUM
            ),
            // Levels 11-20
            PuzzleLevel(11, "T Gate Intro", "Apply T gate", QubitState.PLUS,
                QubitState(Complex(1.0/kotlin.math.sqrt(2.0), 0.0),
                    Complex(kotlin.math.cos(kotlin.math.PI/4)/kotlin.math.sqrt(2.0),
                        kotlin.math.sin(kotlin.math.PI/4)/kotlin.math.sqrt(2.0))),
                listOf(PuzzleGate.T), 3, PuzzleDifficulty.HARD),
            PuzzleLevel(12, "Combo 1", "H then X", QubitState.ZERO, QubitState.MINUS,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Z), 4, PuzzleDifficulty.HARD),
            PuzzleLevel(13, "Combo 2", "Complex transformation", QubitState.ONE, QubitState.PLUS,
                listOf(PuzzleGate.H, PuzzleGate.X), 4, PuzzleDifficulty.HARD),
            PuzzleLevel(14, "Expert 1", "Find the path", QubitState.MINUS, QubitState.ONE,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Z), 5, PuzzleDifficulty.HARD),
            PuzzleLevel(15, "Expert 2", "Minimal moves", QubitState.ZERO, QubitState.MINUS,
                listOf(PuzzleGate.H, PuzzleGate.Z), 3, PuzzleDifficulty.EXPERT),
            PuzzleLevel(16, "Master 1", "All gates", QubitState.ZERO, QubitState.ONE,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Y, PuzzleGate.Z), 4, PuzzleDifficulty.EXPERT),
            PuzzleLevel(17, "Master 2", "Reverse path", QubitState.MINUS, QubitState.ZERO,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Z), 4, PuzzleDifficulty.EXPERT),
            PuzzleLevel(18, "Master 3", "Phase mastery", QubitState.PLUS, QubitState.ZERO,
                listOf(PuzzleGate.H, PuzzleGate.S, PuzzleGate.T), 4, PuzzleDifficulty.EXPERT),
            PuzzleLevel(19, "Grandmaster 1", "Ultimate test", QubitState.ONE, QubitState.MINUS,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Y, PuzzleGate.Z, PuzzleGate.S), 5, PuzzleDifficulty.EXPERT),
            PuzzleLevel(20, "Grandmaster 2", "Final challenge", QubitState.MINUS, QubitState.PLUS,
                listOf(PuzzleGate.H, PuzzleGate.X, PuzzleGate.Y, PuzzleGate.Z, PuzzleGate.S, PuzzleGate.T), 3, PuzzleDifficulty.EXPERT)
        )
    }
}
