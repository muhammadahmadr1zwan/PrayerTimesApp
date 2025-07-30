package com.masjid.prayerapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@WebMvcTest(PrayerController.class)
public class PrayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrayerService prayerService;

    @Test
    public void testGetPrayerTimes() throws Exception {
        String date = "2025-06-29";
        PrayerTimeResponse response = new PrayerTimeResponse(
            date,
            Arrays.asList(
                new Prayer("Fajr", "04:43:00", "05:03:00"),
                new Prayer("Sunrise", "06:20:00", "06:20:00"),
                new Prayer("Dhuhr", "13:48:00", "14:08:00"),
                new Prayer("Asr", "17:47:00", "18:07:00"),
                new Prayer("Maghrib", "21:17:00", "21:22:00"),
                new Prayer("Isha", "22:54:00", "23:14:00")
            )
        );
        given(prayerService.getPrayerTimesForDate(date)).willReturn(response);

        mockMvc.perform(get("/api/prayer-times/" + date))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date").value(date))
            .andExpect(jsonPath("$.prayers").isArray())
            .andExpect(jsonPath("$.prayers[0].name").value("Fajr"))
            .andExpect(jsonPath("$.prayers[0].athan").value("04:43:00"))
            .andExpect(jsonPath("$.prayers[0].iqamah").value("05:03:00"));
    }
} 