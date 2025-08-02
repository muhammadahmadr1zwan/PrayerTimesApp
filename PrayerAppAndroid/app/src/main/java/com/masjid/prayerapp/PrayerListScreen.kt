package com.masjid.prayerapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun PrayerTimesScreen(paddingValues: PaddingValues) {
    val viewModel: PrayerViewModel = viewModel()
    val prayers by viewModel.prayers
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = Color(0xFF0A0F1C) // IMCA dark background
    ) {
        when {
            isLoading -> {
                LoadingScreen()
            }
            error != null -> {
                ErrorState(
                    error = error ?: "Unknown error",
                    onRetry = { viewModel.retry() }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 24.dp)
                ) {
                    item {
                        IMCAHeader()
                    }
                    
                    item {
                        CurrentPrayerCard(prayers)
                    }
                    
                    items(prayers) { prayer ->
                        PrayerCard(prayer = prayer)
                    }
                }
            }
        }
    }
}

@Composable
fun IMCAHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // IMCA Logo/Title
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
                Text(
                    text = "IMCA",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F9DFF) // IMCA blue
                    ),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Indianapolis Muslim Community Association",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Today's date
                val today = LocalDate.now()
                val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
                val formattedDate = today.format(dateFormatter)
                
                Text(
                    text = dayOfWeek,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4F9DFF)
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFFF8FAFC)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CurrentPrayerCard(prayers: List<Prayer>) {
    // Find current prayer based on actual time
    val currentPrayer = getCurrentPrayer(prayers)
    
    currentPrayer?.let { prayer ->
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
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Current Prayer",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Current Prayer",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = prayer.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Iqamah: ${prayer.iqamah}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun getCurrentPrayer(prayers: List<Prayer>): Prayer? {
    val currentTime = java.time.LocalTime.now()
    
    // Convert prayer times to LocalTime for comparison
    val prayerTimes = prayers.mapNotNull { prayer ->
        try {
            val timeStr = prayer.athan.replace(" AM", "").replace(" PM", "")
            val (hour, minute, second) = timeStr.split(":").map { it.toInt() }
            
            // Convert to 24-hour format
            val hour24 = when {
                prayer.athan.contains("PM") && hour != 12 -> hour + 12
                prayer.athan.contains("AM") && hour == 12 -> 0
                else -> hour
            }
            
            prayer to java.time.LocalTime.of(hour24, minute, second)
        } catch (e: Exception) {
            null
        }
    }
    
    // Find the next prayer time
    val nextPrayer = prayerTimes.find { (_, time) -> time > currentTime }
    
    // If no next prayer found today, return the first prayer (for tomorrow)
    return nextPrayer?.first ?: prayers.firstOrNull()
}

@Composable
fun PrayerCard(prayer: Prayer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Prayer icon and name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = getPrayerIcon(prayer.name),
                    contentDescription = prayer.name,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF4F9DFF)
                )
                Text(
                    text = prayer.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Prayer times
            Row(
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Athan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = prayer.athan,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF8FAFC)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iqamah",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = prayer.iqamah,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF8FAFC)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun getPrayerIcon(prayerName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (prayerName.lowercase()) {
        "fajr" -> Icons.Default.Home
        "sunrise" -> Icons.Default.Home
        "dhuhr" -> Icons.Default.Home
        "asr" -> Icons.Default.Home
        "maghrib" -> Icons.Default.Home
        "isha" -> Icons.Default.Home
        else -> Icons.Default.Home
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4F9DFF)
            )
            Text(
                text = "Loading prayer times...",
                color = Color(0xFFF8FAFC),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFFEF4444)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Unable to load prayer times",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF8FAFC)
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF94A3B8)
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F9DFF)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry")
        }
    }
} 