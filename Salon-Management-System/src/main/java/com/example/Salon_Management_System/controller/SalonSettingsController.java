package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.SalonSettingsDTO;
import com.example.Salon_Management_System.service.SalonSettingsService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalonSettingsController {

    private final SalonSettingsService service;

    // GET SETTINGS
    @GetMapping
    public ResponseEntity<SalonSettingsDTO> getSettings() {

        return ResponseEntity.ok(
                service.getSettings()
        );
    }

    // UPDATE ALL SETTINGS
    @PutMapping
    public ResponseEntity<SalonSettingsDTO> updateSettings(
            @RequestBody SalonSettingsDTO dto) {

        return ResponseEntity.ok(
                service.updateSettings(dto)
        );
    }

    // RESET SETTINGS
    @PostMapping("/reset")
    public ResponseEntity<String> resetSettings() {

        service.resetSettings();

        return ResponseEntity.ok(
                "Settings reset successfully"
        );
    }

    // 2FA
    @PutMapping("/2fa")
    public ResponseEntity<String> updateTwoFactor(
            @RequestParam boolean enabled) {

        service.updateTwoFactor(enabled);

        return ResponseEntity.ok(
                enabled
                        ? "Two-factor authentication enabled"
                        : "Two-factor authentication disabled"
        );
    }
}