package com.masjid.prayerapp;

import java.util.List;

public class PrayerTimeResponse {
    private String date;
    private List<Prayer> prayers;

    public PrayerTimeResponse() {}

    public PrayerTimeResponse(String date, List<Prayer> prayers) {
        this.date = date;
        this.prayers = prayers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Prayer> getPrayers() {
        return prayers;
    }

    public void setPrayers(List<Prayer> prayers) {
        this.prayers = prayers;
    }
} 