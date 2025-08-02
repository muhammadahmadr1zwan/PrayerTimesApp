package com.masjid.prayerapp

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrayerViewModel(
    private val apiService: PrayerApiService = PrayerApiService(),
    private val context: Context? = null
) : ViewModel() {
    
    private val _prayers = MutableStateFlow<List<Prayer>>(emptyList())
    val prayers: StateFlow<List<Prayer>> = _prayers.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()
    
    init {
        Log.d("PrayerViewModel", "Initializing PrayerViewModel")
        getCurrentLocation()
        fetchTodayPrayers()
    }
    
    private fun getCurrentLocation() {
        context?.let { ctx ->
            try {
                val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                
                // Try to get last known location from GPS provider
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLocation != null) {
                    Log.d("PrayerViewModel", "Got GPS location: ${gpsLocation.latitude}, ${gpsLocation.longitude}")
                    _currentLocation.value = gpsLocation
                    return
                }
                
                // Try network provider as fallback
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLocation != null) {
                    Log.d("PrayerViewModel", "Got network location: ${networkLocation.latitude}, ${networkLocation.longitude}")
                    _currentLocation.value = networkLocation
                    return
                }
                
                Log.d("PrayerViewModel", "No location available")
            } catch (e: Exception) {
                Log.e("PrayerViewModel", "Error getting location: ${e.message}", e)
            }
        }
    }
    
    private fun fetchTodayPrayers() {
        Log.d("PrayerViewModel", "Starting to fetch today's prayers")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                Log.d("PrayerViewModel", "Making API call to get today's prayers")
                
                val location = _currentLocation.value
                val response = if (location != null) {
                    Log.d("PrayerViewModel", "Using location-based prayer times: ${location.latitude}, ${location.longitude}")
                    apiService.getTodayPrayerTimesWithLocation(location.latitude, location.longitude)
                } else {
                    Log.d("PrayerViewModel", "Using default prayer times (no location available)")
                    apiService.getTodayPrayerTimes()
                }
                
                Log.d("PrayerViewModel", "API response received: $response")
                
                if (response == null) {
                    Log.e("PrayerViewModel", "Response is null - API returned no data")
                    _error.value = "Response is null - API returned no data"
                    _prayers.value = emptyList()
                } else if (response.prayers.isEmpty()) {
                    Log.w("PrayerViewModel", "No prayer times available for today")
                    _error.value = "No prayer times available for today"
                    _prayers.value = emptyList()
                } else {
                    Log.d("PrayerViewModel", "Successfully loaded ${response.prayers.size} prayers")
                    _prayers.value = response.prayers
                    _error.value = null
                    
                    // Schedule notifications if context is available
                    context?.let { ctx ->
                        schedulePrayerNotifications(ctx, response.prayers)
                    }
                }
            } catch (e: Exception) {
                Log.e("PrayerViewModel", "Failed to load prayer times: ${e.message}", e)
                _error.value = "Failed to load prayer times: ${e.message} (${e.javaClass.simpleName})"
                _prayers.value = emptyList()
            } finally {
                _isLoading.value = false
                Log.d("PrayerViewModel", "Finished loading prayers. Loading: ${_isLoading.value}, Error: ${_error.value}, Prayers count: ${_prayers.value.size}")
            }
        }
    }
    
    fun retry() {
        Log.d("PrayerViewModel", "Retrying to fetch prayers")
        getCurrentLocation() // Refresh location
        fetchTodayPrayers()
    }
    
    fun schedulePrayerNotifications(context: Context, prayers: List<Prayer>) {
        try {
            // Get notification time from settings
            val notificationTime = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getInt("notification_time_minutes", 15)
            
            // Check if notifications are enabled
            val notificationsEnabled = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("prayer_notifications_enabled", false)
            
            if (notificationsEnabled) {
                Log.d("PrayerViewModel", "Scheduling prayer notifications for ${prayers.size} prayers")
                PrayerNotificationService.schedulePrayerNotifications(context, prayers, notificationTime)
            } else {
                Log.d("PrayerViewModel", "Prayer notifications are disabled")
                PrayerNotificationService.cancelAllNotifications(context)
            }
        } catch (e: Exception) {
            Log.e("PrayerViewModel", "Failed to schedule notifications: ${e.message}", e)
        }
    }
    
    fun enablePrayerNotifications(context: Context, enabled: Boolean) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("prayer_notifications_enabled", enabled)
            .apply()
        
        if (enabled) {
            schedulePrayerNotifications(context, _prayers.value)
        } else {
            PrayerNotificationService.cancelAllNotifications(context)
        }
    }
    
    fun setNotificationTime(context: Context, minutes: Int) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putInt("notification_time_minutes", minutes)
            .apply()
        
        // Reschedule notifications with new time
        if (_prayers.value.isNotEmpty()) {
            schedulePrayerNotifications(context, _prayers.value)
        }
    }
} 