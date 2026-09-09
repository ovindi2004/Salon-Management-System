package com.example.Salon_Management_System.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalonSettingsDTO {

    private Long id;

    // General
    private String systemName;
    private String tagline;
    private String defaultLanguage;
    private String currency;
    private String timeZone;
    private String dateFormat;
    private String timeFormat;

    // Salon
    private String salonName;
    private String salonEmail;
    private String salonPhone;
    private String salonAddress;
    private String salonWebsite;
    private String openingTime;
    private String closingTime;
    private String workingDays;
    private String logoUrl;

    // Appointment
    private Integer appointmentDuration;
    private Integer bookingAdvance;
    private Integer maxDailyAppointments;
    private String cancellationPolicy;
    private Boolean sameDayBooking;
    private Boolean onlineBooking;
    private Boolean autoConfirm;

    // Payment
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