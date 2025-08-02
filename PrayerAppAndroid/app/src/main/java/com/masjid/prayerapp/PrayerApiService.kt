package com.masjid.prayerapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class PrayerApiService(private val baseUrl: String = "https://prayer-app-backend-vozu.onrender.com/api/prayer-times/") {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val responseAdapter = moshi.adapter(PrayerTimeResponse::class.java)

    @Throws(IOException::class)
    suspend fun getTodayPrayerTimes(): PrayerTimeResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "${baseUrl}today"
                Log.d("PrayerApiService", "Making request to: $url")
                
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    Log.d("PrayerApiService", "Response code: ${response.code}")
                    Log.d("PrayerApiService", "Response message: ${response.message}")
                    
                    if (!response.isSuccessful) {
                        val errorMsg = "HTTP ${response.code}: ${response.message}"
                        Log.e("PrayerApiService", errorMsg)
                        throw IOException(errorMsg)
                    }
                    
                    val body = response.body?.string()
                    Log.d("PrayerApiService", "Response body: $body")
                    
                    if (body == null) {
                        Log.e("PrayerApiService", "Response body is null")
                        throw IOException("Response body is null")
                    }
                    
                    // Test parsing with a simple approach first
                    try {
                        // Try to parse as raw JSON first
                        Log.d("PrayerApiService", "Attempting to parse JSON...")
                        val result = responseAdapter.fromJson(body)
                        Log.d("PrayerApiService", "Parsed result: $result")
                        if (result != null) {
                            Log.d("PrayerApiService", "Prayers count: ${result.prayers.size}")
                            result.prayers.forEach { prayer ->
                                Log.d("PrayerApiService", "Prayer: ${prayer.name} - ${prayer.athan} - ${prayer.iqamah}")
                            }
                        }
                        result
                    } catch (e: Exception) {
                        Log.e("PrayerApiService", "Failed to parse JSON: ${e.message}", e)
                        Log.e("PrayerApiService", "Raw response was: $body")
                        throw IOException("Failed to parse JSON: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("PrayerApiService", "Network error: ${e.message}", e)
                throw e
            }
        }
    }
    
    @Throws(IOException::class)
    suspend fun getPrayerTimes(date: String): PrayerTimeResponse? {
        return withContext(Dispatchers.IO) {
            val url = "$baseUrl$date"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("HTTP ${response.code}: ${response.message}")
                }
                val body = response.body?.string()
                if (body == null) {
                    throw IOException("Response body is null")
                }
                try {
                    responseAdapter.fromJson(body)
                } catch (e: Exception) {
                    throw IOException("Failed to parse JSON: ${e.message}")
                }
            }
        }
    }
    
    @Throws(IOException::class)
    suspend fun getTomorrowPrayerTimes(): PrayerTimeResponse? {
        return withContext(Dispatchers.IO) {
            val url = "${baseUrl}tomorrow"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("HTTP ${response.code}: ${response.message}")
                }
                val body = response.body?.string()
                if (body == null) {
                    throw IOException("Response body is null")
                }
                try {
                    responseAdapter.fromJson(body)
                } catch (e: Exception) {
                    throw IOException("Failed to parse JSON: ${e.message}")
                }
            }
        }
    }
    
    @Throws(IOException::class)
    suspend fun getJummahInfo(): PrayerTimeResponse? {
        return withContext(Dispatchers.IO) {
            val url = "${baseUrl}jummah"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("HTTP ${response.code}: ${response.message}")
                }
                val body = response.body?.string()
                if (body == null) {
                    throw IOException("Response body is null")
                }
                try {
                    responseAdapter.fromJson(body)
                } catch (e: Exception) {
                    throw IOException("Failed to parse JSON: ${e.message}")
                }
            }
        }
    }
} 