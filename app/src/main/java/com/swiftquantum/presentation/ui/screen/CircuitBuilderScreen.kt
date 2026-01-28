package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.GateType
import com.swiftquantum.presentation.ui.component.CircuitDiagram
import com.swiftquantum.presentation.ui.component.GatePalette
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.viewmodel.CircuitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircuitBuilderScreen(
    viewModel: CircuitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showGateSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.circuit_builder),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { viewModel.showLoadDialog(true) }) {
                        Icon(Icons.Default.FolderOpen, contentDescription = stringResource(R.string.load_circuit))
                    }
                    IconButton(onClick = { viewModel.showSaveDialog(true) }) {
                        Icon(Icons.Default.Save, contentDescription = stringResource(R.string.save_circuit))
                    }
                    IconButton(onClick = { viewModel.clearCircuit() }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.clear_circuit))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showGateSheet = true },
                containerColor = QuantumPurple
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_gate))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Circuit info card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.circuitName,
                            onValueChange = { viewModel.setCircuitName(it) },
                            label = { Text(stringResource(R.string.circuit_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = uiState.circuitDescription,
                            onValueChange = { viewModel.setCircuitDescription(it) },
                            label = { Text(stringResource(R.string.circuit_description)) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp)
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
                                value = uiState.circuit.numQubits.toFloat(),
                                onValueChange = { viewModel.setNumQubits(it.toInt()) },
                                valueRange = 1f..uiState.maxQubits.toFloat(),
                                steps = uiState.maxQubits - 2,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = uiState.circuit.numQubits.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuantumPurple,
                                modifier = Modifier.width(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Gates: ${uiState.circuit.gateCount}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Depth: ${uiState.circuit.depth}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
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
                        Text(
                            text = "Circuit Diagram",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.circuit.gates.isEmpty()) {
                            Text(
                                text = "Tap + to add gates to your circuit",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 32.dp)
                            )
                        } else {
                            CircuitDiagram(
                                numQubits = uiState.circuit.numQubits,
                                gates = uiState.circuit.gates,
                                onGateClick = { index -> viewModel.removeGate(index) }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Gate selection bottom sheet
        if (showGateSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGateSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.add_gate),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Qubit selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Qubit:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Slider(
                            value = uiState.selectedQubit.toFloat(),
                            onValueChange = { viewModel.selectQubit(it.toInt()) },
                            valueRange = 0f..(uiState.circuit.numQubits - 1).toFloat(),
                            steps = uiState.circuit.numQubits - 2,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "q${uiState.selectedQubit}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = QuantumPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GatePalette(
                        onGateSelected = { gateType ->
                            val controlQubit = if (gateType.qubitCount > 1) {
                                (uiState.selectedQubit + 1) % uiState.circuit.numQubits
                            } else null
                            viewModel.addGate(gateType, uiState.selectedQubit, controlQubit)
                            showGateSheet = false
                        },
                        selectedGate = uiState.selectedGateType
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Save dialog
        if (uiState.showSaveDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showSaveDialog(false) },
                title = { Text(stringResource(R.string.save_circuit)) },
                text = {
                    Column {
                        Text("Save \"${uiState.circuitName.ifBlank { "Untitled Circuit" }}\"?")
                        if (uiState.error != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.saveCircuit() },
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(stringResource(R.string.save))
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showSaveDialog(false) }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        // Load dialog
        if (uiState.showLoadDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showLoadDialog(false) },
                title = { Text(stringResource(R.string.my_circuits)) },
                text = {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else if (uiState.savedCircuits.isEmpty()) {
                        Text("No saved circuits")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.savedCircuits) { circuit ->
                                CircuitListItem(
                                    circuit = circuit,
                                    onClick = { viewModel.loadCircuitFromList(circuit) },
                                    onDelete = { circuit.id?.let { viewModel.deleteCircuit(it) } }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.showLoadDialog(false) }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
private fun CircuitListItem(
    circuit: Circuit,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = circuit.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${circuit.numQubits} qubits, ${circuit.gateCount} gates",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
