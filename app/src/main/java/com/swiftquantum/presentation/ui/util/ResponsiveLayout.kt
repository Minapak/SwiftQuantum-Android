package com.swiftquantum.presentation.ui.util

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Responsive Layout Utilities for SwiftQuantum Ecosystem
 *
 * Provides adaptive layouts for different device sizes:
 * - Phones (Compact)
 * - Foldables (Medium)
 * - Tablets (Expanded)
 */

/**
 * Device size categories based on WindowSizeClass
 */
enum class DeviceSize {
    Compact,   // Phone portrait
    Medium,    // Phone landscape / Foldable
    Expanded   // Tablet
}

/**
 * Composable function to get current window size class
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(activity)
}

/**
 * Get current device size category
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberDeviceSize(): DeviceSize {
    val windowSizeClass = rememberWindowSizeClass()
    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> DeviceSize.Compact
        WindowWidthSizeClass.Medium -> DeviceSize.Medium
        WindowWidthSizeClass.Expanded -> DeviceSize.Expanded
        else -> DeviceSize.Compact
    }
}

/**
 * Check if current device is a tablet
 */
@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}

/**
 * Check if current device is a foldable (medium width)
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun isFoldable(): Boolean {
    val windowSizeClass = rememberWindowSizeClass()
    return windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
}

/**
 * Check if device is in landscape orientation
 */
@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * Adaptive Layout that switches between different layouts based on screen size
 */
@Composable
fun AdaptiveLayout(
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable (() -> Unit)? = null,
    expandedContent: @Composable (() -> Unit)? = null
) {
    val deviceSize = rememberDeviceSize()

    when (deviceSize) {
        DeviceSize.Compact -> compactContent()
        DeviceSize.Medium -> (mediumContent ?: compactContent)()
        DeviceSize.Expanded -> (expandedContent ?: mediumContent ?: compactContent)()
    }
}

/**
 * Adaptive column count based on screen width
 */
@Composable
fun rememberAdaptiveColumnCount(
    compactColumns: Int = 1,
    mediumColumns: Int = 2,
    expandedColumns: Int = 3
): Int {
    val deviceSize = rememberDeviceSize()
    return when (deviceSize) {
        DeviceSize.Compact -> compactColumns
        DeviceSize.Medium -> mediumColumns
        DeviceSize.Expanded -> expandedColumns
    }
}

/**
 * Adaptive padding based on screen size
 */
@Composable
fun rememberAdaptivePadding(): PaddingValues {
    val deviceSize = rememberDeviceSize()
    return when (deviceSize) {
        DeviceSize.Compact -> PaddingValues(16.dp)
        DeviceSize.Medium -> PaddingValues(24.dp)
        DeviceSize.Expanded -> PaddingValues(32.dp)
    }
}

/**
 * Navigation Suite Scaffold for adaptive navigation
 * Uses NavigationBar for phones, NavigationRail for foldables/tablets
 */
@Composable
fun AdaptiveNavigationScaffold(
    navigationItems: List<NavigationItem>,
    selectedIndex: Int,
    onNavigate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val deviceSize = rememberDeviceSize()

    when (deviceSize) {
        DeviceSize.Compact -> {
            // Phone: Bottom navigation bar
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    NavigationBar {
                        navigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = index == selectedIndex,
                                onClick = { onNavigate(index) },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedIndex) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                },
                floatingActionButton = floatingActionButton,
                content = content
            )
        }
        DeviceSize.Medium, DeviceSize.Expanded -> {
            // Foldable/Tablet: Navigation rail
            Row(modifier = modifier.fillMaxSize()) {
                NavigationRail {
                    Spacer(modifier = Modifier.height(8.dp))
                    navigationItems.forEachIndexed { index, item ->
                        NavigationRailItem(
                            selected = index == selectedIndex,
                            onClick = { onNavigate(index) },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedIndex) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.weight(1f),
                    floatingActionButton = floatingActionButton,
                    content = content
                )
            }
        }
    }
}

/**
 * Navigation item data class
 */
data class NavigationItem(
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

/**
 * Two pane layout for list-detail patterns on large screens
 */
@Composable
fun TwoPaneLayout(
    isDetailVisible: Boolean,
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceSize = rememberDeviceSize()

    when (deviceSize) {
        DeviceSize.Compact -> {
            // Phone: Show one pane at a time
            if (isDetailVisible) {
                detailPane()
            } else {
                listPane()
            }
        }
        DeviceSize.Medium -> {
            // Foldable: Split 40/60
            Row(modifier = modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(0.4f)) {
                    listPane()
                }
                Box(modifier = Modifier.weight(0.6f)) {
                    detailPane()
                }
            }
        }
        DeviceSize.Expanded -> {
            // Tablet: Split 35/65
            Row(modifier = modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(0.35f)) {
                    listPane()
                }
                Box(modifier = Modifier.weight(0.65f)) {
                    detailPane()
                }
            }
        }
    }
}

/**
 * Responsive grid for displaying items in a flexible grid
 */
@Composable
fun ResponsiveGrid(
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier
) {
    val columnCount = rememberAdaptiveColumnCount()

    Column(modifier = modifier) {
        items.chunked(columnCount).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        item()
                    }
                }
                // Fill remaining space if row is not complete
                repeat(columnCount - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
