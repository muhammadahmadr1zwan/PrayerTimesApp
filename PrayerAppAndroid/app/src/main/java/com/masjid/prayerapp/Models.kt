package com.masjid.prayerapp

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Prayer(
    val name: String,
    val athan: String,
    val iqamah: String
)

@JsonClass(generateAdapter = true)
data class PrayerTimeResponse(
    val date: String,
    val prayers: List<Prayer>
) 