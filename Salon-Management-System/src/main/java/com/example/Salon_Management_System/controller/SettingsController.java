package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.SettingsResponseDTO;
import com.example.Salon_Management_System.dto.SettingsSaveDTO;
import com.example.Salon_Management_System.dto.SettingsUpdateDTO;
import com.example.Salon_Management_System.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
                "http://localhost:*",
                "http://127.0.0.1:*",
                "null"
        }
)
public class SettingsController {

    private final SettingsService settingsService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveSettings(@RequestBody SettingsSaveDTO dto) {
        SettingsResponseDTO response = settingsService.saveSettings(dto);
        return new CommonResponse(0, response, "Settings saved successfully");
    }



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSettings() {
        SettingsResponseDTO response = settingsService.getSettings();
        return new CommonResponse(0, response, "Settings retrieved successfully");
    }


    @GetMapping(value = "/{settingsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSettingsById(@PathVariable Long settingsId) {
        SettingsResponseDTO response = settingsService.getSettingsById(settingsId);
        return new CommonResponse(0, response, "Settings retrieved successfully");
    }


    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateSettings(@RequestBody SettingsUpdateDTO dto) {
        SettingsResponseDTO response = settingsService.updateSettings(dto);
        return new CommonResponse(0, response, "Settings updated successfully");
    }

    @DeleteMapping(value = "/delete/{settingsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteSettings(@PathVariable Long settingsId) {
        settingsService.deleteSettings(settingsId);
        return new CommonResponse(0, null, "Settings deleted successfully");
    }
}