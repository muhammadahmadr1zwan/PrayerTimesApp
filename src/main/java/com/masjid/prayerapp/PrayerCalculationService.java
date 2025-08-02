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
    
    // Default IMCA (Indianapolis Muslim Community Association) coordinates
    private static final double DEFAULT_LATITUDE = 39.7684; // Indianapolis, IN
    private static final double DEFAULT_LONGITUDE = -86.1581;
    private static final String DEFAULT_TIMEZONE = "America/Indiana/Indianapolis";
    
    // Prayer time calculation constants
    private static final double FAJR_ANGLE = 18.0; // Fajr angle
    private static final double ISHA_ANGLE = 18.0; // Isha angle (18 degrees)
    private static final double ASR_FACTOR = 1.0; // Asr calculation factor
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr) {
        return calculatePrayerTimesForDate(dateStr, DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_TIMEZONE);
    }
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr, double latitude, double longitude, String timezone) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            
            // Calculate prayer times using astronomical formulas for the given location
            LocalTime fajr = calculateFajr(date, latitude, longitude);
            LocalTime sunrise = calculateSunrise(date, latitude, longitude);
            LocalTime dhuhr = calculateDhuhr(date, latitude, longitude);
            LocalTime asr = calculateAsr(date, latitude, longitude);
            LocalTime maghrib = calculateMaghrib(date, latitude, longitude);
            LocalTime isha = calculateIsha(date, latitude, longitude);
            
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
    
    private LocalTime calculateFajr(LocalDate date, double latitude, double longitude) {
        // Simplified Fajr calculation (18 degrees before sunrise)
        LocalTime sunrise = calculateSunrise(date, latitude, longitude);
        return sunrise.minusMinutes(18 * 4); // Approximate 18 degrees = 72 minutes
    }
    
    private LocalTime calculateSunrise(LocalDate date, double latitude, double longitude) {
        // Improved sunrise calculation based on latitude and longitude
        int dayOfYear = date.getDayOfYear();
        
        // Calculate solar declination
        double declination = 23.45 * Math.sin(Math.toRadians((284 + dayOfYear) * 360.0 / 365.0));
        
        // Calculate sunrise hour angle
        double latitudeRad = Math.toRadians(latitude);
        double declinationRad = Math.toRadians(declination);
        double hourAngle = Math.acos(-Math.tan(latitudeRad) * Math.tan(declinationRad));
        
        // Convert to time
        double sunriseHour = 12 - (hourAngle * 180 / Math.PI) / 15.0;
        
        // Adjust for longitude (time zone correction)
        double longitudeCorrection = (longitude / 15.0) - 5.0; // Assuming EST/EDT
        sunriseHour += longitudeCorrection;
        
        int hour = (int) sunriseHour;
        int minute = (int) ((sunriseHour - hour) * 60);
        
        // Ensure valid time
        if (hour < 0) hour += 24;
        if (hour >= 24) hour -= 24;
        if (minute < 0) minute = 0;
        if (minute >= 60) minute = 59;
        
        return LocalTime.of(hour, minute);
    }
    
    private LocalTime calculateDhuhr(LocalDate date, double latitude, double longitude) {
        // Dhuhr is solar noon, adjusted for longitude
        double longitudeCorrection = (longitude / 15.0) - 5.0; // Assuming EST/EDT
        double solarNoon = 12.0 + longitudeCorrection;
        
        int hour = (int) solarNoon;
        int minute = (int) ((solarNoon - hour) * 60);
        
        if (hour < 0) hour += 24;
        if (hour >= 24) hour -= 24;
        
        return LocalTime.of(hour, minute);
    }
    
    private LocalTime calculateAsr(LocalDate date, double latitude, double longitude) {
        // Asr is typically 3-4 hours after Dhuhr, but varies by location
        LocalTime dhuhr = calculateDhuhr(date, latitude, longitude);
        return dhuhr.plusHours(3).plusMinutes(45);
    }
    
    private LocalTime calculateMaghrib(LocalDate date, double latitude, double longitude) {
        // Maghrib is sunset (opposite of sunrise)
        LocalTime sunrise = calculateSunrise(date, latitude, longitude);
        int totalMinutes = sunrise.getHour() * 60 + sunrise.getMinute();
        int sunsetMinutes = (24 * 60) - totalMinutes;
        
        return LocalTime.of(sunsetMinutes / 60, sunsetMinutes % 60);
    }
    
    private LocalTime calculateIsha(LocalDate date, double latitude, double longitude) {
        // Isha is typically 1.5-2 hours after Maghrib
        LocalTime maghrib = calculateMaghrib(date, latitude, longitude);
        return maghrib.plusHours(1).plusMinutes(30);
    }
    
    private Prayer createPrayer(String name, LocalTime prayerTime, int iqamahDelayMinutes) {
        // Force 12-hour format with AM/PM for IMCA prayer times
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String athanTime = prayerTime.format(formatter);
        
        LocalTime iqamahTime = prayerTime.plusMinutes(iqamahDelayMinutes);
        String iqamahTimeStr = iqamahTime.format(formatter);
        
        return new Prayer(name, athanTime, iqamahTimeStr);
    }
    
    public PrayerTimeResponse getTodayPrayerTimes() {
        return calculatePrayerTimesForDate(LocalDate.now().toString());
    }
    
    public PrayerTimeResponse getTomorrowPrayerTimes() {
        return calculatePrayerTimesForDate(LocalDate.now().plusDays(1).toString());
    }
} 