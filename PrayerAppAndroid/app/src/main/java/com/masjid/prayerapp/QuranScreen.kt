package com.masjid.prayerapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun QuranScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    var lastReadSurah by remember { mutableStateOf<Surah?>(null) }
    var lastReadAyah by remember { mutableStateOf(1) }
    
    // Load last read from preferences
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)
        val lastSurahNumber = prefs.getInt("last_surah", 1)
        val lastAyah = prefs.getInt("last_ayah", 1)
        lastReadSurah = getSurahList().find { it.number == lastSurahNumber }
        lastReadAyah = lastAyah
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
                QuranHeader()
            }
            
            item {
                LastReadCard(
                    lastReadSurah = lastReadSurah,
                    lastReadAyah = lastReadAyah,
                    onContinue = { surah ->
                        // Open Quran reading app or website
                        openQuranReader(context, surah.number, lastReadAyah)
                    }
                )
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
                SurahCard(
                    surah = surah,
                    onSurahClick = { clickedSurah ->
                        // Mark as last read and open
                        markAsLastRead(context, clickedSurah.number, 1)
                        openQuranReader(context, clickedSurah.number, 1)
                    },
                    onPlayClick = { clickedSurah ->
                        // Play recitation
                        playSurahRecitation(context, clickedSurah)
                    }
                )
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
fun LastReadCard(
    lastReadSurah: Surah?,
    lastReadAyah: Int,
    onContinue: (Surah) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F9DFF) // IMCA blue
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { lastReadSurah?.let { onContinue(it) } },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Last Read",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Last Read",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = lastReadSurah?.let { "Surah ${it.name} - Ayah $lastReadAyah" } ?: "No reading history",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
            
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
fun SurahCard(
    surah: Surah,
    onSurahClick: (Surah) -> Unit,
    onPlayClick: (Surah) -> Unit
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
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Surah details
            Column(
                modifier = Modifier.weight(1f)
                    .clickable { onSurahClick(surah) }
            ) {
                Text(
                    text = surah.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF8FAFC)
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = surah.arabicName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8)
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${surah.verses} verses • ${surah.revelationType}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8)
                    )
                )
            }
            
            // Play button
            IconButton(
                onClick = { onPlayClick(surah) }
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
    val revelationType: String,
    val englishTranslation: String = "",
    val description: String = ""
)

fun getSurahList(): List<Surah> {
    return listOf(
        Surah(1, "Al-Fatiha", "الفاتحة", 7, "Meccan", "The Opening", "The first chapter of the Quran"),
        Surah(2, "Al-Baqarah", "البقرة", 286, "Medinan", "The Cow", "The longest chapter of the Quran"),
        Surah(3, "Aal-Imran", "آل عمران", 200, "Medinan", "The Family of Imran", "Discusses the family of Imran"),
        Surah(4, "An-Nisa", "النساء", 176, "Medinan", "The Women", "Discusses women's rights and family law"),
        Surah(5, "Al-Ma'idah", "المائدة", 120, "Medinan", "The Table Spread", "Discusses food laws and the Last Supper"),
        Surah(6, "Al-An'am", "الأنعام", 165, "Meccan", "The Cattle", "Discusses monotheism and the Day of Judgment"),
        Surah(7, "Al-A'raf", "الأعراف", 206, "Meccan", "The Heights", "Discusses the story of Adam and Eve"),
        Surah(8, "Al-Anfal", "الأنفال", 75, "Medinan", "The Spoils of War", "Discusses the Battle of Badr"),
        Surah(9, "At-Tawbah", "التوبة", 129, "Medinan", "The Repentance", "Discusses warfare and repentance"),
        Surah(10, "Yunus", "يونس", 109, "Meccan", "Jonah", "Discusses the story of Prophet Jonah"),
        Surah(36, "Ya-Sin", "يس", 83, "Meccan", "Yasin", "The heart of the Quran"),
        Surah(55, "Ar-Rahman", "الرحمن", 78, "Meccan", "The Most Gracious", "Discusses Allah's mercy and blessings"),
        Surah(67, "Al-Mulk", "الملك", 30, "Meccan", "The Sovereignty", "Discusses Allah's power and creation"),
        Surah(112, "Al-Ikhlas", "الإخلاص", 4, "Meccan", "The Sincerity", "Discusses the oneness of Allah"),
        Surah(113, "Al-Falaq", "الفلق", 5, "Meccan", "The Daybreak", "A protection chapter"),
        Surah(114, "An-Nas", "الناس", 6, "Meccan", "The Mankind", "A protection chapter")
    )
}

// Helper functions for Quran functionality
fun markAsLastRead(context: Context, surahNumber: Int, ayahNumber: Int) {
    context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)
        .edit()
        .putInt("last_surah", surahNumber)
        .putInt("last_ayah", ayahNumber)
        .apply()
}

fun openQuranReader(context: Context, surahNumber: Int, ayahNumber: Int) {
    // Try to open a Quran app, or fall back to a website
    try {
        // Try to open popular Quran apps
        val quranAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse("quran://$surahNumber:$ayahNumber"))
        context.startActivity(quranAppIntent)
    } catch (e: Exception) {
        // Fall back to website
        val websiteUrl = "https://quran.com/$surahNumber/$ayahNumber"
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        context.startActivity(webIntent)
    }
}

fun playSurahRecitation(context: Context, surah: Surah) {
    // Try to play recitation from various sources
    val recitationUrl = "https://audio1.islamhouse.com/quran/abdul-basit-abdul-samad/${surah.number.toString().padStart(3, '0')}.mp3"
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recitationUrl))
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fall back to YouTube search
        val youtubeUrl = "https://www.youtube.com/results?search_query=${surah.name}+recitation"
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        context.startActivity(webIntent)
    }
} 