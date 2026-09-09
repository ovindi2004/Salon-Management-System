package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salon_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalonSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // General
    @Column(nullable = false)
    private String systemName;

    private String tagline;

    private String defaultLanguage;

    private String currency;

    private String timeZone;

    private String dateFormat;

    private String timeFormat;

    // Salon Profile
    private String salonName;

    private String salonEmail;

    private String salonPhone;

    private String salonAddress;

    private String salonWebsite;

    private String openingTime;

    private String closingTime;

    @Column(length = 500)
    private String workingDays;

    @Column(length = 1000)
    private String logoUrl;

    // Appointment
    private Integer appointmentDuration;

    private Integer bookingAdvance;

    private Integer maxDailyAppointments;

    @Column(length = 2000)
    private String cancellationPolicy;

    private Boolean sameDayBooking;

    private Boolean onlineBooking;

    private Boolean autoConfirm;

    // Payment
    @Column(length = 500)
    private String paymentMethods;

    private String defaultPaymentMethod;

    private Double taxRate;

    private Boolean onlinePayments;

    private Boolean paymentConfirmation;

    // Appearance
    private String theme;

    private String accentColor;

    private Boolean compactSidebar;

    private Boolean enableAnimations;

    // Security
    private Boolean twoFactorEnabled;

    // Notifications
    private Boolean appointmentNotification;

    private Boolean cancellationNotification;

    private Boolean newCustomerNotification;

    private Boolean paymentNotification;

    private Boolean feedbackNotification;

    private Boolean staffNotification;

    private Boolean emailNotification;

    private Boolean systemNotification;

    private Boolean smsNotification;
}