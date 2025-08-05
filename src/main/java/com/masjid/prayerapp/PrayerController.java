package com.masjid.prayerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/prayer-times")
public class PrayerController {
    private final PrayerService prayerService;
    private final PrayerCalculationService calculationService;

    @Autowired
    public PrayerController(PrayerService prayerService, PrayerCalculationService calculationService) {
        this.prayerService = prayerService;
        this.calculationService = calculationService;
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
    
    @GetMapping("/jummah")
    public PrayerTimeResponse getJummahInfo() {
        return prayerService.getJummahInfo();
    }
    
    /**
     * Get prayer times for the next 7 days - dynamic weekly view
     * No cloud deployment needed - calculates automatically
     */
    @GetMapping("/week")
    public List<PrayerTimeResponse> getWeeklyPrayerTimes() {
        return calculationService.getPrayerTimesForNextWeek();
    }
    
    /**
     * Get prayer times for a specific month - dynamic monthly view
     * Format: /api/prayer-times/month/2025/8 for August 2025
     */
    @GetMapping("/month/{year}/{month}")
    public List<PrayerTimeResponse> getMonthlyPrayerTimes(@PathVariable int year, @PathVariable int month) {
        return calculationService.getPrayerTimesForMonth(year, month);
    }
    
    /**
     * Get prayer times for current month
     */
    @GetMapping("/month/current")
    public List<PrayerTimeResponse> getCurrentMonthPrayerTimes() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return calculationService.getPrayerTimesForMonth(now.getYear(), now.getMonthValue());
    }
    
    /**
     * Get prayer times for next month
     */
    @GetMapping("/month/next")
    public List<PrayerTimeResponse> getNextMonthPrayerTimes() {
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate nextMonth = now.plusMonths(1);
        return calculationService.getPrayerTimesForMonth(nextMonth.getYear(), nextMonth.getMonthValue());
    }
} 