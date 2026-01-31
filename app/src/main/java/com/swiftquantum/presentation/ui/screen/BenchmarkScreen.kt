package com.swiftquantum.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.domain.model.BenchmarkResult
import com.swiftquantum.domain.model.EngineBenchmarkEntry
import com.swiftquantum.domain.model.EngineStatus
import com.swiftquantum.domain.model.HybridEngineType
import com.swiftquantum.domain.model.HybridExecutionResult
import com.swiftquantum.domain.model.OptimizationLevel
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumOrange
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.ui.theme.QuantumRed
import com.swiftquantum.presentation.ui.theme.StatusOnline
import com.swiftquantum.presentation.ui.theme.StatusPending
import com.swiftquantum.presentation.viewmodel.BenchmarkEvent
import com.swiftquantum.presentation.viewmodel.BenchmarkViewModel
import com.swiftquantum.presentation.viewmodel.CircuitPreset
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenchmarkScreen(
    viewModel: BenchmarkViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is BenchmarkEvent.ExecutionComplete -> {
                    Toast.makeText(
                        context,
                        "Execution completed in ${event.result.metrics.formattedExecutionTime}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is BenchmarkEvent.BenchmarkComplete -> {
                    Toast.makeText(
                        context,
                        "Benchmark complete! Fastest: ${event.result.fastestEngine?.displayName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is BenchmarkEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is BenchmarkEvent.TierRequired -> {
                    Toast.makeText(
                        context,
                        "Upgrade your subscription to access this feature",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.text_benchmark_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.text_compare_engine_performance),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadEngineStatus() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Engine Status Cards
            item {
                Text(
                    text = stringResource(R.string.text_engine_status),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isLoadingStatus) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.engineStatuses) { status ->
                            EngineStatusCard(status = status)
                        }
                    }
                }
            }

            // Circuit selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.test_circuit),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(CircuitPreset.entries) { preset ->
                                FilterChip(
                                    selected = uiState.circuit.name == preset.name.replace("_", " "),
                                    onClick = { viewModel.loadPresetCircuit(preset) },
                                    label = { Text(preset.name.replace("_", " ")) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = QuantumPurple.copy(alpha = 0.2f),
                                        selectedLabelColor = QuantumPurple
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Circuit info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CircuitStatItem(
                                label = "Qubits",
                                value = uiState.circuit.numQubits.toString(),
                                color = QuantumPurple
                            )
                            CircuitStatItem(
                                label = "Gates",
                                value = uiState.circuit.gateCount.toString(),
                                color = QuantumCyan
                            )
                            CircuitStatItem(
                                label = "Depth",
                                value = uiState.circuit.depth.toString(),
                                color = QuantumOrange
                            )
                        }
                    }
                }
            }

            // Engine & Optimization selection
            item {
                EngineSelector(
                    selectedEngine = uiState.selectedEngine,
                    selectedOptimization = uiState.selectedOptimization,
                    shots = uiState.shots,
                    userTier = uiState.userTier,
                    onEngineChange = viewModel::setEngine,
                    onOptimizationChange = viewModel::setOptimizationLevel,
                    onShotsChange = viewModel::setShots
                )
            }

            // Action buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.executeWithEngine() },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isExecuting && !uiState.isBenchmarking,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isExecuting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Execute")
                    }

                    OutlinedButton(
                        onClick = { viewModel.runBenchmark() },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isExecuting && !uiState.isBenchmarking &&
                                uiState.userTier != UserTier.FREE,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isBenchmarking) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Timeline, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.benchmark_all))
                    }
                }

                if (uiState.userTier == UserTier.FREE) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.upgrade_pro_benchmark),
                        style = MaterialTheme.typography.bodySmall,
                        color = QuantumOrange,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Execution Result
            uiState.executionResult?.let { result ->
                item {
                    ExecutionResultCard(result = result)
                }
            }

            // Benchmark Result
            uiState.benchmarkResult?.let { result ->
                item {
                    BenchmarkResultCard(result = result)
                }
            }

            // Error display
            uiState.error?.let { error ->
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun EngineStatusCard(status: EngineStatus) {
    val statusColor = when {
        !status.isAvailable -> QuantumRed
        (status.currentLoad ?: 0.0) > 0.8 -> QuantumOrange
        else -> StatusOnline
    }

    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = status.engineType.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = status.statusText,
                style = MaterialTheme.typography.bodySmall,
                color = statusColor
            )

            if (status.isAvailable && status.currentLoad != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Load: ${status.loadPercentage}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${status.engineType.speedupFactor.toInt()}x speedup",
                style = MaterialTheme.typography.labelSmall,
                color = QuantumCyan
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EngineSelector(
    selectedEngine: HybridEngineType,
    selectedOptimization: OptimizationLevel,
    shots: Int,
    userTier: UserTier,
    onEngineChange: (HybridEngineType) -> Unit,
    onOptimizationChange: (OptimizationLevel) -> Unit,
    onShotsChange: (Int) -> Unit
) {
    var showEngineMenu by remember { mutableStateOf(false) }
    var showOptimizationMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.text_execution_settings),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Engine selector
            ExposedDropdownMenuBox(
                expanded = showEngineMenu,
                onExpandedChange = { showEngineMenu = it }
            ) {
                OutlinedTextField(
                    value = selectedEngine.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_engine)) },
                    leadingIcon = {
                        Icon(Icons.Default.Speed, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showEngineMenu)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = showEngineMenu,
                    onDismissRequest = { showEngineMenu = false }
                ) {
                    HybridEngineType.cloudEngines.forEach { engine ->
                        val isLocked = engine == HybridEngineType.CPP_HPC && userTier != UserTier.MASTER

                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(engine.displayName)
                                        if (isLocked) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "MASTER",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = QuantumOrange
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${engine.speedupFactor.toInt()}x speedup",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = QuantumCyan
                                    )
                                }
                            },
                            onClick = {
                                onEngineChange(engine)
                                showEngineMenu = false
                            },
                            enabled = !isLocked
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Optimization level selector
            ExposedDropdownMenuBox(
                expanded = showOptimizationMenu,
                onExpandedChange = { showOptimizationMenu = it }
            ) {
                OutlinedTextField(
                    value = selectedOptimization.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Optimization") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showOptimizationMenu)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = showOptimizationMenu,
                    onDismissRequest = { showOptimizationMenu = false }
                ) {
                    OptimizationLevel.entries.forEach { level ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(level.displayName)
                                    Text(
                                        text = level.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                onOptimizationChange(level)
                                showOptimizationMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shots slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Shots",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(60.dp)
                )
                Slider(
                    value = kotlin.math.log10(shots.toFloat()),
                    onValueChange = {
                        onShotsChange(10.0.pow(it.toDouble()).toInt())
                    },
                    valueRange = 1f..5f,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = shots.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuantumCyan,
                    modifier = Modifier.width(60.dp)
                )
            }
        }
    }
}

