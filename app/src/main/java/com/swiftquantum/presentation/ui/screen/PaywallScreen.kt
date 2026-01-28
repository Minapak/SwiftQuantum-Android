package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumOrange
import com.swiftquantum.presentation.ui.theme.QuantumPink
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.ui.theme.TierMaster
import com.swiftquantum.presentation.ui.theme.TierPro
import com.swiftquantum.presentation.viewmodel.PaywallEvent
import com.swiftquantum.presentation.viewmodel.PaywallViewModel
import kotlinx.coroutines.flow.collectLatest

enum class BillingPeriod {
    MONTHLY, YEARLY
}

data class PlanFeature(
    val title: String,
    val included: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen(
    onNavigateBack: () -> Unit,
    onPurchaseSuccess: () -> Unit,
    viewModel: PaywallViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPeriod by remember { mutableStateOf(BillingPeriod.MONTHLY) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        viewModel.events.collectLatest { event ->
            when (event) {
                is PaywallEvent.PurchaseSuccess -> onPurchaseSuccess()
                is PaywallEvent.RestoreSuccess -> onPurchaseSuccess()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D0D1A),
                            Color(0xFF1A1A2E),
                            Color(0xFF0D0D1A)
                        )
                    )
                )
        ) {
            // Cyberpunk scan lines effect
            CyberpunkScanLines()

            // Animated glow orbs
            AnimatedGlowOrbs()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Header
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn() + slideInVertically { -it }
                    ) {
                        PaywallHeader()
                    }
                }

                // Billing Period Toggle
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 100))
                    ) {
                        BillingPeriodToggle(
                            selectedPeriod = selectedPeriod,
                            onPeriodSelected = { selectedPeriod = it }
                        )
                    }
                }

                // Pro Engine Card
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 200)) +
                                slideInVertically(animationSpec = tween(delayMillis = 200)) { it / 2 }
                    ) {
                        ProEngineCard(
                            isYearly = selectedPeriod == BillingPeriod.YEARLY,
                            isPurchasing = uiState.isPurchasing,
                            onSubscribe = {
                                val productId = if (selectedPeriod == BillingPeriod.YEARLY)
                                    "swiftquantum_pro_yearly"
                                else
                                    "swiftquantum_pro_monthly"
                                viewModel.purchaseSubscription(productId)
                            }
                        )
                    }
                }

                // Master Engine Card (Recommended)
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 300)) +
                                slideInVertically(animationSpec = tween(delayMillis = 300)) { it / 2 }
                    ) {
                        MasterEngineCard(
                            isYearly = selectedPeriod == BillingPeriod.YEARLY,
                            isPurchasing = uiState.isPurchasing,
                            onSubscribe = {
                                val productId = if (selectedPeriod == BillingPeriod.YEARLY)
                                    "swiftquantum_master_yearly"
                                else
                                    "swiftquantum_master_monthly"
                                viewModel.purchaseSubscription(productId)
                            }
                        )
                    }
                }

                // Restore Purchases Button
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 400))
                    ) {
                        TextButton(
                            onClick = { viewModel.restorePurchases() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isRestoring
                        ) {
                            if (uiState.isRestoring) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = QuantumCyan
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            } else {
                                Icon(
                                    Icons.Default.Restore,
                                    contentDescription = null,
                                    tint = QuantumCyan
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = stringResource(R.string.restore_purchases),
                                color = QuantumCyan
                            )
                        }
                    }
                }

                // Disclaimer
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 500))
                    ) {
                        PaywallDisclaimer()
                    }
                }

                // Terms and Privacy Links
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(delayMillis = 600))
                    ) {
                        TermsAndPrivacyLinks()
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun CyberpunkScanLines() {
    val infiniteTransition = rememberInfiniteTransition(label = "scanlines")
    val scanLineOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline_offset"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val lineSpacing = 4.dp.toPx()
        val lineCount = (size.height / lineSpacing).toInt()

        for (i in 0 until lineCount) {
            val y = i * lineSpacing
            val alpha = ((i + scanLineOffset * 10) % 10 / 10f) * 0.05f
            drawLine(
                color = Color.White.copy(alpha = alpha),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
private fun AnimatedGlowOrbs() {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    val orbOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    val orbOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Purple orb
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-50).dp + orbOffset1.dp, y = 100.dp)
                .blur(80.dp)
                .alpha(0.3f)
                .background(QuantumPurple, CircleShape)
        )

        // Cyan orb
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = 250.dp + orbOffset2.dp, y = 400.dp)
                .blur(60.dp)
                .alpha(0.3f)
                .background(QuantumCyan, CircleShape)
        )

        // Orange orb
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 50.dp, y = 600.dp + orbOffset1.dp)
                .blur(50.dp)
                .alpha(0.2f)
                .background(QuantumOrange, CircleShape)
        )
    }
}

