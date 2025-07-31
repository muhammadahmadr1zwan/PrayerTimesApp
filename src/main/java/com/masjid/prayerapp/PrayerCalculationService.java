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
    
    // IMCA (Indianapolis Muslim Community Association) coordinates
    private static final double DEFAULT_LATITUDE = 39.7684; // Indianapolis, IN
    private static final double DEFAULT_LONGITUDE = -86.1581;
    private static final String DEFAULT_TIMEZONE = "America/Indiana/Indianapolis";
    
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
            prayers.add(createPrayer("Fajr", prayerTimes.fajr, 20));
            prayers.add(createPrayer("Sunrise", prayerTimes.sunrise, 0)); // No iqamah for sunrise
            prayers.add(createPrayer("Dhuhr", prayerTimes.dhuhr, 20));
            prayers.add(createPrayer("Asr", prayerTimes.asr, 20));
            prayers.add(createPrayer("Maghrib", prayerTimes.maghrib, 5)); // 5 minutes for Maghrib
            prayers.add(createPrayer("Isha", prayerTimes.isha, 20));
            
            return new PrayerTimeResponse(dateStr, prayers);
            
        } catch (Exception e) {
            // Fallback to empty response if calculation fails
            return new PrayerTimeResponse(dateStr, new ArrayList<>());
        }
    }
    
    private Prayer createPrayer(String name, Date prayerTime, int iqamahDelayMinutes) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
            prayerTime.toInstant(), 
            ZoneId.of(DEFAULT_TIMEZONE)
        );
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = localDateTime.format(formatter);
        
        // Calculate Iqamah time based on prayer type
        LocalDateTime iqamahTime = localDateTime.plusMinutes(iqamahDelayMinutes);
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