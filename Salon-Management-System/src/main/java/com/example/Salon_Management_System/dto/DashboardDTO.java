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

    // =========================================================
    // DATE RANGE
    // =========================================================

    private LocalDate fromDate;
    private LocalDate toDate;


    // =========================================================
    // REVENUE SUMMARY
    // =========================================================

    private BigDecimal totalRevenue;
    private BigDecimal paidRevenue;
    private BigDecimal pendingAmount;
    private BigDecimal refundedAmount;


    // =========================================================
    // APPOINTMENT SUMMARY
    // =========================================================

    private long totalAppointments;
    private long scheduledAppointments;
    private long confirmedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
    private long noShowAppointments;


    // =========================================================
    // CUSTOMER SUMMARY
    // =========================================================

    private long totalCustomers;
    private long activeCustomers;
    private long newCustomers;


    // =========================================================
    // STAFF & SERVICE SUMMARY
    // =========================================================

    private long totalStaff;
    private long totalServices;


    // =========================================================
    // TODAY'S SUMMARY
    // =========================================================

    private long todayAppointments;
    private long todayScheduled;
    private long todayConfirmed;
    private long todayCompleted;
    private long todayCancelled;
    private long todayNoShow;

    private BigDecimal todayRevenue;


    // =========================================================
    // CHART / TABLE DATA
    // =========================================================

    private List<DashboardRevenueDTO> revenueAnalytics;

    private List<DashboardStaffDTO> topStaff;

    private List<DashboardServiceDTO> topServices;

    private List<DashboardAppointmentDTO> recentAppointments;
}