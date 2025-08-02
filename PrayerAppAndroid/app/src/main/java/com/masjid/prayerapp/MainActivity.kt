package com.masjid.prayerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMCAApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCAApp() {
    val context = LocalContext.current
    val prayerViewModel: PrayerViewModel = viewModel { PrayerViewModel(context = context) }
    
    var selectedTab by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        TabItem("Prayer Times", Icons.Default.Home),
        TabItem("Qibla", Icons.Default.LocationOn),
        TabItem("Quran", Icons.Default.Book),
        TabItem("Settings", Icons.Default.Settings)
    )
    
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF4F9DFF),
            surface = Color(0xFF0A0F1C),
            onSurface = Color(0xFFF8FAFC)
        )
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF1A2332),
                    contentColor = Color(0xFFF8FAFC)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            icon = { Icon(tab.icon, contentDescription = tab.title) },
                            label = { 
                                Text(
                                    text = tab.title,
                                    textAlign = TextAlign.Center
                                ) 
                            },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }
        ) { paddingValues ->
            when (selectedTab) {
                0 -> PrayerTimesScreen(paddingValues)
                1 -> QiblaScreen(paddingValues)
                2 -> QuranScreen(paddingValues)
                3 -> SettingsScreen(paddingValues)
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) 