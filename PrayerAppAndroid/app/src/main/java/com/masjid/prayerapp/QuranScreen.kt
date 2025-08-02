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

@Composable
fun QuranScreen(paddingValues: PaddingValues) {
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
                QuranHeader()
            }
            
            item {
                LastReadCard()
            }
            
            item {
                Text(
                    text = "All Surahs",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            items(getSurahList()) { surah ->
                SurahCard(surah = surah)
            }
        }
    }
}

@Composable
fun QuranHeader() {
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
                imageVector = Icons.Default.Info,
                contentDescription = "Quran",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4F9DFF)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "The Holy Quran",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8FAFC)
                ),
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Read, reflect, and connect with Allah's words",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF94A3B8)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LastReadCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F9DFF) // IMCA blue
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Last Read",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Last Read",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Surah Al-Baqarah - Ayah 255",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Continue",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun SurahCard(surah: Surah) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2332)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Surah number
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFF4F9DFF),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = surah.number.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Surah details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = surah.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = surah.arabicName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${surah.verses} verses • ${surah.revelationType}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Play button
            IconButton(
                onClick = { /* TODO: Play recitation */ }
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color(0xFF4F9DFF)
                )
            }
        }
    }
}

data class Surah(
    val number: Int,
    val name: String,
    val arabicName: String,
    val verses: Int,
    val revelationType: String
)

fun getSurahList(): List<Surah> {
    return listOf(
        Surah(1, "Al-Fatiha", "الفاتحة", 7, "Meccan"),
        Surah(2, "Al-Baqarah", "البقرة", 286, "Medinan"),
        Surah(3, "Aal-Imran", "آل عمران", 200, "Medinan"),
        Surah(4, "An-Nisa", "النساء", 176, "Medinan"),
        Surah(5, "Al-Ma'idah", "المائدة", 120, "Medinan"),
        Surah(6, "Al-An'am", "الأنعام", 165, "Meccan"),
        Surah(7, "Al-A'raf", "الأعراف", 206, "Meccan"),
        Surah(8, "Al-Anfal", "الأنفال", 75, "Medinan"),
        Surah(9, "At-Tawbah", "التوبة", 129, "Medinan"),
        Surah(10, "Yunus", "يونس", 109, "Meccan"),
        Surah(36, "Ya-Sin", "يس", 83, "Meccan"),
        Surah(55, "Ar-Rahman", "الرحمن", 78, "Meccan"),
        Surah(67, "Al-Mulk", "الملك", 30, "Meccan"),
        Surah(112, "Al-Ikhlas", "الإخلاص", 4, "Meccan"),
        Surah(113, "Al-Falaq", "الفلق", 5, "Meccan"),
        Surah(114, "An-Nas", "الناس", 6, "Meccan")
    )
} 