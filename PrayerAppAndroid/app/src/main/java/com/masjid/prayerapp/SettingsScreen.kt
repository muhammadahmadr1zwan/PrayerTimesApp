package com.masjid.prayerapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.ContextCompat

@Composable
fun SettingsScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    var prayerNotificationsEnabled by remember { mutableStateOf(false) }
    var adhanSoundEnabled by remember { mutableStateOf(false) }
    var autoDetectLocationEnabled by remember { mutableStateOf(false) }
    var darkThemeEnabled by remember { mutableStateOf(true) }
    var notificationTime by remember { mutableStateOf(15) }
    
    // Check notification permission
    var hasNotificationPermission by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    
    // Permission launchers
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            prayerNotificationsEnabled = true
            createNotificationChannel(context)
        }
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            autoDetectLocationEnabled = true
        }
    }
    
    // Check permissions on first load
    LaunchedEffect(Unit) {
        hasNotificationPermission = ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = Color(0xFF0A0F1C) // IMCA dark background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                SettingsHeader()
            }
            
            item {
                Text(
                    text = "Prayer Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.Notifications,
                    title = "Prayer Notifications",
                    subtitle = "Get notified before prayer times",
                    isSwitch = true,
                    isEnabled = prayerNotificationsEnabled,
                    onToggle = { enabled ->
                        if (enabled && !hasNotificationPermission) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            prayerNotificationsEnabled = enabled
                            if (enabled) {
                                createNotificationChannel(context)
                            }
                        }
                    }
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.VolumeUp,
                    title = "Adhan Sound",
                    subtitle = "Play adhan at prayer times",
                    isSwitch = true,
                    isEnabled = adhanSoundEnabled,
                    onToggle = { enabled ->
                        adhanSoundEnabled = enabled
                        if (enabled && !hasNotificationPermission) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.Schedule,
                    title = "Notification Time",
                    subtitle = "$notificationTime minutes before prayer",
                    isSwitch = false,
                    isEnabled = false,
                    onToggle = { },
                    onClick = {
                        // Show time picker dialog
                        showNotificationTimeDialog(context) { time ->
                            notificationTime = time
                        }
                    }
                )
            }
            
            item {
                Text(
                    text = "Location Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.LocationOn,
                    title = "Current Location",
                    subtitle = "Indianapolis, IN",
                    isSwitch = false,
                    isEnabled = false,
                    onToggle = { },
                    onClick = {
                        // Open location settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", context.packageName, null)
                        context.startActivity(intent)
                    }
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.MyLocation,
                    title = "Auto-detect Location",
                    subtitle = "Automatically detect your location",
                    isSwitch = true,
                    isEnabled = autoDetectLocationEnabled,
                    onToggle = { enabled ->
                        if (enabled && !hasLocationPermission) {
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        } else {
                            autoDetectLocationEnabled = enabled
                        }
                    }
                )
            }
            
            item {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Theme",
                    subtitle = "Use dark theme for better visibility",
                    isSwitch = true,
                    isEnabled = darkThemeEnabled,
                    onToggle = { enabled ->
                        darkThemeEnabled = enabled
                        // Apply theme change
                        applyTheme(context, enabled)
                    }
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.Translate,
                    title = "Language",
                    subtitle = "English",
                    isSwitch = false,
                    isEnabled = false,
                    onToggle = { },
                    onClick = {
                        // Show language selection dialog
                        showLanguageDialog(context)
                    }
                )
            }
            
            item {
                SettingCard(
                    icon = Icons.Default.Info,
                    title = "About",
                    subtitle = "App version and information",
                    isSwitch = false,
                    isEnabled = false,
                    onToggle = { },
                    onClick = {
                        // Show about dialog
                        showAboutDialog(context)
                    }
                )
            }
            
            item {
                IMCAInfoCard()
            }
        }
    }
}

@Composable
fun SettingsHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4F9DFF)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8FAFC)
                ),
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Customize your prayer experience",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF94A3B8)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SettingCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSwitch: Boolean,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF4F9DFF)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFF8FAFC)
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8)
                    )
                )
            }
            
            if (isSwitch) {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4F9DFF),
                        uncheckedThumbColor = Color(0xFF94A3B8),
                        uncheckedTrackColor = Color(0xFF2A3441)
                    )
                )
            } else {
                IconButton(
                    onClick = { onClick?.invoke() }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

// Helper functions for settings functionality
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "prayer_notifications",
            "Prayer Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for prayer times"
            enableVibration(true)
            enableLights(true)
        }
        
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotificationTimeDialog(context: Context, onTimeSelected: (Int) -> Unit) {
    // This would show a proper time picker dialog
    // For now, we'll just cycle through common values
    val times = listOf(5, 10, 15, 20, 30)
    val currentIndex = times.indexOf(15)
    val nextIndex = (currentIndex + 1) % times.size
    onTimeSelected(times[nextIndex])
}

fun applyTheme(context: Context, isDark: Boolean) {
    // This would apply the theme change
    // For now, we'll just store the preference
    context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .edit()
        .putBoolean("dark_theme", isDark)
        .apply()
}

fun showLanguageDialog(context: Context) {
    // This would show a language selection dialog
    // For now, we'll just show a toast or snackbar
}

fun showAboutDialog(context: Context) {
    // This would show an about dialog with app information
    // For now, we'll just show a toast or snackbar
}

@Composable
fun IMCAInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F9DFF) // IMCA blue
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "IMCA Prayer App",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Indianapolis Muslim Community Association",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "2846 Cold Spring Rd, Indianapolis, IN 46222",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Phone: (317) 855-9934",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { /* TODO: Open website */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Website",
                        tint = Color.White
                    )
                }
                
                IconButton(
                    onClick = { /* TODO: Open phone */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call",
                        tint = Color.White
                    )
                }
                
                IconButton(
                    onClick = { /* TODO: Open email */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.White
                    )
                }
            }
        }
    }
} 