@Composable
private fun PaywallHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Rocket,
            contentDescription = null,
            tint = QuantumCyan,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.paywall_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.paywall_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BillingPeriodToggle(
    selectedPeriod: BillingPeriod,
    onPeriodSelected: (BillingPeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Monthly Button
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selectedPeriod == BillingPeriod.MONTHLY)
                        QuantumPurple
                    else
                        Color.Transparent
                )
                .clickable { onPeriodSelected(BillingPeriod.MONTHLY) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.paywall_monthly),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selectedPeriod == BillingPeriod.MONTHLY) FontWeight.Bold else FontWeight.Normal,
                color = Color.White
            )
        }

        // Yearly Button with Badge
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selectedPeriod == BillingPeriod.YEARLY)
                        QuantumPurple
                    else
                        Color.Transparent
                )
                .clickable { onPeriodSelected(BillingPeriod.YEARLY) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.paywall_yearly),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selectedPeriod == BillingPeriod.YEARLY) FontWeight.Bold else FontWeight.Normal,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = QuantumGreen
                ) {
                    Text(
                        text = stringResource(R.string.paywall_save_30),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProEngineCard(
    isYearly: Boolean,
    isPurchasing: Boolean,
    onSubscribe: () -> Unit
) {
    val monthlyPrice = "$9.99"
    val yearlyPrice = "$83.99"
    val yearlyMonthlyPrice = "$6.99"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(TierPro.copy(alpha = 0.5f), TierPro.copy(alpha = 0.2f))
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E).copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(TierPro.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = null,
                            tint = TierPro,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.paywall_pro_engine),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.paywall_pro_tagline),
                            style = MaterialTheme.typography.bodySmall,
                            color = TierPro
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = if (isYearly) yearlyMonthlyPrice else monthlyPrice,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.paywall_per_month),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
            }

            if (isYearly) {
                Text(
                    text = stringResource(R.string.paywall_billed_yearly, yearlyPrice),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Features
            ProFeaturesList()

            Spacer(modifier = Modifier.height(16.dp))

            // Subscribe Button
            Button(
                onClick = onSubscribe,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isPurchasing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TierPro
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isPurchasing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = stringResource(R.string.subscribe),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProFeaturesList() {
    val features = listOf(
        stringResource(R.string.paywall_pro_feature_1),
        stringResource(R.string.paywall_pro_feature_2),
        stringResource(R.string.paywall_pro_feature_3),
        stringResource(R.string.paywall_pro_feature_4)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        features.forEach { feature ->
            FeatureRow(feature = feature, color = TierPro)
        }
    }
}

@Composable
private fun MasterEngineCard(
    isYearly: Boolean,
    isPurchasing: Boolean,
    onSubscribe: () -> Unit
) {
    val monthlyPrice = "$29.99"
    val yearlyPrice = "$251.99"
    val yearlyMonthlyPrice = "$20.99"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(TierMaster, QuantumPink, TierMaster)
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E).copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Recommended Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TierMaster
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.paywall_recommended),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(TierMaster, QuantumPink)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Memory,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.paywall_master_engine),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.paywall_master_tagline),
                            style = MaterialTheme.typography.bodySmall,
                            color = TierMaster
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = if (isYearly) yearlyMonthlyPrice else monthlyPrice,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.paywall_per_month),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                )
            }

            if (isYearly) {
                Text(
                    text = stringResource(R.string.paywall_billed_yearly, yearlyPrice),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Features
            MasterFeaturesList()

            Spacer(modifier = Modifier.height(16.dp))

            // Subscribe Button
            Button(
                onClick = onSubscribe,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isPurchasing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TierMaster, QuantumPink)
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPurchasing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.subscribe),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MasterFeaturesList() {
    val features = listOf(
        stringResource(R.string.paywall_master_feature_1),
        stringResource(R.string.paywall_master_feature_2),
        stringResource(R.string.paywall_master_feature_3),
        stringResource(R.string.paywall_master_feature_4),
        stringResource(R.string.paywall_master_feature_5)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        features.forEach { feature ->
            FeatureRow(feature = feature, color = TierMaster)
        }
    }
}

@Composable
private fun FeatureRow(
    feature: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = feature,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun PaywallDisclaimer() {
    Text(
        text = stringResource(R.string.paywall_disclaimer),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    )
}

@Composable
private fun TermsAndPrivacyLinks() {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.terms_of_service),
            style = MaterialTheme.typography.bodySmall,
            color = QuantumCyan,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                // Open terms URL
            }
        )

        Text(
            text = " | ",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.5f)
        )

        Text(
            text = stringResource(R.string.privacy_policy),
            style = MaterialTheme.typography.bodySmall,
            color = QuantumCyan,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                // Open privacy URL
            }
        )
    }
}
