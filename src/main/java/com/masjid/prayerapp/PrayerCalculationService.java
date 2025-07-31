package com.masjid.prayerapp;

import com.batoulapps.adhan.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrayerCalculationService {
    
    // Default coordinates for a central location (you can make this configurable)
    private static final double DEFAULT_LATITUDE = 40.7128; // New York City
    private static final double DEFAULT_LONGITUDE = -74.0060;
    private static final String DEFAULT_TIMEZONE = "America/New_York";
    
    // Prayer time calculation method (using Muslim World League)
    private static final CalculationMethod CALCULATION_METHOD = CalculationMethod.MUSLIM_WORLD_LEAGUE;
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            Coordinates coordinates = new Coordinates(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            
            // Create prayer times for the given date
            PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, CALCULATION_METHOD);
            
            List<Prayer> prayers = new ArrayList<>();
            
            // Add each prayer time
            prayers.add(createPrayer("Fajr", prayerTimes.fajr));
            prayers.add(createPrayer("Sunrise", prayerTimes.sunrise));
            prayers.add(createPrayer("Dhuhr", prayerTimes.dhuhr));
            prayers.add(createPrayer("Asr", prayerTimes.asr));
            prayers.add(createPrayer("Maghrib", prayerTimes.maghrib));
            prayers.add(createPrayer("Isha", prayerTimes.isha));
            
            return new PrayerTimeResponse(dateStr, prayers);
            
        } catch (Exception e) {
            // Fallback to empty response if calculation fails
            return new PrayerTimeResponse(dateStr, new ArrayList<>());
        }
    }
    
    private Prayer createPrayer(String name, Date prayerTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
            prayerTime.toInstant(), 
            ZoneId.of(DEFAULT_TIMEZONE)
        );
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = localDateTime.format(formatter);
        
        // Add 20 minutes for Iqamah (you can make this configurable)
        LocalDateTime iqamahTime = localDateTime.plusMinutes(20);
        String iqamahStr = iqamahTime.format(formatter);
        
        return new Prayer(name, timeStr, iqamahStr);
    }
    
    // Method to get prayer times for today
    public PrayerTimeResponse getTodayPrayerTimes() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return calculatePrayerTimesForDate(today);
    }
    
    // Method to get prayer times for tomorrow
    public PrayerTimeResponse getTomorrowPrayerTimes() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        return calculatePrayerTimesForDate(tomorrow);
    }
} 