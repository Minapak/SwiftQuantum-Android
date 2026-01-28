package com.swiftquantum.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.swiftquantum.data.repository.BillingRepositoryImpl
import com.swiftquantum.presentation.navigation.Screen
import com.swiftquantum.presentation.navigation.SwiftQuantumNavHost
import com.swiftquantum.presentation.ui.component.DrawerMenuItem
import com.swiftquantum.presentation.ui.component.UnifiedNavigationDrawer
import com.swiftquantum.presentation.ui.theme.SwiftPurple
import com.swiftquantum.presentation.ui.theme.SwiftQuantumTheme
import com.swiftquantum.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var billingRepository: BillingRepositoryImpl

    private var deepLinkUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set activity for billing
        billingRepository.setActivity(this)

        // Handle incoming deep link intent
        handleIntent(intent)

        setContent {
            SwiftQuantumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwiftQuantumMainScreen(deepLinkUri = deepLinkUri)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.let { uri ->
                    if (uri.scheme == "swiftquantum") {
                        deepLinkUri = uri
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        billingRepository.setActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        billingRepository.clearActivity()
    }
}

@Composable
fun SwiftQuantumMainScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    deepLinkUri: Uri? = null
) {
    val authState by authViewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Determine if we should show bottom navigation
    val showBottomBar = remember(currentDestination) {
        currentDestination?.route in Screen.bottomNavItems.map { it.route }
    }

    // SwiftQuantum app-specific drawer menu items
    val drawerMenuItems = remember {
        listOf(
            DrawerMenuItem(
                title = "Circuit Designer",
                icon = Icons.Filled.Memory,
                route = "circuit_designer"
            ),
            DrawerMenuItem(
                title = "Quantum Gates",
                icon = Icons.Filled.GridOn,
                route = "gates"
            ),
            DrawerMenuItem(
                title = "Simulations",
                icon = Icons.Filled.PlayArrow,
                route = "simulations"
            ),
            DrawerMenuItem(
                title = "My Circuits",
                icon = Icons.Filled.Folder,
                route = "my_circuits"
            ),
            DrawerMenuItem(
                title = "Tutorials",
                icon = Icons.Filled.School,
                route = "tutorials"
            )
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            UnifiedNavigationDrawer(
                currentAppName = "SwiftQuantum",
                userDisplayName = authState.user?.name ?: "Quantum User",
                userEmail = authState.user?.email ?: "user@quantum.dev",
                currentAppFeatures = drawerMenuItems,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route)
                },
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Screen.bottomNavItems.forEach { screen ->
                            val selected = currentDestination?.hierarchy?.any {
                                it.route == screen.route
                            } == true

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                        contentDescription = screen.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = screen.title,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SwiftPurple,
                                    selectedTextColor = SwiftPurple,
                                    indicatorColor = SwiftPurple.copy(alpha = 0.1f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                SwiftQuantumNavHost(
                    navController = navController,
                    isLoggedIn = authState.isLoggedIn
                )
            }
        }
    }
}
