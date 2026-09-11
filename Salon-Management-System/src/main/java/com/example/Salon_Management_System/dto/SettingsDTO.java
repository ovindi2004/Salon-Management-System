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

    private String salonName;
    private String tagline;
    private String phone;
    private String email;
    private String address;


    private LocalTime openingTime;
    private LocalTime closingTime;


    private Integer appointmentDuration;
    private String cancellationPolicy;


    private String currency;


    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean reminderEnabled;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}