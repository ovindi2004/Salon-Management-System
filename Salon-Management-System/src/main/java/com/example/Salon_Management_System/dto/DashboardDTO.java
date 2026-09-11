package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {



    private LocalDate fromDate;

    private LocalDate toDate;

    private BigDecimal totalRevenue;

    private BigDecimal paidRevenue;

    private BigDecimal pendingAmount;

    private BigDecimal refundedAmount;

    private long totalAppointments;

    private long scheduledAppointments;

    private long confirmedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long noShowAppointments;

    private long totalCustomers;

    private long activeCustomers;

    private long newCustomers;

    private long totalStaff;

    private long totalServices;

    private long todayAppointments;

    private long todayScheduled;

    private long todayConfirmed;

    private long todayCompleted;

    private long todayCancelled;

    private long todayNoShow;

    private BigDecimal todayRevenue;

    private List<DashboardRevenueDTO> revenueAnalytics;

    private List<DashboardStaffDTO> topStaff;

    private List<DashboardServiceDTO> topServices;

    private List<DashboardAppointmentDTO> recentAppointments;
}