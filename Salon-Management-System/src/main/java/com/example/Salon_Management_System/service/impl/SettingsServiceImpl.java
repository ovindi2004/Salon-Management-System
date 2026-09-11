package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.SettingsResponseDTO;
import com.example.Salon_Management_System.dto.SettingsSaveDTO;
import com.example.Salon_Management_System.dto.SettingsUpdateDTO;
import com.example.Salon_Management_System.entity.Settings;
import com.example.Salon_Management_System.repository.SettingsRepository;
import com.example.Salon_Management_System.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;


    // =========================================================
    // SAVE SETTINGS
    // =========================================================

    @Override
    public SettingsResponseDTO saveSettings(SettingsSaveDTO dto) {

        Settings settings = new Settings();

        settings.setSalonName(dto.getSalonName());
        settings.setTagline(dto.getTagline());
        settings.setPhone(dto.getPhone());
        settings.setEmail(dto.getEmail());
        settings.setAddress(dto.getAddress());

        settings.setOpeningTime(dto.getOpeningTime());
        settings.setClosingTime(dto.getClosingTime());

        settings.setAppointmentDuration(dto.getAppointmentDuration());
        settings.setCancellationPolicy(dto.getCancellationPolicy());

        settings.setCurrency(dto.getCurrency());

        settings.setEmailNotifications(dto.getEmailNotifications());
        settings.setSmsNotifications(dto.getSmsNotifications());
        settings.setReminderEnabled(dto.getReminderEnabled());

        Settings savedSettings = settingsRepository.save(settings);

        return mapToResponseDTO(savedSettings);
    }


    // =========================================================
    // GET SETTINGS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SettingsResponseDTO getSettings() {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (settings == null) {
            return null;
        }

        return mapToResponseDTO(settings);
    }


    // =========================================================
    // GET SETTINGS BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SettingsResponseDTO getSettingsById(Long settingsId) {

        Settings settings = settingsRepository.findById(settingsId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Settings not found with ID: " + settingsId
                        )
                );

        return mapToResponseDTO(settings);
    }


    // =========================================================
    // UPDATE SETTINGS
    // =========================================================

    @Override
    public SettingsResponseDTO updateSettings(SettingsUpdateDTO dto) {

        Settings settings = settingsRepository.findById(dto.getSettingsId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Settings not found with ID: "
                                        + dto.getSettingsId()
                        )
                );

        settings.setSalonName(dto.getSalonName());
        settings.setTagline(dto.getTagline());
        settings.setPhone(dto.getPhone());
        settings.setEmail(dto.getEmail());
        settings.setAddress(dto.getAddress());

        settings.setOpeningTime(dto.getOpeningTime());
        settings.setClosingTime(dto.getClosingTime());

        settings.setAppointmentDuration(dto.getAppointmentDuration());
        settings.setCancellationPolicy(dto.getCancellationPolicy());

        settings.setCurrency(dto.getCurrency());

        settings.setEmailNotifications(dto.getEmailNotifications());
        settings.setSmsNotifications(dto.getSmsNotifications());
        settings.setReminderEnabled(dto.getReminderEnabled());

        Settings updatedSettings = settingsRepository.save(settings);

        return mapToResponseDTO(updatedSettings);
    }


    // =========================================================
    // DELETE SETTINGS
    // =========================================================

    @Override
    public void deleteSettings(Long settingsId) {

        if (!settingsRepository.existsById(settingsId)) {
            throw new RuntimeException(
                    "Settings not found with ID: " + settingsId
            );
        }

        settingsRepository.deleteById(settingsId);
    }


    // =========================================================
    // ENTITY → RESPONSE DTO
    // =========================================================

    private SettingsResponseDTO mapToResponseDTO(Settings settings) {

        SettingsResponseDTO dto = new SettingsResponseDTO();

        dto.setSettingsId(settings.getSettingsId());

        // Salon Information
        dto.setSalonName(settings.getSalonName());
        dto.setTagline(settings.getTagline());
        dto.setPhone(settings.getPhone());
        dto.setEmail(settings.getEmail());
        dto.setAddress(settings.getAddress());

        // Business Hours
        dto.setOpeningTime(settings.getOpeningTime());
        dto.setClosingTime(settings.getClosingTime());

        // Appointment Settings
        dto.setAppointmentDuration(
                settings.getAppointmentDuration()
        );

        dto.setCancellationPolicy(
                settings.getCancellationPolicy()
        );

        // Currency
        dto.setCurrency(settings.getCurrency());

        // Notifications
        dto.setEmailNotifications(
                settings.getEmailNotifications()
        );

        dto.setSmsNotifications(
                settings.getSmsNotifications()
        );

        dto.setReminderEnabled(
                settings.getReminderEnabled()
        );

        // System Information
        dto.setCreatedAt(settings.getCreatedAt());
        dto.setUpdatedAt(settings.getUpdatedAt());

        return dto;
    }
}