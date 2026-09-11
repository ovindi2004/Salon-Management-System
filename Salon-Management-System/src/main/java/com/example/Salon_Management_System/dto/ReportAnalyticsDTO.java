package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportAnalyticsDTO {

    private long totalAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long pendingAppointments;

    private long noShowAppointments;

    private long totalCustomers;

    private long activeCustomers;

    private long newCustomers;

    private BigDecimal totalRevenue;

    private BigDecimal paidRevenue;

    private BigDecimal pendingAmount;

    private BigDecimal refundedAmount;

    private long totalStaff;

    private long totalServices;
}