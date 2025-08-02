package com.masjid.prayerapp

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun QiblaScreen(paddingValues: PaddingValues) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = Color(0xFF0A0F1C) // IMCA dark background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
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
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Qibla",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF4F9DFF)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Qibla Direction",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        ),
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "Point your device towards the Kaaba",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF94A3B8)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Compass
            CompassCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Location Info
            LocationInfoCard()
        }
    }
}

@Composable
fun CompassCard() {
    var rotation by remember { mutableStateOf(0f) }
    
    Card(
        modifier = Modifier.size(280.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer ring
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(
                        color = Color(0xFF2A3441),
                        shape = CircleShape
                    )
            )
            
            // Compass needle
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .rotate(rotation)
            ) {
                // North indicator
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "North",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(32.dp),
                    tint = Color(0xFFEF4444)
                )
                
                // South indicator
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "South",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(32.dp),
                    tint = Color(0xFF4F9DFF)
                )
                
                // East indicator
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "East",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp),
                    tint = Color(0xFF10B981)
                )
                
                // West indicator
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "West",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(32.dp),
                    tint = Color(0xFFF59E0B)
                )
            }
            
            // Center dot
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = Color(0xFF4F9DFF),
                        shape = CircleShape
                    )
            )
            
            // Qibla direction indicator
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Qibla",
                modifier = Modifier
                    .size(40.dp)
                    .rotate(rotation + 45f), // Example angle
                tint = Color(0xFFD4AF37) // Gold color
            )
        }
    }
    
    // Simulate compass movement
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(100)
            rotation += 1f
        }
    }
}

@Composable
fun LocationInfoCard() {
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
                text = "Location Information",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8FAFC)
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Location",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8)
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Indianapolis, IN",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Distance to Kaaba",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8)
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "10,847 km",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Qibla Direction",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "45° NE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Accuracy",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "±5°",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 