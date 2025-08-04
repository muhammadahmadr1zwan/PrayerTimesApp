package com.masjid.prayerapp;

import com.batoulapps.adhan.*;
import com.batoulapps.adhan.data.DateComponents;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrayerCalculationService {
    
    // IMCA (Indianapolis Muslim Community Association) coordinates - FIXED
    private static final double IMCA_LATITUDE = 39.7684; // Indianapolis, IN
    private static final double IMCA_LONGITUDE = -86.1581;
    private static final String IMCA_TIMEZONE = "America/Indiana/Indianapolis";
    
    // Iqamah delays (in minutes) - IMCA specific
    private static final int FAJR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int DHUHR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int ASR_IQAMAH_DELAY = 20; // 20 min after athan
    private static final int MAGHRIB_IQAMAH_DELAY = 5; // 5 min after athan (special)
    private static final int ISHA_IQAMAH_DELAY = 20; // 20 min after athan
    
    public PrayerTimeResponse calculatePrayerTimesForDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            
            // Create coordinates for Indianapolis
            Coordinates coordinates = new Coordinates(IMCA_LATITUDE, IMCA_LONGITUDE);
            
            // Create date components
            DateComponents dateComponents = new DateComponents(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            
            // Set calculation parameters for North America with Hanafi madhab
            CalculationParameters params = CalculationMethod.NORTH_AMERICA.getParameters();
            params.madhab = Madhab.HANAFI; // IMCA uses Hanafi madhab
            
            // Calculate prayer times using adhan library
            PrayerTimes prayerTimes = new PrayerTimes(coordinates, dateComponents, params);
            
            // Convert to Indianapolis timezone
            ZoneId indianapolisZone = ZoneId.of(IMCA_TIMEZONE);
            
            List<Prayer> prayers = new ArrayList<>();
            
            // Add each prayer time with appropriate iqamah delays
            prayers.add(createPrayerFromDate("Fajr", prayerTimes.fajr, FAJR_IQAMAH_DELAY, indianapolisZone));
            prayers.add(createPrayerFromDate("Dhuhr", prayerTimes.dhuhr, DHUHR_IQAMAH_DELAY, indianapolisZone));
            prayers.add(createPrayerFromDate("Asr", prayerTimes.asr, ASR_IQAMAH_DELAY, indianapolisZone));
            prayers.add(createPrayerFromDate("Maghrib", prayerTimes.maghrib, MAGHRIB_IQAMAH_DELAY, indianapolisZone));
            prayers.add(createPrayerFromDate("Isha", prayerTimes.isha, ISHA_IQAMAH_DELAY, indianapolisZone));
            
            return new PrayerTimeResponse(dateStr, prayers);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to empty response if calculation fails
            return new PrayerTimeResponse(dateStr, new ArrayList<>());
        }
    }
    
    private Prayer createPrayerFromDate(String name, Date prayerDate, int iqamahDelayMinutes, ZoneId timezone) {
        // Convert UTC Date to Indianapolis timezone
        ZonedDateTime athanTime = prayerDate.toInstant().atZone(timezone);
        ZonedDateTime iqamahTime = athanTime.plusMinutes(iqamahDelayMinutes);
        
        // Format times in 12-hour format with AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String athanTimeStr = athanTime.format(formatter);
        String iqamahTimeStr = iqamahTime.format(formatter);
        
        return new Prayer(name, athanTimeStr, iqamahTimeStr);
    }
    
    public PrayerTimeResponse getTodayPrayerTimes() {
        return calculatePrayerTimesForDate(LocalDate.now().toString());
    }
    
    public PrayerTimeResponse getTomorrowPrayerTimes() {
        return calculatePrayerTimesForDate(LocalDate.now().plusDays(1).toString());
    }
} 