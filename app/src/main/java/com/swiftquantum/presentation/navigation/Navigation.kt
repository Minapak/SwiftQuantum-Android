package com.swiftquantum.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.swiftquantum.presentation.ui.screen.AuthScreen
import com.swiftquantum.presentation.ui.screen.CircuitBuilderScreen
import com.swiftquantum.presentation.ui.screen.HardwareScreen
import com.swiftquantum.presentation.ui.screen.ProfileScreen
import com.swiftquantum.presentation.ui.screen.SimulatorScreen

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Simulator : Screen(
        route = "simulator",
        title = "Simulator",
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayArrow
    )

    data object Circuit : Screen(
        route = "circuit",
        title = "Circuit",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )

    data object Hardware : Screen(
        route = "hardware",
        title = "Hardware",
        selectedIcon = Icons.Filled.Memory,
        unselectedIcon = Icons.Outlined.Memory
    )

    data object Profile : Screen(
        route = "profile",
        title = "Profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    data object Auth : Screen(
        route = "auth",
        title = "Login",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    companion object {
        val bottomNavItems = listOf(Simulator, Circuit, Hardware, Profile)
    }
}

@Composable
fun SwiftQuantumNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
    startDestination: String = if (isLoggedIn) Screen.Simulator.route else Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToMain = {
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

        composable(Screen.Hardware.route) {
            HardwareScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
