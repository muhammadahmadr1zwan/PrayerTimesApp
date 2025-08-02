package com.masjid.prayerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prayer-times")
public class PrayerController {
    private final PrayerService prayerService;

    @Autowired
    public PrayerController(PrayerService prayerService) {
        this.prayerService = prayerService;
    }

    @GetMapping("/{date}")
    public PrayerTimeResponse getPrayerTimes(@PathVariable String date) {
        return prayerService.getPrayerTimesForDate(date);
    }
    
    @GetMapping("/today")
    public PrayerTimeResponse getTodayPrayerTimes() {
        return prayerService.getTodayPrayerTimes();
    }
    
    @GetMapping("/tomorrow")
    public PrayerTimeResponse getTomorrowPrayerTimes() {
        return prayerService.getTomorrowPrayerTimes();
    }
    
    // New endpoints with location parameters
    @GetMapping("/today/location")
    public PrayerTimeResponse getTodayPrayerTimesWithLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "America/New_York") String timezone) {
        return prayerService.getTodayPrayerTimesWithLocation(latitude, longitude, timezone);
    }
    
    @GetMapping("/tomorrow/location")
    public PrayerTimeResponse getTomorrowPrayerTimesWithLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "America/New_York") String timezone) {
        return prayerService.getTomorrowPrayerTimesWithLocation(latitude, longitude, timezone);
    }
    
    @GetMapping("/{date}/location")
    public PrayerTimeResponse getPrayerTimesWithLocation(
            @PathVariable String date,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "America/New_York") String timezone) {
        return prayerService.getPrayerTimesForDateWithLocation(date, latitude, longitude, timezone);
    }
    
    @GetMapping("/jummah")
    public PrayerTimeResponse getJummahInfo() {
        return prayerService.getJummahInfo();
    }
} 