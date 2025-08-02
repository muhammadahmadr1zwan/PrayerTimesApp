package com.masjid.prayerapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.util.Log

class PrayerViewModel(private val apiService: PrayerApiService = PrayerApiService()) : ViewModel() {
    private val _prayers = mutableStateOf<List<Prayer>>(emptyList())
    val prayers: State<List<Prayer>> get() = _prayers

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    init {
        Log.d("PrayerViewModel", "Initializing PrayerViewModel")
        fetchTodayPrayers()
    }

    private fun fetchTodayPrayers() {
        Log.d("PrayerViewModel", "Starting to fetch today's prayers")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                Log.d("PrayerViewModel", "Making API call to get today's prayers")
                
                // Use the today endpoint for automatic date handling
                val response = apiService.getTodayPrayerTimes()
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
        fetchTodayPrayers()
    }
} 