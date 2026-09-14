package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long appointmentId;

    private Long customerId;
    private String customerName;
    private String customerPhone;

    private Long serviceId;
    private String serviceName;
    private BigDecimal servicePrice;

    private Long staffId;
    private String staffName;

    private LocalDate appointmentDate;
    private LocalTime startTime;

    private Integer duration;

    private AppointmentStatus status;

    private String notes;
}