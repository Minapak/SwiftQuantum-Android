package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.domain.model.BlochSphereState
import com.swiftquantum.domain.model.ComplexNumber
import com.swiftquantum.domain.model.ExecutionResult
import com.swiftquantum.presentation.ui.component.BlochSphere
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumOrange
import com.swiftquantum.presentation.ui.theme.QuantumPink
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel
import kotlin.math.PI

enum class VisualizationTab {
    BLOCH_SPHERE,
    HISTOGRAM,
    STATE_VECTOR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizeScreen(
    onNavigateBack: () -> Unit,
    viewModel: SimulatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(VisualizationTab.BLOCH_SPHERE) }
    var selectedQubit by remember { mutableIntStateOf(0) }

    val hasVisualizationData = uiState.lastResult != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.visualize_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Selection
            VisualizationTabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            if (!hasVisualizationData) {
                // Empty State
                EmptyVisualizationState()
            } else {
                // Tab Content
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                                (slideOutHorizontally { -it } + fadeOut())
                    },
                    label = "tab_content"
                ) { tab ->
                    when (tab) {
                        VisualizationTab.BLOCH_SPHERE -> {
                            BlochSphereTab(
                                result = uiState.lastResult,
                                selectedQubit = selectedQubit,
                                numQubits = uiState.numQubits,
                                onQubitSelected = { selectedQubit = it }
                            )
                        }
                        VisualizationTab.HISTOGRAM -> {
                            HistogramTab(result = uiState.lastResult)
                        }
                        VisualizationTab.STATE_VECTOR -> {
                            StateVectorTab(result = uiState.lastResult)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VisualizationTabBar(
    selectedTab: VisualizationTab,
    onTabSelected: (VisualizationTab) -> Unit
) {
    val tabs = listOf(
        Triple(VisualizationTab.BLOCH_SPHERE, Icons.Default.Public, R.string.visualize_bloch_sphere),
        Triple(VisualizationTab.HISTOGRAM, Icons.Default.BarChart, R.string.histogram),
        Triple(VisualizationTab.STATE_VECTOR, Icons.Default.Functions, R.string.state_vector)
    )

    TabRow(
        selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = QuantumPurple,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabs.indexOfFirst { it.first == selectedTab }])
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                    .background(QuantumPurple)
            )
        }
    ) {
        tabs.forEach { (tab, icon, titleRes) ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(titleRes),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                selectedContentColor = QuantumPurple,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyVisualizationState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(QuantumPurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QuestionMark,
                    contentDescription = null,
                    tint = QuantumPurple,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.visualize_no_data),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.visualize_run_simulation),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
        }
    }
}

