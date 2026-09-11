package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePerformanceDTO {

    private Long serviceId;

    private String serviceName;

    private String category;

    private BigDecimal price;

    private long totalAppointments;

    private long completedAppointments;

    private long cancelledAppointments;
}