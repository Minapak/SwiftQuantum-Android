package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.domain.model.BlochSphereState
import com.swiftquantum.domain.model.ExecutionBackend
import com.swiftquantum.domain.model.GateType
import com.swiftquantum.presentation.ui.component.BlochSphere
import com.swiftquantum.presentation.ui.component.CircuitDiagram
import com.swiftquantum.presentation.ui.component.GateChip
import com.swiftquantum.presentation.ui.component.ResultHistogram
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.viewmodel.CircuitPreset
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(
    viewModel: SimulatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBackendMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.simulator_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.simulator_tier_info, uiState.userTier.name, uiState.maxQubits),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { viewModel.clearCircuit() }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.clear))
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
            // Configuration Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.configuration),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Qubits slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.qubits),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(80.dp)
                            )
                            Slider(
                                value = uiState.numQubits.toFloat(),
                                onValueChange = { viewModel.setNumQubits(it.toInt()) },
                                valueRange = 1f..uiState.maxQubits.toFloat(),
                                steps = uiState.maxQubits - 2,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = uiState.numQubits.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuantumPurple,
                                modifier = Modifier.width(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Shots slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.shots),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(80.dp)
                            )
                            Slider(
                                value = kotlin.math.log10(uiState.shots.toFloat()),
                                onValueChange = {
                                    viewModel.setShots(10.0.pow(it.toDouble()).toInt())
                                },
                                valueRange = 1f..5f,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = uiState.shots.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuantumCyan,
                                modifier = Modifier.width(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Backend selector
                        ExposedDropdownMenuBox(
                            expanded = showBackendMenu,
                            onExpandedChange = { showBackendMenu = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.selectedBackend.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.backend)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Speed, contentDescription = null)
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showBackendMenu)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = showBackendMenu,
                                onDismissRequest = { showBackendMenu = false }
                            ) {
                                ExecutionBackend.simulators.forEach { backend ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(backend.displayName)
                                                Text(
                                                    text = backend.description,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.setBackend(backend)
                                            showBackendMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Preset circuits
            item {
                Text(
                    text = stringResource(R.string.quick_start_circuits),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CircuitPreset.entries) { preset ->
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.loadPresetCircuit(preset) },
                            label = {
                                Text(
                                    text = preset.name.replace("_", " "),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        )
                    }
                }
            }

            // Gate palette
            item {
                Text(
                    text = stringResource(R.string.gates),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GateType.singleQubitGates + GateType.multiQubitGates.take(2)) { gate ->
                        GateChip(
                            gateType = gate,
                            onClick = {
                                viewModel.addGate(
                                    gateType = gate,
                                    targetQubit = uiState.selectedQubit,
                                    controlQubit = if (gate.qubitCount > 1) {
                                        (uiState.selectedQubit + 1) % uiState.numQubits
                                    } else null
                                )
                            }
                        )
                    }
                }
            }

            // Circuit diagram
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.circuit_builder),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.depth_count, uiState.circuit.depth),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CircuitDiagram(
                            numQubits = uiState.numQubits,
                            gates = uiState.circuit.gates,
                            onGateClick = { viewModel.removeLastGate() }
                        )
                    }
                }
            }

            // Run button
            item {
                Button(
                    onClick = { viewModel.runSimulation() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isRunning
                ) {
                    if (uiState.isRunning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.simulation_running))
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.run_simulation),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // Results
            uiState.result?.let { result ->
                item {
                    Text(
                        text = stringResource(R.string.results),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Execution info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = QuantumGreen.copy(alpha = 0.2f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${result.executionTimeMs}ms",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = QuantumGreen
                                )
                                Text(
                                    text = stringResource(R.string.execution_time),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        result.fidelity?.let { fidelity ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = QuantumPurple.copy(alpha = 0.2f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = String.format("%.2f%%", fidelity * 100),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = QuantumPurple
                                    )
                                    Text(
                                        text = stringResource(R.string.fidelity),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Histogram
                item {
                    ResultHistogram(result = result)
                }

                // Bloch Sphere (only for single qubit)
                if (uiState.numQubits == 1 && uiState.blochStates.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.bloch_sphere),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BlochSphere(
                            state = uiState.blochStates.first(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Error
            uiState.error?.let { error ->
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
