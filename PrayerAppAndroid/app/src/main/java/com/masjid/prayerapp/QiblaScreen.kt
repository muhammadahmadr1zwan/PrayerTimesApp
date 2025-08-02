package com.masjid.prayerapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlin.math.*

@Composable
fun QiblaScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var qiblaDirection by remember { mutableStateOf(0f) }
    var distanceToKaaba by remember { mutableStateOf(0.0) }
    var accuracy by remember { mutableStateOf(0f) }
    
    // Request location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    
    // Check and request permission on first load
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                hasLocationPermission = true
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    
    // Get location and calculate Qibla direction
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    currentLocation = location
                    accuracy = location.accuracy
                    
                    // Calculate Qibla direction
                    val qiblaResult = calculateQiblaDirection(location.latitude, location.longitude)
                    qiblaDirection = qiblaResult.first
                    distanceToKaaba = qiblaResult.second
                }
                
                override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L, // Update every second
                    1f, // Update every meter
                    locationListener
                )
            } catch (e: SecurityException) {
                // Handle permission denied
            }
        }
    }
    
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
            CompassCard(qiblaDirection)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Location Info
            LocationInfoCard(
                currentLocation = currentLocation,
                distanceToKaaba = distanceToKaaba,
                qiblaDirection = qiblaDirection,
                accuracy = accuracy,
                hasPermission = hasLocationPermission
            )
        }
    }
}

@Composable
fun CompassCard(qiblaDirection: Float) {
    var compassRotation by remember { mutableStateOf(0f) }
    
    // Compass sensor
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    compassRotation = -it.values[0] // Negative for correct rotation
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        sensorManager.registerListener(
            sensorEventListener,
            compassSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }
    
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
            
            // Compass needle with Qibla direction
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .rotate(compassRotation)
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
                    .rotate(qiblaDirection - compassRotation),
                tint = Color(0xFFD4AF37) // Gold color
            )
        }
    }
}

@Composable
fun LocationInfoCard(
    currentLocation: Location?,
    distanceToKaaba: Double,
    qiblaDirection: Float,
    accuracy: Float,
    hasPermission: Boolean
) {
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
            
            if (!hasPermission) {
                Text(
                    text = "Location permission required for Qibla direction",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFEF4444)
                    ),
                    textAlign = TextAlign.Center
                )
            } else if (currentLocation == null) {
                Text(
                    text = "Getting your location...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8)
                    ),
                    textAlign = TextAlign.Center
                )
            } else {
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
                            text = "${String.format("%.0f", distanceToKaaba)} km",
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
                        text = "${String.format("%.0f", qiblaDirection)}°",
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
                        text = "±${String.format("%.0f", accuracy)}°",
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
}

// Calculate Qibla direction from current location to Kaaba
fun calculateQiblaDirection(latitude: Double, longitude: Double): Pair<Float, Double> {
    // Kaaba coordinates (Mecca, Saudi Arabia)
    val kaabaLat = 21.4225
    val kaabaLng = 39.8262
    
    // Convert to radians
    val lat1 = Math.toRadians(latitude)
    val lng1 = Math.toRadians(longitude)
    val lat2 = Math.toRadians(kaabaLat)
    val lng2 = Math.toRadians(kaabaLng)
    
    // Calculate bearing
    val deltaLng = lng2 - lng1
    val y = sin(deltaLng) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLng)
    val bearing = atan2(y, x)
    
    // Convert to degrees and normalize
    val bearingDegrees = Math.toDegrees(bearing)
    val normalizedBearing = (bearingDegrees + 360) % 360
    
    // Calculate distance using Haversine formula
    val earthRadius = 6371.0 // Earth's radius in kilometers
    val dLat = lat2 - lat1
    val dLng = lng2 - lng1
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(lat1) * cos(lat2) * sin(dLng / 2) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = earthRadius * c
    
    return Pair(normalizedBearing.toFloat(), distance)
} 