@Composable
private fun BlochSphereTab(
    result: ExecutionResult?,
    selectedQubit: Int,
    numQubits: Int,
    onQubitSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Qubit Selector
        item {
            Text(
                text = stringResource(R.string.visualize_select_qubit),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(numQubits) { qubitIndex ->
                    FilterChip(
                        selected = selectedQubit == qubitIndex,
                        onClick = { onQubitSelected(qubitIndex) },
                        label = {
                            Text(
                                text = "Q$qubitIndex",
                                fontWeight = if (selectedQubit == qubitIndex) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuantumPurple,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Bloch Sphere Visualization
        item {
            val blochState = remember(result, selectedQubit) {
                calculateBlochStateForQubit(result, selectedQubit)
            }

            BlochSphere(
                state = blochState,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Qubit State Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.visualize_qubit_state, selectedQubit),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val blochState = calculateBlochStateForQubit(result, selectedQubit)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StateInfoItem(
                            label = "X",
                            value = String.format("%.4f", blochState.x),
                            color = Color(0xFFEF4444)
                        )
                        StateInfoItem(
                            label = "Y",
                            value = String.format("%.4f", blochState.y),
                            color = Color(0xFF22C55E)
                        )
                        StateInfoItem(
                            label = "Z",
                            value = String.format("%.4f", blochState.z),
                            color = Color(0xFF3B82F6)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun StateInfoItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun HistogramTab(result: ExecutionResult?) {
    if (result == null) {
        EmptyVisualizationState()
        return
    }

    val sortedProbabilities = remember(result) {
        result.probabilities.entries
            .sortedByDescending { it.value }
            .take(16)
    }

    val maxProbability = sortedProbabilities.maxOfOrNull { it.value } ?: 1.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.visualize_top_states, sortedProbabilities.size),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        itemsIndexed(sortedProbabilities) { index, (state, probability) ->
            val count = result.counts[state] ?: 0
            val animatedProgress by animateFloatAsState(
                targetValue = (probability / maxProbability).toFloat(),
                animationSpec = tween(durationMillis = 500, delayMillis = index * 50),
                label = "bar_$state"
            )

            HistogramBarItem(
                state = state,
                probability = probability,
                count = count,
                progress = animatedProgress,
                index = index
            )
        }

        if (result.probabilities.size > 16) {
            item {
                Text(
                    text = stringResource(R.string.visualize_showing_top, result.probabilities.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun HistogramBarItem(
    state: String,
    probability: Double,
    count: Int,
    progress: Float,
    index: Int
) {
    val barColors = listOf(
        QuantumPurple, QuantumCyan, QuantumGreen, QuantumOrange,
        QuantumPink, Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899)
    )
    val barColor = barColors[index % barColors.size]

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // State Label
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = barColor.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "|$state\u27E9",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = barColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(barColor, barColor.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Probability and Count
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.2f%%", probability * 100),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StateVectorTab(result: ExecutionResult?) {
    if (result?.stateVector == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Functions,
                    contentDescription = null,
                    tint = QuantumPurple.copy(alpha = 0.5f),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.visualize_state_vector_unavailable),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 48.dp)
                )
            }
        }
        return
    }

    val stateVector = result.stateVector

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.visualize_state_vector_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.visualize_state_vector_desc, stateVector.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.visualize_basis_state),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.visualize_amplitude),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.visualize_probability),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }

        itemsIndexed(stateVector.take(32)) { index, amplitude ->
            val binaryState = index.toString(2).padStart(
                kotlin.math.ceil(kotlin.math.log2(stateVector.size.toDouble())).toInt().coerceAtLeast(1),
                '0'
            )

            StateVectorRow(
                basisState = binaryState,
                amplitude = amplitude,
                index = index
            )
        }

        if (stateVector.size > 32) {
            item {
                Text(
                    text = stringResource(R.string.visualize_showing_first, stateVector.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun StateVectorRow(
    basisState: String,
    amplitude: ComplexNumber,
    index: Int
) {
    val probability = amplitude.probability
    val backgroundColor = if (probability > 0.01) {
        QuantumPurple.copy(alpha = (probability * 0.3f).toFloat().coerceIn(0.05f, 0.3f))
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Basis State
        Text(
            text = "|$basisState\u27E9",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // Amplitude
        Text(
            text = amplitude.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = if (probability > 0.01) QuantumCyan else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )

        // Probability
        Text(
            text = String.format("%.4f", probability),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (probability > 0.01) FontWeight.Bold else FontWeight.Normal,
            color = if (probability > 0.01) QuantumGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

private fun calculateBlochStateForQubit(result: ExecutionResult?, qubitIndex: Int): BlochSphereState {
    if (result?.stateVector == null || result.stateVector.isEmpty()) {
        return BlochSphereState.ZERO
    }

    // For single qubit visualization, calculate the reduced density matrix
    // and extract the Bloch sphere coordinates
    val stateVector = result.stateVector
    val numQubits = kotlin.math.ceil(kotlin.math.log2(stateVector.size.toDouble())).toInt().coerceAtLeast(1)

    if (qubitIndex >= numQubits) {
        return BlochSphereState.ZERO
    }

    // Simple approximation: calculate expectation values
    var z = 0.0
    var x = 0.0
    var y = 0.0

    for (i in stateVector.indices) {
        val prob = stateVector[i].probability
        val bit = (i shr (numQubits - 1 - qubitIndex)) and 1
        z += if (bit == 0) prob else -prob
    }

    // For x and y, we need to consider coherences
    // This is a simplified calculation
    val theta = kotlin.math.acos(z.coerceIn(-1.0, 1.0))
    val phi = 0.0 // Simplified - would need full state tomography for accurate phi

    return BlochSphereState(theta, phi)
}
