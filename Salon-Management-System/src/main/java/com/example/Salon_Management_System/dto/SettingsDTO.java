package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDTO {

    private Long settingsId;

    // Salon Information
    private String salonName;
    private String tagline;
    private String phone;
    private String email;
    private String address;

    // Business Hours
    private LocalTime openingTime;
    private LocalTime closingTime;

    // Appointment Settings
    private Integer appointmentDuration;
    private String cancellationPolicy;

    // Currency
    private String currency;

    // Notification Settings
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean reminderEnabled;

    // System Information
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}