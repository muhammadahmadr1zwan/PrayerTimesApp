package com.masjid.prayerapp.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark color scheme matching the React template
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = PrimaryForeground,
    primaryContainer = Primary,
    onPrimaryContainer = PrimaryForeground,
    secondary = Secondary,
    onSecondary = SecondaryForeground,
    tertiary = Accent,
    onTertiary = AccentForeground,
    background = Background,
    onBackground = Foreground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = Destructive,
    onError = DestructiveForeground,
    outline = Border,
    outlineVariant = GlassBorder
)

// Light color scheme (fallback, but we'll primarily use dark)
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = PrimaryForeground,
    primaryContainer = Primary,
    onPrimaryContainer = PrimaryForeground,
    secondary = Secondary,
    onSecondary = SecondaryForeground,
    tertiary = Accent,
    onTertiary = AccentForeground,
    background = Background,
    onBackground = Foreground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = Destructive,
    onError = DestructiveForeground,
    outline = Border,
    outlineVariant = GlassBorder
)

@Composable
fun PrayerAppTheme(
    darkTheme: Boolean = true, // Always use dark theme like the template
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable to use our custom colors
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
            window.statusBarColor = Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 