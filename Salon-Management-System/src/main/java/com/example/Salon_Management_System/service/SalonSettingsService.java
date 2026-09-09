package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.SalonSettingsDTO;

public interface SalonSettingsService {

    SalonSettingsDTO getSettings();

    SalonSettingsDTO updateSettings(SalonSettingsDTO dto);

    void resetSettings();

    void updateTwoFactor(boolean enabled);
}