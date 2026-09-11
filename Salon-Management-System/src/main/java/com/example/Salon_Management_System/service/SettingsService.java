package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.SettingsDTO;
import com.example.Salon_Management_System.dto.SettingsResponseDTO;
import com.example.Salon_Management_System.dto.SettingsSaveDTO;
import com.example.Salon_Management_System.dto.SettingsUpdateDTO;

public interface SettingsService {

    SettingsResponseDTO saveSettings(SettingsSaveDTO dto);

    SettingsResponseDTO getSettings();

    SettingsResponseDTO getSettingsById(Long settingsId);

    SettingsResponseDTO updateSettings(SettingsUpdateDTO dto);

    void deleteSettings(Long settingsId);
}