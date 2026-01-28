package com.swiftquantum.presentation.ui.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.domain.model.BackendStatus
import com.swiftquantum.domain.model.IBMQuantumBackend
import com.swiftquantum.domain.model.IBMQuantumJob
import com.swiftquantum.domain.model.JobStatus
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumOrange
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.ui.theme.QuantumRed
import com.swiftquantum.presentation.ui.theme.StatusOffline
import com.swiftquantum.presentation.ui.theme.StatusOnline
import com.swiftquantum.presentation.ui.theme.StatusPending
import com.swiftquantum.presentation.ui.theme.TierMaster
import com.swiftquantum.presentation.viewmodel.HardwareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardwareScreen(
    viewModel: HardwareViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showToken by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.ibm_quantum),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (uiState.connection.isConnected) stringResource(R.string.connected)
                            else stringResource(R.string.disconnected),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (uiState.connection.isConnected) StatusOnline else StatusOffline
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    if (uiState.connection.isConnected) {
                        IconButton(onClick = { viewModel.loadBackends() }) {
                            Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.refresh))
                        }
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
            // Access restriction warning
            if (!uiState.hasHardwareAccess) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = TierMaster.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Memory,
                                contentDescription = null,
                                tint = TierMaster,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.hardware_access_master_only),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TierMaster
                                )
                                Text(
                                    text = "Upgrade to MASTER tier for IBM Quantum access",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Connection card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.connect_ibm),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!uiState.connection.isConnected) {
                            OutlinedTextField(
                                value = uiState.apiToken,
                                onValueChange = { viewModel.setApiToken(it) },
                                label = { Text(stringResource(R.string.ibm_api_token)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = if (showToken) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showToken = !showToken }) {
                                        Icon(
                                            if (showToken) Icons.Default.VisibilityOff
                                            else Icons.Default.Visibility,
                                            contentDescription = null
                                        )
                                    }
                                },
                                enabled = uiState.hasHardwareAccess
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.connect() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                enabled = uiState.hasHardwareAccess && uiState.apiToken.isNotBlank() && !uiState.isConnecting
                            ) {
                                if (uiState.isConnecting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text("Connect")
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(StatusOnline)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = uiState.connection.username ?: "Connected",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${uiState.availableBackends.size} backends available",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = { viewModel.disconnect() },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = QuantumRed
                                    )
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Disconnect")
                                }
                            }
                        }

                        // Error
                        uiState.error?.let { error ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Available backends
            if (uiState.connection.isConnected && uiState.availableBackends.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.available_backends),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(uiState.availableBackends) { backend ->
                    BackendCard(
                        backend = backend,
                        isSelected = uiState.selectedBackend?.name == backend.name,
                        onClick = { viewModel.selectBackend(backend) }
                    )
                }
            }

            // Active jobs
            if (uiState.activeJobs.isNotEmpty()) {
                item {
                    Text(
                        text = "Active Jobs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(uiState.activeJobs) { job ->
                    JobCard(
                        job = job,
                        onCancel = { viewModel.cancelJob(job.id) },
                        onViewResult = { viewModel.getJobResult(job.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun BackendCard(
    backend: IBMQuantumBackend,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) QuantumPurple.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(QuantumPurple)
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Memory,
                    contentDescription = null,
                    tint = if (backend.isSimulator) QuantumCyan else QuantumOrange,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = backend.displayName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${backend.numQubits} qubits" +
                                if (backend.pendingJobs > 0) " | ${backend.pendingJobs} pending jobs" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                StatusBadge(status = backend.status)
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = QuantumGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: BackendStatus) {
    val (color, text) = when (status) {
        BackendStatus.ONLINE -> StatusOnline to "Online"
        BackendStatus.OFFLINE -> StatusOffline to "Offline"
        BackendStatus.MAINTENANCE -> StatusPending to "Maintenance"
        BackendStatus.UNKNOWN -> StatusOffline to "Unknown"
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun JobCard(
    job: IBMQuantumJob,
    onCancel: () -> Unit,
    onViewResult: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Job ${job.id.take(8)}...",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = job.backend,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                JobStatusBadge(status = job.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                job.queuePosition?.let { position ->
                    Text(
                        text = "${stringResource(R.string.queue_position)}: $position",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    if (job.status in listOf(JobStatus.QUEUED, JobStatus.RUNNING)) {
                        OutlinedButton(
                            onClick = onCancel,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = QuantumRed
                            )
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }

                    if (job.status == JobStatus.COMPLETED) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onViewResult) {
                            Text("View Result")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JobStatusBadge(status: JobStatus) {
    val (color, text) = when (status) {
        JobStatus.INITIALIZING -> StatusPending to "Initializing"
        JobStatus.QUEUED -> QuantumOrange to "Queued"
        JobStatus.VALIDATING -> QuantumCyan to "Validating"
        JobStatus.RUNNING -> QuantumPurple to "Running"
        JobStatus.COMPLETED -> QuantumGreen to "Completed"
        JobStatus.FAILED -> QuantumRed to "Failed"
        JobStatus.CANCELLED -> StatusOffline to "Cancelled"
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (status in listOf(JobStatus.RUNNING, JobStatus.VALIDATING)) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    color = color,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}
