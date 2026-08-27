package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffWorkingHourDTO {
    private Long workingHourId;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean dayOff;
}
