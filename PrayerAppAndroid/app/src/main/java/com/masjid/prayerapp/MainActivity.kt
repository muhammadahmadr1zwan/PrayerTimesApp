package com.masjid.prayerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masjid.prayerapp.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            IMCATheme {
                IMCAApp()
            }
        }
    }
}

@Composable
fun IMCATheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = IMCABlue,
        primaryContainer = IMCAMediumBlue,
        secondaryContainer = CardBackground,
        secondary = IMCALightBlue,
        tertiary = IMCAGold,
        background = DarkBackground,
        surface = CardBackground,
        surfaceVariant = SurfaceVariant,
        onPrimary = TextPrimary,
        onPrimaryContainer = OnPrimaryContainer,
        onSecondary = TextPrimary,
        onSecondaryContainer = OnSecondaryContainer,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        onSurfaceVariant = TextSecondary,
        error = ErrorRed,
        errorContainer = Color(0xFF2D1B1B),
        onError = TextPrimary,
        onErrorContainer = OnErrorContainer
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            displayLarge = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            ),
            displayMedium = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            ),
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            ),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onBackground
            ),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onBackground
            ),
            titleLarge = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onBackground
            ),
            titleMedium = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = colorScheme.onBackground
            ),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                color = colorScheme.onSurfaceVariant
            ),
            bodyMedium = MaterialTheme.typography.bodyMedium.copy(
                color = colorScheme.onSurfaceVariant
            ),
            labelLarge = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            )
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCAApp() {
    val context = LocalContext.current
    val prayerViewModel: PrayerTimesViewModel = viewModel { PrayerTimesViewModel(context) }
    
    var selectedTab by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        NavigationItem("Prayer Times", Icons.Filled.Schedule, Icons.Outlined.Schedule),
        NavigationItem("Qibla", Icons.Filled.Explore, Icons.Outlined.Explore),
        NavigationItem("Quran", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        NavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 12.dp,
                modifier = Modifier.height(80.dp)
            ) {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { 
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> PrayerTimesScreen(paddingValues, prayerViewModel)
            1 -> QiblaScreen(paddingValues)
            2 -> QuranScreen(paddingValues)
            3 -> SettingsScreen(paddingValues)
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) 