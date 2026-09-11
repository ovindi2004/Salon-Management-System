package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardAppointmentDTO {

    private Long appointmentId;

    private String customerName;

    private String serviceName;

    private String staffName;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private Integer duration;

    private String status;
}