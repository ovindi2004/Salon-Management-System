package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingsId;


    // =========================================================
    // SALON INFORMATION
    // =========================================================

    @Column(nullable = false, length = 100)
    private String salonName;

    @Column(length = 255)
    private String tagline;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(length = 500)
    private String address;


    // =========================================================
    // BUSINESS HOURS
    // =========================================================

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;


    // =========================================================
    // APPOINTMENT SETTINGS
    // =========================================================

    @Column(nullable = false)
    private Integer appointmentDuration;


    @Column(length = 1000)
    private String cancellationPolicy;


    // =========================================================
    // CURRENCY
    // =========================================================

    @Column(nullable = false, length = 10)
    private String currency;


    // =========================================================
    // NOTIFICATION SETTINGS
    // =========================================================

    @Column(nullable = false)
    private Boolean emailNotifications = true;

    @Column(nullable = false)
    private Boolean smsNotifications = true;

    @Column(nullable = false)
    private Boolean reminderEnabled = true;


    // =========================================================
    // SYSTEM INFORMATION
    // =========================================================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // =========================================================
    // PRE-PERSIST
    // =========================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (emailNotifications == null) {
            emailNotifications = true;
        }

        if (smsNotifications == null) {
            smsNotifications = true;
        }

        if (reminderEnabled == null) {
            reminderEnabled = true;
        }

        if (currency == null || currency.isBlank()) {
            currency = "LKR";
        }

        if (appointmentDuration == null) {
            appointmentDuration = 60;
        }
    }


    // =========================================================
    // PRE-UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}