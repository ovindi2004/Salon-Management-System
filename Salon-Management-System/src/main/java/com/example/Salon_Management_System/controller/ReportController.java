package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.AppointmentAnalyticsDTO;
import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.ReportAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportDTO;
import com.example.Salon_Management_System.dto.RevenueAnalyticsDTO;
import com.example.Salon_Management_System.dto.ServicePerformanceDTO;
import com.example.Salon_Management_System.dto.StaffPerformanceDTO;
import com.example.Salon_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveReport(@RequestBody ReportDTO dto) {
        ReportDTO savedReport = reportService.saveReport(dto);
        return new CommonResponse(0, savedReport, "Report saved successfully");
    }

    @GetMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getReportById(@PathVariable Long reportId) {
        ReportDTO report = reportService.getReportById(reportId);
        return new CommonResponse(0, report, "Report retrieved successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return new CommonResponse(0, reports, "All reports retrieved successfully");
    }

    @GetMapping(value = "/type/{reportType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getReportsByType(@PathVariable String reportType) {
        List<ReportDTO> reports = reportService.getReportsByType(reportType);
        return new CommonResponse(0, reports, "Reports retrieved successfully by type");
    }

    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getReportsByDateRange(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        List<ReportDTO> reports = reportService.getReportsByDateRange(fromDate, toDate);
        return new CommonResponse(0, reports, "Reports retrieved successfully for the selected date range");
    }


    @DeleteMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);
        return new CommonResponse(0, null, "Report deleted successfully");
    }


    @GetMapping(value = "/analytics/overall", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOverallAnalytics(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        ReportAnalyticsDTO analytics = reportService.getOverallAnalytics(fromDate, toDate);
        return new CommonResponse(0, analytics, "Overall report analytics retrieved successfully");
    }


    @GetMapping(value = "/analytics/revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRevenueAnalytics(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        List<RevenueAnalyticsDTO> revenue = reportService.getRevenueAnalytics(fromDate, toDate);
        return new CommonResponse(0, revenue, "Revenue analytics retrieved successfully");
    }

    @GetMapping(value = "/analytics/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentAnalytics(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        AppointmentAnalyticsDTO analytics = reportService.getAppointmentAnalytics(fromDate, toDate);
        return new CommonResponse(0, analytics, "Appointment analytics retrieved successfully");
    }


    @GetMapping(value = "/analytics/staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffPerformance(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        List<StaffPerformanceDTO> performance = reportService.getStaffPerformance(fromDate, toDate);
        return new CommonResponse(0, performance, "Staff performance retrieved successfully");
    }

    @GetMapping(value = "/analytics/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServicePerformance(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        List<ServicePerformanceDTO> performance = reportService.getServicePerformance(fromDate, toDate);
        return new CommonResponse(0, performance, "Service performance retrieved successfully");
    }
}