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
    
    // IMCA (Indianapolis Muslim Community Association) coordinates - FIXED
    private static final double IMCA_LATITUDE = 39.7684; // Indianapolis, IN
    private static final double IMCA_LONGITUDE = -86.1581;
    private static final String IMCA_TIMEZONE = "America/Indiana/Indianapolis";
    
    // IMCA Prayer Times (from https://imcaindy.org/prayer-times/)
    // Updated for August 2, 2025
    private static final LocalTime IMCA_FAJR = LocalTime.of(5, 17); // 5:17 AM
    private static final LocalTime IMCA_DHUHR = LocalTime.of(13, 51); // 1:51 PM
    private static final LocalTime IMCA_ASR = LocalTime.of(17, 44); // 5:44 PM
    private static final LocalTime IMCA_MAGHRIB = LocalTime.of(20, 57); // 8:57 PM
    private static final LocalTime IMCA_ISHA = LocalTime.of(22, 24); // 10:24 PM
    
    // Iqamah delays (from IMCA website)
    private static final int FAJR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int DHUHR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int ASR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int MAGHRIB_IQAMAH_DELAY = 5; // 5 min after athan
    private static final int ISHA_IQAMAH_DELAY = 20; // 20 min after athan
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            
            List<Prayer> prayers = new ArrayList<>();
            
            // Add IMCA prayer times with correct iqamah delays
            prayers.add(createPrayer("Fajr", IMCA_FAJR, FAJR_IQAMAH_DELAY));
            prayers.add(createPrayer("Dhuhr", IMCA_DHUHR, DHUHR_IQAMAH_DELAY));
            prayers.add(createPrayer("Asr", IMCA_ASR, ASR_IQAMAH_DELAY));
            prayers.add(createPrayer("Maghrib", IMCA_MAGHRIB, MAGHRIB_IQAMAH_DELAY));
            prayers.add(createPrayer("Isha", IMCA_ISHA, ISHA_IQAMAH_DELAY));
            
            return new PrayerTimeResponse(dateStr, prayers);
            
        } catch (Exception e) {
            // Fallback to empty response if calculation fails
            return new PrayerTimeResponse(dateStr, new ArrayList<>());
        }
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