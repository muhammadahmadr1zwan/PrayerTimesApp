package com.masjid.prayerapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masjid.prayerapp.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// Glassmorphism modifier
fun Modifier.glassCard() = this
    .background(
        color = GlassBackground,
        shape = RoundedCornerShape(24.dp)
    )
    .border(
        width = 1.dp,
        color = GlassBorder,
        shape = RoundedCornerShape(24.dp)
    )

@Composable
fun PrayerTimesScreen(paddingValues: PaddingValues, viewModel: PrayerTimesViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPrayer by viewModel.currentPrayer.collectAsStateWithLifecycle()
    val nextPrayer by viewModel.nextPrayer.collectAsStateWithLifecycle()
    
    // Floating animation for the main card
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatingY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
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
                    radius = 800f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Header Section
                HeaderSection()
            }
            
            when (uiState) {
                is PrayerTimesUiState.Loading -> {
                    item {
                        LoadingSection()
                    }
                }
                
                is PrayerTimesUiState.Error -> {
                    item {
                        ErrorSection(
                            message = uiState.message,
                            onRetry = { viewModel.retry() }
                        )
                    }
                }
                
                is PrayerTimesUiState.Success -> {
                    item {
                        // Next Prayer Hero Card
                        nextPrayer?.let { prayer ->
                            NextPrayerHeroCard(
                                prayer = prayer,
                                modifier = Modifier.offset(y = floatingY.dp)
                            )
                        }
                    }
                    
                    item {
                        // Today's Date Card
                        TodaysDateCard()
                    }
                    
                    item {
                        // Prayer Times Grid
                        PrayerTimesGrid(
                            prayers = uiState.prayers,
                            currentPrayer = currentPrayer,
                            nextPrayer = nextPrayer
                        )
                    }
                    
                    item {
                        // Quick Stats
                        QuickStatsSection()
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    val currentTime by produceState(initialValue = LocalTime.now()) {
        while (true) {
            value = LocalTime.now()
            kotlinx.coroutines.delay(1000)
        }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "As-Salamu Alaikum",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp),
                    tint = MutedForeground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Indianapolis, IN",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedForeground
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                color = Foreground
            )
            Text(
                text = "Local Time",
                style = MaterialTheme.typography.bodySmall,
                color = MutedForeground
            )
        }
    }
}

@Composable
fun NextPrayerHeroCard(
    prayer: Prayer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "NEXT PRAYER",
                        style = MaterialTheme.typography.labelMedium,
                        color = Accent,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prayer.name,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Foreground
                    )
                    Text(
                        text = "in 2 hours 15 minutes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedForeground
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = prayer.athan,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        ),
                        color = Primary
                    )
                    Text(
                        text = "Iqamah: ${prayer.iqamah}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedForeground
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress Bar
            Column {
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Primary,
                    trackColor = Muted,
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Started",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedForeground
                    )
                    Text(
                        text = "65%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedForeground
                    )
                }
            }
        }
    }
}

@Composable
fun TodaysDateCard() {
    val currentDate = LocalDate.now()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(20.dp),
                    tint = Accent
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Foreground
                    )
                    Text(
                        text = currentDate.format(
                            DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedForeground
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Ramadan 3",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Primary
                )
                Text(
                    text = "1446 AH",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedForeground
                )
            }
        }
    }
}

@Composable
fun PrayerTimesGrid(
    prayers: List<Prayer>,
    currentPrayer: Prayer?,
    nextPrayer: Prayer?
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(400.dp) // Fixed height for grid
    ) {
        items(prayers) { prayer ->
            PrayerTimeCard(
                prayer = prayer,
                isCurrentPrayer = prayer == currentPrayer,
                isNextPrayer = prayer == nextPrayer
            )
        }
    }
}

@Composable
fun PrayerTimeCard(
    prayer: Prayer,
    isCurrentPrayer: Boolean,
    isNextPrayer: Boolean
) {
    val icon = getPrayerIcon(prayer.name)
    val progress = if (isCurrentPrayer) 65 else 0
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard()
            .then(
                if (isCurrentPrayer) {
                    Modifier.border(
                        width = 2.dp,
                        brush = Brush.linearGradient(listOf(Primary, Accent)),
                        shape = RoundedCornerShape(24.dp)
                    )
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPrayer) Primary.copy(alpha = 0.1f) else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = prayer.name,
                    modifier = Modifier.size(20.dp),
                    tint = when {
                        isCurrentPrayer -> Primary
                        isNextPrayer -> Accent
                        else -> MutedForeground
                    }
                )
                
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = when {
                                isCurrentPrayer -> Primary
                                isNextPrayer -> Accent
                                else -> Muted
                            },
                            shape = CircleShape
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = prayer.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Foreground
            )
            
            Text(
                text = prayer.athan,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                color = Foreground
            )
            
            if (prayer.iqamah.isNotEmpty()) {
                Text(
                    text = "Iqamah: ${prayer.iqamah}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedForeground
                )
            }
            
            // Mini progress ring for current prayer
            if (isCurrentPrayer && progress > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.size(32.dp),
                        color = Primary,
                        strokeWidth = 2.dp,
                        trackColor = Muted
                    )
                    Text(
                        text = "${progress}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Primary
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(80.dp)
    ) {
        items(
            listOf(
                Triple("4/6", "Prayers Today", Primary),
                Triple("58°", "Qibla Direction", Accent),
                Triple("28", "Day Streak", Primary)
            )
        ) { (value, label, color) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCard(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = color
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedForeground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Primary,
                strokeWidth = 4.dp,
                trackColor = Muted
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Loading prayer times...",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedForeground
            )
        }
    }
}

@Composable
fun ErrorSection(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCard(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                modifier = Modifier.size(48.dp),
                tint = Destructive
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Unable to load prayer times",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Foreground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedForeground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = PrimaryForeground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Retry",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again")
            }
        }
    }
}

@Composable
fun getPrayerIcon(prayerName: String): ImageVector {
    return when (prayerName.lowercase()) {
        "fajr" -> Icons.Default.WbTwilight
        "dhuhr", "zuhr" -> Icons.Default.WbSunny
        "asr" -> Icons.Default.LightMode
        "maghrib" -> Icons.Default.Sunset
        "isha" -> Icons.Default.NightsStay
        else -> Icons.Default.AccessTime
    }
} 