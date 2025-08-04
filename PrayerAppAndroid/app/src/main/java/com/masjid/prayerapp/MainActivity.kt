package com.masjid.prayerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masjid.prayerapp.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            PrayerAppTheme {
                IMCAApp()
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCAApp() {
    val context = LocalContext.current
    val prayerViewModel: PrayerTimesViewModel = viewModel { PrayerTimesViewModel(context) }
    
    var selectedTab by remember { mutableStateOf(0) }
    
    // Updated tabs matching the React template
    val tabs = listOf(
        NavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "home"),
        NavigationItem("Prayers", Icons.Filled.Schedule, Icons.Outlined.Schedule, "prayers"),
        NavigationItem("Dhikr", Icons.Filled.Add, Icons.Outlined.Add, "dhikr"),
        NavigationItem("Qibla", Icons.Filled.Explore, Icons.Outlined.Explore, "qibla"),
        NavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "settings")
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.1f),
                        Background,
                        Background
                    ),
                    radius = 1000f
                )
            )
    ) {
        // Background gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Primary.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.8f, 0.2f),
                        radius = 800f
                    )
                )
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Accent.copy(alpha = 0.03f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.2f, 0.8f),
                        radius = 600f
                    )
                )
        )
        
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                ModernBottomNavigation(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { paddingValues ->
            when (selectedTab) {
                0 -> PrayerTimesScreen(paddingValues, prayerViewModel)
                1 -> PrayerDetailScreen(paddingValues)
                2 -> DhikrScreen(paddingValues)
                3 -> QiblaScreen(paddingValues)
                4 -> SettingsScreen(paddingValues)
            }
        }
    }
}

@Composable
fun ModernBottomNavigation(
    tabs: List<NavigationItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .glassCard(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    
                    Button(
                        onClick = { onTabSelected(index) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Primary else Color.Transparent,
                            contentColor = if (isSelected) PrimaryForeground else MutedForeground
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = if (isSelected) 4.dp else 0.dp
                        ),
                        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// Placeholder screens to match the React template structure
@Composable
fun PrayerDetailScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .glassCard(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Crescent Moon Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Primary.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Background, CircleShape)
                            .offset(x = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Fajr",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = Foreground
                )
                
                Text(
                    text = "04:27",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = Foreground
                )
                
                Text(
                    text = "- 05:45:39",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun DhikrScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .glassCard(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Use regular gradient without brush since text brush isn't available in Compose yet
                Text(
                    text = "Dhikr Counter",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Keep your heart connected with remembrance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedForeground
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Dhikr counter implementation would go here
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun QiblaScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .glassCard(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Qibla Direction",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MutedForeground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Indianapolis, IN → Mecca",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedForeground
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Qibla compass implementation would go here
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .glassCard(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Customize your prayer experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedForeground
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Settings implementation would go here
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
            }
        }
    }
} 