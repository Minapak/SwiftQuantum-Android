package com.swiftquantum.presentation.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMVersion
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumOrange
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.ui.theme.QuantumRed
import com.swiftquantum.presentation.viewmodel.QASMEvent
import com.swiftquantum.presentation.viewmodel.QASMViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QASMScreen(
    viewModel: QASMViewModel = hiltViewModel(),
    onCircuitImported: ((Circuit) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is QASMEvent.ImportSuccess -> {
                    Toast.makeText(context, "Circuit imported successfully", Toast.LENGTH_SHORT).show()
                    onCircuitImported?.invoke(event.circuit)
                }
                is QASMEvent.ExportSuccess -> {
                    Toast.makeText(context, "Circuit exported to QASM", Toast.LENGTH_SHORT).show()
                }
                is QASMEvent.ValidationComplete -> {
                    val message = if (event.isValid) "QASM code is valid" else "QASM code has errors"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
                is QASMEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is QASMEvent.CodeCopied -> {
                    Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
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
                            text = "QASM Editor",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Import & Export OpenQASM",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    // Copy button
                    IconButton(
                        onClick = {
                            if (uiState.code.isNotEmpty()) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("QASM Code", uiState.code))
                                viewModel.onCodeCopied()
                            }
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Editor") },
                    icon = { Icon(Icons.Default.Code, contentDescription = null) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Templates") }
                )
            }

            when (selectedTabIndex) {
                0 -> QASMEditorTab(
                    uiState = uiState,
                    onCodeChange = viewModel::updateCode,
                    onVersionChange = viewModel::setVersion,
                    onImport = viewModel::importQASM,
                    onValidate = viewModel::validateCode,
                    onIncludeCommentsChange = viewModel::setIncludeComments,
                    onPrettyPrintChange = viewModel::setPrettyPrint,
                    onIncludeMeasurementsChange = viewModel::setIncludeMeasurements
                )
                1 -> QASMTemplatesTab(
                    templates = uiState.templates,
                    selectedCategory = uiState.selectedCategory,
                    isLoading = uiState.isLoadingTemplates,
                    onCategoryChange = viewModel::loadTemplates,
                    onTemplateSelect = { template ->
                        viewModel.selectTemplate(template)
                        selectedTabIndex = 0
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QASMEditorTab(
    uiState: com.swiftquantum.presentation.viewmodel.QASMUiState,
    onCodeChange: (String) -> Unit,
    onVersionChange: (QASMVersion) -> Unit,
    onImport: () -> Unit,
    onValidate: () -> Unit,
    onIncludeCommentsChange: (Boolean) -> Unit,
    onPrettyPrintChange: (Boolean) -> Unit,
    onIncludeMeasurementsChange: (Boolean) -> Unit
) {
    var showVersionMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Version selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExposedDropdownMenuBox(
                    expanded = showVersionMenu,
                    onExpandedChange = { showVersionMenu = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = uiState.selectedVersion.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("QASM Version") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showVersionMenu)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = showVersionMenu,
                        onDismissRequest = { showVersionMenu = false }
                    ) {
                        QASMVersion.entries.forEach { version ->
                            DropdownMenuItem(
                                text = { Text(version.displayName) },
                                onClick = {
                                    onVersionChange(version)
                                    showVersionMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Code editor
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Syntax highlighted code editor
                    QASMCodeEditor(
                        code = uiState.code,
                        onCodeChange = onCodeChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    // Validation status
                    uiState.validationResult?.let { result ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isValid) Icons.Default.Check else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (result.isValid) QuantumGreen else QuantumRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (result.isValid) "Valid QASM" else "Invalid: ${result.errors.firstOrNull()?.message ?: "Unknown error"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (result.isValid) QuantumGreen else QuantumRed
                            )
                        }
                    }
                }
            }
        }

        // Export options
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Export Options",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Include Comments", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = uiState.includeComments,
                            onCheckedChange = onIncludeCommentsChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Pretty Print", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = uiState.prettyPrint,
                            onCheckedChange = onPrettyPrintChange
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Include Measurements", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = uiState.includeMeasurements,
                            onCheckedChange = onIncludeMeasurementsChange
                        )
                    }
                }
            }
        }

        // Action buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onValidate,
                    modifier = Modifier.weight(1f),
                    enabled = uiState.code.isNotEmpty() && !uiState.isValidating,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isValidating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Validate")
                }

                Button(
                    onClick = onImport,
                    modifier = Modifier.weight(1f),
                    enabled = uiState.code.isNotEmpty() && !uiState.isImporting,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isImporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Upload, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Import")
                }
            }
        }

        // Imported circuit preview
        uiState.importedCircuit?.let { circuit ->
            item {
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
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = QuantumGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Circuit Imported",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = QuantumGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CircuitPreviewCard(circuit = circuit)
                    }
                }
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

@Composable
private fun QASMCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E1E2E))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
    ) {
        BasicTextField(
            value = code,
            onValueChange = onCodeChange,
            textStyle = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            ),
            cursorBrush = SolidColor(QuantumCyan),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .horizontalScroll(scrollState),
            decorationBox = { innerTextField ->
                if (code.isEmpty()) {
                    Text(
                        text = "// Enter QASM code here...\nOPENQASM 2.0;\ninclude \"qelib1.inc\";\n\nqreg q[2];\ncreg c[2];\n\nh q[0];\ncx q[0], q[1];\n\nmeasure q -> c;",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = Color.Gray.copy(alpha = 0.5f),
                            lineHeight = 20.sp
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun QASMTemplatesTab(
    templates: List<QASMTemplate>,
    selectedCategory: QASMTemplateCategory?,
    isLoading: Boolean,
    onCategoryChange: (QASMTemplateCategory?) -> Unit,
    onTemplateSelect: (QASMTemplate) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Category filter
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { onCategoryChange(null) },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuantumPurple.copy(alpha = 0.2f),
                            selectedLabelColor = QuantumPurple
                        )
                    )
                }
                items(QASMTemplateCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuantumPurple.copy(alpha = 0.2f),
                            selectedLabelColor = QuantumPurple
                        )
                    )
                }
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(templates) { template ->
                TemplateCard(
                    template = template,
                    onClick = { onTemplateSelect(template) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TemplateCard(
    template: QASMTemplate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    text = template.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                DifficultyBadge(level = template.difficultyLevel)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(
                    label = "${template.numQubits} qubits",
                    color = QuantumPurple
                )
                InfoChip(
                    label = template.category.name.replace("_", " ").lowercase(),
                    color = QuantumCyan
                )
            }
        }
    }
}

@Composable
private fun DifficultyBadge(level: com.swiftquantum.domain.model.DifficultyLevel) {
    val (color, text) = when (level) {
        com.swiftquantum.domain.model.DifficultyLevel.BEGINNER -> QuantumGreen to "Beginner"
        com.swiftquantum.domain.model.DifficultyLevel.INTERMEDIATE -> QuantumOrange to "Intermediate"
        com.swiftquantum.domain.model.DifficultyLevel.ADVANCED -> QuantumRed to "Advanced"
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoChip(
    label: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CircuitPreviewCard(circuit: Circuit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = circuit.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = circuit.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem(label = "Qubits", value = circuit.numQubits.toString(), color = QuantumPurple)
            StatItem(label = "Gates", value = circuit.gateCount.toString(), color = QuantumCyan)
            StatItem(label = "Depth", value = circuit.depth.toString(), color = QuantumOrange)
        }
    }
}

@Composable
private fun StatItem(
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
