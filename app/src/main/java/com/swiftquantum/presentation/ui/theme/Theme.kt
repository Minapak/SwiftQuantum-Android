package com.swiftquantum.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = QuantumPurple,
    onPrimary = Color.White,
    primaryContainer = QuantumPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = QuantumCyan,
    onSecondary = Color.Black,
    secondaryContainer = QuantumCyanDark,
    onSecondaryContainer = Color.White,
    tertiary = QuantumOrange,
    onTertiary = Color.Black,
    tertiaryContainer = QuantumOrange.copy(alpha = 0.3f),
    onTertiaryContainer = QuantumOrange,
    error = QuantumRed,
    onError = Color.White,
    errorContainer = QuantumRed.copy(alpha = 0.3f),
    onErrorContainer = QuantumRed,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = TextDisabled,
    outlineVariant = SurfaceVariantDark,
    inverseSurface = TextPrimary,
    inverseOnSurface = BackgroundDark,
    inversePrimary = QuantumPurpleDark
)

private val LightColorScheme = lightColorScheme(
    primary = QuantumPurple,
    onPrimary = Color.White,
    primaryContainer = QuantumPurpleContainer,
    onPrimaryContainer = OnQuantumPurpleContainer,
    secondary = QuantumCyanDark,
    onSecondary = Color.White,
    secondaryContainer = QuantumCyanLight.copy(alpha = 0.3f),
    onSecondaryContainer = QuantumCyanDark,
    tertiary = QuantumOrange,
    onTertiary = Color.White,
    tertiaryContainer = QuantumOrange.copy(alpha = 0.2f),
    onTertiaryContainer = QuantumOrange,
    error = QuantumRed,
    onError = Color.White,
    errorContainer = QuantumRed.copy(alpha = 0.2f),
    onErrorContainer = QuantumRed,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    inverseSurface = Color(0xFF1A1A2E),
    inverseOnSurface = Color.White,
    inversePrimary = QuantumPurpleLight
)

@Composable
fun SwiftQuantumTheme(
    darkTheme: Boolean = true, // Default to dark theme for quantum computing aesthetic
    dynamicColor: Boolean = false, // Disable dynamic color to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
