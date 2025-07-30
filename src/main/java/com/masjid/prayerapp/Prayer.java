package com.masjid.prayerapp;

public class Prayer {
    private String name;
    private String athan;
    private String iqamah;

    public Prayer() {}

    public Prayer(String name, String athan, String iqamah) {
        this.name = name;
        this.athan = athan;
        this.iqamah = iqamah;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAthan() {
        return athan;
    }

    public void setAthan(String athan) {
        this.athan = athan;
    }

    public String getIqamah() {
        return iqamah;
    }

    public void setIqamah(String iqamah) {
        this.iqamah = iqamah;
    }
} 