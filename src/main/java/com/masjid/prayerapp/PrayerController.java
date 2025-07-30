package com.masjid.prayerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
} 