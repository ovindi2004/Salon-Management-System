package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.SalonSettingsDTO;
import com.example.Salon_Management_System.entity.SalonSettings;
import com.example.Salon_Management_System.repository.SalonSettingsRepository;
import com.example.Salon_Management_System.service.SalonSettingsService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SalonSettingsServiceImpl implements SalonSettingsService {

    private final SalonSettingsRepository repository;

    @Override
    public SalonSettingsDTO getSettings() {

        SalonSettings settings = repository.findById(1L)
                .orElseGet(this::createDefaultSettings);

        return convertToDTO(settings);
    }


    @Override
    public SalonSettingsDTO updateSettings(SalonSettingsDTO dto) {

        SalonSettings settings = repository.findById(1L)
                .orElseGet(SalonSettings::new);

        settings.setId(1L);

        // General
        settings.setSystemName(dto.getSystemName());
        settings.setTagline(dto.getTagline());
        settings.setDefaultLanguage(dto.getDefaultLanguage());
        settings.setCurrency(dto.getCurrency());
        settings.setTimeZone(dto.getTimeZone());
        settings.setDateFormat(dto.getDateFormat());
        settings.setTimeFormat(dto.getTimeFormat());

        // Salon
        settings.setSalonName(dto.getSalonName());
        settings.setSalonEmail(dto.getSalonEmail());
        settings.setSalonPhone(dto.getSalonPhone());
        settings.setSalonAddress(dto.getSalonAddress());
        settings.setSalonWebsite(dto.getSalonWebsite());
        settings.setOpeningTime(dto.getOpeningTime());
        settings.setClosingTime(dto.getClosingTime());
        settings.setWorkingDays(dto.getWorkingDays());
        settings.setLogoUrl(dto.getLogoUrl());

        // Appointment
        settings.setAppointmentDuration(dto.getAppointmentDuration());
        settings.setBookingAdvance(dto.getBookingAdvance());
        settings.setMaxDailyAppointments(dto.getMaxDailyAppointments());
        settings.setCancellationPolicy(dto.getCancellationPolicy());
        settings.setSameDayBooking(dto.getSameDayBooking());
        settings.setOnlineBooking(dto.getOnlineBooking());
        settings.setAutoConfirm(dto.getAutoConfirm());

        // Payment
        settings.setPaymentMethods(dto.getPaymentMethods());
        settings.setDefaultPaymentMethod(dto.getDefaultPaymentMethod());
        settings.setTaxRate(dto.getTaxRate());
        settings.setOnlinePayments(dto.getOnlinePayments());
        settings.setPaymentConfirmation(dto.getPaymentConfirmation());

        // Appearance
        settings.setTheme(dto.getTheme());
        settings.setAccentColor(dto.getAccentColor());
        settings.setCompactSidebar(dto.getCompactSidebar());
        settings.setEnableAnimations(dto.getEnableAnimations());

        // Notifications
        settings.setAppointmentNotification(
                dto.getAppointmentNotification());

        settings.setCancellationNotification(
                dto.getCancellationNotification());

        settings.setNewCustomerNotification(
                dto.getNewCustomerNotification());

        settings.setPaymentNotification(
                dto.getPaymentNotification());

        settings.setFeedbackNotification(
                dto.getFeedbackNotification());

        settings.setStaffNotification(
                dto.getStaffNotification());

        settings.setEmailNotification(
                dto.getEmailNotification());

        settings.setSystemNotification(
                dto.getSystemNotification());

        settings.setSmsNotification(
                dto.getSmsNotification());

        // Security
        settings.setTwoFactorEnabled(dto.getTwoFactorEnabled());

        SalonSettings saved = repository.save(settings);

        return convertToDTO(saved);
    }

    @Override
    public void resetSettings() {

        repository.deleteAll();

        createDefaultSettings();
    }

    @Override
    public void updateTwoFactor(boolean enabled) {

        SalonSettings settings = repository.findById(1L)
                .orElseGet(this::createDefaultSettings);

        settings.setTwoFactorEnabled(enabled);

        repository.save(settings);
    }

    private SalonSettings createDefaultSettings() {

        SalonSettings settings = SalonSettings.builder()

                .id(1L)

                // General
                .systemName("PINK BEAUTY SALON")
                .tagline("Luxury Beauty Meets Smart Technology")
                .defaultLanguage("English")
                .currency("LKR")
                .timeZone("Asia/Colombo")
                .dateFormat("DD/MM/YYYY")
                .timeFormat("12")

                // Salon
                .salonName("PINK BEAUTY SALON")
                .salonEmail("info@pinkbeautysalon.com")
                .salonPhone("+94 71 234 5678")
                .salonAddress("Colombo, Sri Lanka")
                .salonWebsite("www.pinkbeautysalon.com")
                .openingTime("09:00")
                .closingTime("18:00")
                .workingDays(
                        "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday"
                )

                // Appointment
                .appointmentDuration(30)
                .bookingAdvance(7)
                .maxDailyAppointments(50)
                .cancellationPolicy(
                        "Appointments can be cancelled up to 24 hours before the scheduled time."
                )
                .sameDayBooking(true)
                .onlineBooking(true)
                .autoConfirm(false)

                // Payment
                .paymentMethods("Cash,Card")
                .defaultPaymentMethod("Cash")
                .taxRate(0.0)
                .onlinePayments(false)
                .paymentConfirmation(true)

                // Appearance
                .theme("light")
                .accentColor("#B76E79")
                .compactSidebar(false)
                .enableAnimations(true)

                // Security
                .twoFactorEnabled(false)

                // Notifications
                .appointmentNotification(true)
                .cancellationNotification(true)
                .newCustomerNotification(true)
                .paymentNotification(true)
                .feedbackNotification(true)
                .staffNotification(false)
                .emailNotification(true)
                .systemNotification(true)
                .smsNotification(false)

                .build();

        return repository.save(settings);
    }

    private SalonSettingsDTO convertToDTO(SalonSettings s) {

        return SalonSettingsDTO.builder()

                .id(s.getId())

                // General
                .systemName(s.getSystemName())
                .tagline(s.getTagline())
                .defaultLanguage(s.getDefaultLanguage())
                .currency(s.getCurrency())
                .timeZone(s.getTimeZone())
                .dateFormat(s.getDateFormat())
                .timeFormat(s.getTimeFormat())

                // Salon
                .salonName(s.getSalonName())
                .salonEmail(s.getSalonEmail())
                .salonPhone(s.getSalonPhone())
                .salonAddress(s.getSalonAddress())
                .salonWebsite(s.getSalonWebsite())
                .openingTime(s.getOpeningTime())
                .closingTime(s.getClosingTime())
                .workingDays(s.getWorkingDays())
                .logoUrl(s.getLogoUrl())

                // Appointment
                .appointmentDuration(s.getAppointmentDuration())
                .bookingAdvance(s.getBookingAdvance())
                .maxDailyAppointments(s.getMaxDailyAppointments())
                .cancellationPolicy(s.getCancellationPolicy())
                .sameDayBooking(s.getSameDayBooking())
                .onlineBooking(s.getOnlineBooking())
                .autoConfirm(s.getAutoConfirm())

                // Payment
                .paymentMethods(s.getPaymentMethods())
                .defaultPaymentMethod(s.getDefaultPaymentMethod())
                .taxRate(s.getTaxRate())
                .onlinePayments(s.getOnlinePayments())
                .paymentConfirmation(s.getPaymentConfirmation())

                // Appearance
                .theme(s.getTheme())
                .accentColor(s.getAccentColor())
                .compactSidebar(s.getCompactSidebar())
                .enableAnimations(s.getEnableAnimations())

                // Security
                .twoFactorEnabled(s.getTwoFactorEnabled())

                // Notifications
                .appointmentNotification(
                        s.getAppointmentNotification())

                .cancellationNotification(
                        s.getCancellationNotification())

                .newCustomerNotification(
                        s.getNewCustomerNotification())

                .paymentNotification(
                        s.getPaymentNotification())

                .feedbackNotification(
                        s.getFeedbackNotification())

                .staffNotification(
                        s.getStaffNotification())

                .emailNotification(
                        s.getEmailNotification())

                .systemNotification(
                        s.getSystemNotification())

                .smsNotification(
                        s.getSmsNotification())

                .build();
    }
}