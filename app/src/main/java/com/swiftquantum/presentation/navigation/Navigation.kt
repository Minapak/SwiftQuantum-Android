package com.swiftquantum.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CloudQueue
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.swiftquantum.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.swiftquantum.presentation.ui.screen.AuthScreen
import com.swiftquantum.presentation.ui.screen.BenchmarkScreen
import com.swiftquantum.presentation.ui.screen.CircuitBuilderScreen
import com.swiftquantum.presentation.ui.screen.HardwareScreen
import com.swiftquantum.presentation.ui.screen.PaywallScreen
import com.swiftquantum.presentation.ui.screen.ProfileScreen
import com.swiftquantum.presentation.ui.screen.QASMScreen
import com.swiftquantum.presentation.ui.screen.SettingsScreen
import com.swiftquantum.presentation.ui.screen.SimulatorScreen
import com.swiftquantum.presentation.ui.screen.SplashScreen
import com.swiftquantum.presentation.ui.screen.LanguageSelectionScreen
import com.swiftquantum.presentation.ui.screen.CloudVisualizationScreen
import com.swiftquantum.presentation.ui.screen.EntertainmentScreen
import com.swiftquantum.presentation.ui.screen.VisualizeScreen
import com.swiftquantum.presentation.ui.screen.AustralianStandardsScreen
import com.swiftquantum.presentation.ui.screen.admin.AdminRootScreen

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Splash : Screen(
        route = "splash",
        titleRes = R.string.splash_loading,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Filled.Star
    )

    data object LanguageSelection : Screen(
        route = "language_selection",
        titleRes = R.string.settings_language,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Filled.Star
    )

    data object Simulator : Screen(
        route = "simulator",
        titleRes = R.string.nav_simulator,
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow
    )

    data object Circuit : Screen(
        route = "circuit",
        titleRes = R.string.nav_circuit,
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )

    data object QASM : Screen(
        route = "qasm",
        titleRes = R.string.nav_qasm,
        selectedIcon = Icons.Filled.Code,
        unselectedIcon = Icons.Outlined.Code
    )

    data object Benchmark : Screen(
        route = "benchmark",
        titleRes = R.string.nav_benchmark,
        selectedIcon = Icons.Filled.Speed,
        unselectedIcon = Icons.Outlined.Speed
    )

    data object Hardware : Screen(
        route = "hardware",
        titleRes = R.string.nav_hardware,
        selectedIcon = Icons.Filled.Memory,
        unselectedIcon = Icons.Outlined.Memory
    )

    data object Profile : Screen(
        route = "profile",
        titleRes = R.string.nav_profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    data object Auth : Screen(
        route = "auth",
        titleRes = R.string.nav_login,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    data object Settings : Screen(
        route = "settings",
        titleRes = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    data object Paywall : Screen(
        route = "paywall",
        titleRes = R.string.upgrade,
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    )

    data object Visualize : Screen(
        route = "visualize",
        titleRes = R.string.visualize_title,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    )

    data object Entertainment : Screen(
        route = "entertainment",
        titleRes = R.string.nav_entertainment,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    )

    data object AustralianStandards : Screen(
        route = "australian_standards",
        titleRes = R.string.aus_standards_title,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Filled.Star
    )

    data object AdminDashboard : Screen(
        route = "admin",
        titleRes = R.string.admin_dashboard_title,
        selectedIcon = Icons.Filled.AdminPanelSettings,
        unselectedIcon = Icons.Outlined.AdminPanelSettings
    )

    companion object {
        // Main bottom navigation items (5 items max for good UX)
        val bottomNavItems = listOf(Simulator, Circuit, QASM, Benchmark, Profile)
        // Drawer items include Entertainment
        val drawerItems = listOf(Simulator, Circuit, QASM, Benchmark, Hardware, Entertainment, Visualize)
    }
}

@Composable
fun SwiftQuantumNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
    startDestination: String = Screen.Splash.route,  // Start with splash screen
    showSplash: Boolean = true,
    onboardingCompleted: Boolean = false,
    onLanguageSelected: (String) -> Unit = {},
    onOnboardingComplete: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = if (showSplash) Screen.Splash.route else Screen.Simulator.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                appVersion = "5.2.0",
                onSplashComplete = {
                    if (onboardingCompleted) {
                        navController.navigate(Screen.Simulator.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.LanguageSelection.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                onLanguageSelected = { languageCode ->
                    onLanguageSelected(languageCode)
                },
                onContinue = {
                    onOnboardingComplete()
                    navController.navigate(Screen.Simulator.route) {
                        popUpTo(Screen.LanguageSelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Simulator.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onContinueAsGuest = {
                    navController.navigate(Screen.Simulator.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Simulator.route) {
            SimulatorScreen()
        }

        composable(Screen.Circuit.route) {
            CircuitBuilderScreen()
        }

        composable(Screen.QASM.route) {
            QASMScreen(
                onCircuitImported = { circuit ->
                    // Navigate to circuit builder with the imported circuit
                    // For now, just navigate to circuit screen
                    navController.navigate(Screen.Circuit.route)
                }
            )
        }

        composable(Screen.Benchmark.route) {
            BenchmarkScreen()
        }

        composable(Screen.Hardware.route) {
            HardwareScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPaywall = {
                    navController.navigate(Screen.Paywall.route)
                },
                onNavigateToAustralianStandards = {
                    navController.navigate(Screen.AustralianStandards.route)
                },
                onNavigateToAdmin = {
                    navController.navigate(Screen.AdminDashboard.route)
                },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Paywall.route) {
            PaywallScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPurchaseSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Visualize.route) {
            VisualizeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Entertainment.route) {
            EntertainmentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AustralianStandards.route) {
            AustralianStandardsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminRootScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
