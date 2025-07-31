package com.masjid.prayerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrayerService {
    private List<PrayerCsvRecord> allRecords = new ArrayList<>();
    private final PrayerCalculationService calculationService;
    
    @Autowired
    public PrayerService(PrayerCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostConstruct
    public void loadCsv() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("prayer_times.csv")) {
            if (is == null) return;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = reader.readLine(); // skip header
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        allRecords.add(new PrayerCsvRecord(parts[0], parts[1], parts[2], parts[3]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrayerTimeResponse getPrayerTimesForDate(String date) {
        // First try to get from CSV (for historical data or backup)
        List<Prayer> csvPrayers = allRecords.stream()
                .filter(r -> r.date.equals(date))
                .map(r -> new Prayer(r.name, r.athan, r.iqamah))
                .collect(Collectors.toList());
        
        // If CSV has data, use it (for historical dates)
        if (!csvPrayers.isEmpty()) {
            return new PrayerTimeResponse(date, csvPrayers);
        }
        
        // Otherwise, calculate prayer times automatically
        return calculationService.calculatePrayerTimesForDate(date);
    }
    
    // New method to get today's prayer times
    public PrayerTimeResponse getTodayPrayerTimes() {
        return calculationService.getTodayPrayerTimes();
    }
    
    // New method to get tomorrow's prayer times
    public PrayerTimeResponse getTomorrowPrayerTimes() {
        return calculationService.getTomorrowPrayerTimes();
    }

    private static class PrayerCsvRecord {
        String date, name, athan, iqamah;
        PrayerCsvRecord(String date, String name, String athan, String iqamah) {
            this.date = date;
            this.name = name;
            this.athan = athan;
            this.iqamah = iqamah;
        }
    }
} 