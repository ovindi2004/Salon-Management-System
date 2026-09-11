package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.AppointmentAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportDTO;
import com.example.Salon_Management_System.dto.RevenueAnalyticsDTO;
import com.example.Salon_Management_System.dto.ServicePerformanceDTO;
import com.example.Salon_Management_System.dto.StaffPerformanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    ReportDTO saveReport(ReportDTO dto);

    ReportDTO getReportById(Long reportId);

    List<ReportDTO> getAllReports();

    List<ReportDTO> getReportsByType(String reportType);

    List<ReportDTO> getReportsByDateRange(LocalDate fromDate, LocalDate toDate);

    void deleteReport(Long reportId);

    ReportAnalyticsDTO getOverallAnalytics(LocalDate fromDate, LocalDate toDate);

    List<RevenueAnalyticsDTO> getRevenueAnalytics(LocalDate fromDate, LocalDate toDate);

    AppointmentAnalyticsDTO getAppointmentAnalytics(LocalDate fromDate, LocalDate toDate);

    List<StaffPerformanceDTO> getStaffPerformance(LocalDate fromDate, LocalDate toDate);

    List<ServicePerformanceDTO> getServicePerformance(LocalDate fromDate, LocalDate toDate);
}