@Composable
private fun ExecutionResultCard(result: HybridExecutionResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = QuantumGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = QuantumGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.text_execution_complete),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuantumGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricItem(
                    label = stringResource(R.string.time),
                    value = result.metrics.formattedExecutionTime,
                    color = QuantumGreen
                )
                MetricItem(
                    label = stringResource(R.string.speedup),
                    value = result.metrics.formattedSpeedup,
                    color = QuantumCyan
                )
                MetricItem(
                    label = stringResource(R.string.memory),
                    value = String.format("%.1f MB", result.metrics.memoryUsedMB),
                    color = QuantumOrange
                )
            }

            if (result.metrics.cacheHit) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.cache_hit_result),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BenchmarkResultCard(result: BenchmarkResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.text_benchmark_results),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                result.fastestEngine?.let { fastest ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = QuantumGreen.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = stringResource(R.string.fastest_engine, fastest.displayName),
                            style = MaterialTheme.typography.labelSmall,
                            color = QuantumGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bar chart comparison
            BenchmarkBarChart(
                results = result.results,
                baselineTime = result.baselineTime
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Detailed results
            result.results.forEach { entry ->
                BenchmarkEntryRow(entry = entry, baselineTime = result.baselineTime)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun BenchmarkBarChart(
    results: List<EngineBenchmarkEntry>,
    baselineTime: Long
) {
    val maxTime = results.maxOfOrNull { it.executionTimeMs } ?: 1L
    val colors = listOf(QuantumPurple, QuantumCyan, QuantumOrange)

    Column {
        results.forEachIndexed { index, entry ->
            val barWidth = (entry.executionTimeMs.toFloat() / maxTime).coerceIn(0.05f, 1f)
            val color = colors[index % colors.size]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.engineType.displayName.take(8),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(60.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(barWidth)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                    )
                }

                Text(
                    text = "${entry.executionTimeMs}ms",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun BenchmarkEntryRow(
    entry: EngineBenchmarkEntry,
    baselineTime: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = entry.engineType.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${entry.executionTimeMs}ms",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%.1fx", entry.speedupFactor),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = QuantumCyan
            )
            Text(
                text = "speedup",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CircuitStatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
