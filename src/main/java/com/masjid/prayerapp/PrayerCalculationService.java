package com.masjid.prayerapp;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrayerCalculationService {
    
    // IMCA (Indianapolis Muslim Community Association) coordinates
    private static final double LATITUDE = 39.7684; // Indianapolis, IN
    private static final double LONGITUDE = -86.1581;
    private static final String TIMEZONE = "America/Indiana/Indianapolis";
    
    // Prayer time calculation constants
    private static final double FAJR_ANGLE = 18.0; // Fajr angle
    private static final double ISHA_ANGLE = 18.0; // Isha angle (18 degrees)
    private static final double ASR_FACTOR = 1.0; // Asr calculation factor
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            
            // Calculate prayer times using astronomical formulas
            LocalTime fajr = calculateFajr(date);
            LocalTime sunrise = calculateSunrise(date);
            LocalTime dhuhr = calculateDhuhr(date);
            LocalTime asr = calculateAsr(date);
            LocalTime maghrib = calculateMaghrib(date);
            LocalTime isha = calculateIsha(date);
            
            List<Prayer> prayers = new ArrayList<>();
            
            // Add each prayer time with appropriate iqamah delays
            prayers.add(createPrayer("Fajr", fajr, 20));
            prayers.add(createPrayer("Sunrise", sunrise, 0)); // No iqamah for sunrise
            prayers.add(createPrayer("Dhuhr", dhuhr, 20));
            prayers.add(createPrayer("Asr", asr, 20));
            prayers.add(createPrayer("Maghrib", maghrib, 5)); // 5 minutes for Maghrib
            prayers.add(createPrayer("Isha", isha, 20));
            
            return new PrayerTimeResponse(dateStr, prayers);
            
        } catch (Exception e) {
            // Fallback to empty response if calculation fails
            return new PrayerTimeResponse(dateStr, new ArrayList<>());
        }
    }
    
    private LocalTime calculateFajr(LocalDate date) {
        // Simplified Fajr calculation (18 degrees before sunrise)
        LocalTime sunrise = calculateSunrise(date);
        return sunrise.minusMinutes(18 * 4); // Approximate 18 degrees = 72 minutes
    }
    
    private LocalTime calculateSunrise(LocalDate date) {
        // Simplified sunrise calculation for Indianapolis
        // This is a basic approximation - in production you'd use more accurate astronomical calculations
        int dayOfYear = date.getDayOfYear();
        double offset = Math.sin(Math.toRadians((dayOfYear - 80) * 360.0 / 365.0)) * 30;
        int baseHour = 6;
        int baseMinute = 30;
        int totalMinutes = (int)(baseHour * 60 + baseMinute + offset);
        
        return LocalTime.of(totalMinutes / 60, totalMinutes % 60);
    }
    
    private LocalTime calculateDhuhr(LocalDate date) {
        // Dhuhr is approximately solar noon
        return LocalTime.of(12, 0);
    }
    
    private LocalTime calculateAsr(LocalDate date) {
        // Asr is typically 3-4 hours after Dhuhr
        return LocalTime.of(15, 45);
    }
    
    private LocalTime calculateMaghrib(LocalDate date) {
        // Maghrib is sunset (opposite of sunrise)
        LocalTime sunrise = calculateSunrise(date);
        int totalMinutes = sunrise.getHour() * 60 + sunrise.getMinute();
        int sunsetMinutes = (24 * 60) - totalMinutes;
        
        return LocalTime.of(sunsetMinutes / 60, sunsetMinutes % 60);
    }
    
    private LocalTime calculateIsha(LocalDate date) {
        // Isha is typically 1.5-2 hours after Maghrib
        LocalTime maghrib = calculateMaghrib(date);
        return maghrib.plusHours(1).plusMinutes(30);
    }
    
    private Prayer createPrayer(String name, LocalTime prayerTime, int iqamahDelayMinutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = prayerTime.format(formatter);
        
        // Calculate Iqamah time
        LocalTime iqamahTime = prayerTime.plusMinutes(iqamahDelayMinutes);
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