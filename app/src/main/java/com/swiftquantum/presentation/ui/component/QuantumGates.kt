package com.swiftquantum.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.domain.model.Gate
import com.swiftquantum.domain.model.GateCategory
import com.swiftquantum.domain.model.GateType

@Composable
fun GateChip(
    gateType: GateType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val backgroundColor = when (gateType.category) {
        GateCategory.SINGLE_QUBIT -> Color(0xFF8B5CF6)
        GateCategory.ROTATION -> Color(0xFFF59E0B)
        GateCategory.MULTI_QUBIT -> Color(0xFFEC4899)
        GateCategory.CONTROLLED -> Color(0xFF22C55E)
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .then(
                if (selected) Modifier.border(2.dp, Color.White, RoundedCornerShape(8.dp))
                else Modifier
            ),
        color = if (selected) backgroundColor else backgroundColor.copy(alpha = 0.8f),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = if (selected) 8.dp else 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = gateType.symbol,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Text(
                text = gateType.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GatePalette(
    onGateSelected: (GateType) -> Unit,
    selectedGate: GateType?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        GateCategorySection(
            title = "Single Qubit",
            gates = GateType.singleQubitGates,
            selectedGate = selectedGate,
            onGateSelected = onGateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        GateCategorySection(
            title = "Rotation",
            gates = GateType.rotationGates,
            selectedGate = selectedGate,
            onGateSelected = onGateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        GateCategorySection(
            title = "Multi-Qubit",
            gates = GateType.multiQubitGates,
            selectedGate = selectedGate,
            onGateSelected = onGateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        GateCategorySection(
            title = "Controlled",
            gates = GateType.controlledGates,
            selectedGate = selectedGate,
            onGateSelected = onGateSelected
        )
    }
}

@Composable
private fun GateCategorySection(
    title: String,
    gates: List<GateType>,
    selectedGate: GateType?,
    onGateSelected: (GateType) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(gates) { gate ->
                GateChip(
                    gateType = gate,
                    onClick = { onGateSelected(gate) },
                    selected = selectedGate == gate
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GateGrid(
    onGateSelected: (GateType) -> Unit,
    selectedGate: GateType?,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GateType.entries.forEach { gate ->
            GateChip(
                gateType = gate,
                onClick = { onGateSelected(gate) },
                selected = selectedGate == gate
            )
        }
    }
}

@Composable
fun GateBlock(
    gate: Gate,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = when (gate.type.category) {
        GateCategory.SINGLE_QUBIT -> Color(0xFF8B5CF6)
        GateCategory.ROTATION -> Color(0xFFF59E0B)
        GateCategory.MULTI_QUBIT -> Color(0xFFEC4899)
        GateCategory.CONTROLLED -> Color(0xFF22C55E)
    }

    Surface(
        modifier = modifier
            .size(48.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gate.type.symbol,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CircuitDiagram(
    numQubits: Int,
    gates: List<Gate>,
    modifier: Modifier = Modifier,
    onGateClick: ((Int) -> Unit)? = null
) {
    val maxDepth = gates.maxOfOrNull { it.position }?.plus(1) ?: 1

    Column(modifier = modifier) {
        repeat(numQubits) { qubit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Qubit label
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "q$qubit",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Circuit wire with gates
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )

                // Gates on this qubit
                LazyRow(
                    modifier = Modifier.weight(4f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    val gatesOnQubit = gates.filter { gate ->
                        qubit in gate.targetQubits || qubit in gate.controlQubits
                    }.sortedBy { it.position }

                    items(gatesOnQubit.size) { index ->
                        val gate = gatesOnQubit[index]
                        val gateIndex = gates.indexOf(gate)

                        if (qubit in gate.controlQubits) {
                            // Control dot
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .then(
                                        if (onGateClick != null) Modifier.clickable { onGateClick(gateIndex) }
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color(0xFF6366F1), RoundedCornerShape(50))
                                )
                            }
                        } else {
                            GateBlock(
                                gate = gate,
                                onClick = onGateClick?.let { { it(gateIndex) } }
                            )
                        }
                    }
                }

                // Measurement symbol
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}
