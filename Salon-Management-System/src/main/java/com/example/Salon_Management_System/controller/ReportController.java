package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.AppointmentAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportDTO;
import com.example.Salon_Management_System.dto.RevenueAnalyticsDTO;
import com.example.Salon_Management_System.dto.ServicePerformanceDTO;
import com.example.Salon_Management_System.dto.StaffPerformanceDTO;
import com.example.Salon_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;


    // ============================================================
    // SAVE REPORT
    // POST: /api/v1/reports/save
    // ============================================================

    @PostMapping("/save")
    public ResponseEntity<?> saveReport(
            @RequestBody ReportDTO dto) {

        try {

            ReportDTO savedReport =
                    reportService.saveReport(dto);

            return ResponseEntity.ok(savedReport);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET REPORT BY ID
    // GET: /api/v1/reports/{reportId}
    // ============================================================

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(
            @PathVariable Long reportId) {

        try {

            ReportDTO report =
                    reportService.getReportById(reportId);

            return ResponseEntity.ok(report);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET ALL REPORTS
    // GET: /api/v1/reports/all
    // ============================================================

    @GetMapping("/all")
    public ResponseEntity<?> getAllReports() {

        try {

            List<ReportDTO> reports =
                    reportService.getAllReports();

            return ResponseEntity.ok(reports);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET REPORTS BY TYPE
    // GET: /api/v1/reports/type/{reportType}
    // ============================================================

    @GetMapping("/type/{reportType}")
    public ResponseEntity<?> getReportsByType(
            @PathVariable String reportType) {

        try {

            List<ReportDTO> reports =
                    reportService.getReportsByType(reportType);

            return ResponseEntity.ok(reports);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET REPORTS BY DATE RANGE
    // GET: /api/v1/reports/date-range
    //
    // Example:
    // /api/v1/reports/date-range?fromDate=2026-09-01&toDate=2026-09-10
    // ============================================================

    @GetMapping("/date-range")
    public ResponseEntity<?> getReportsByDateRange(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            List<ReportDTO> reports =
                    reportService.getReportsByDateRange(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(reports);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // DELETE REPORT
    // DELETE: /api/v1/reports/{reportId}
    // ============================================================

    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(
            @PathVariable Long reportId) {

        try {

            reportService.deleteReport(reportId);

            return ResponseEntity.ok(
                    "Report deleted successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // OVERALL ANALYTICS
    // GET: /api/v1/reports/analytics/overall
    //
    // Example:
    // /api/v1/reports/analytics/overall
    //      ?fromDate=2026-09-01
    //      &toDate=2026-09-10
    // ============================================================

    @GetMapping("/analytics/overall")
    public ResponseEntity<?> getOverallAnalytics(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            ReportAnalyticsDTO analytics =
                    reportService.getOverallAnalytics(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(analytics);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // REVENUE ANALYTICS
    // GET: /api/v1/reports/analytics/revenue
    // ============================================================

    @GetMapping("/analytics/revenue")
    public ResponseEntity<?> getRevenueAnalytics(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            List<RevenueAnalyticsDTO> revenue =
                    reportService.getRevenueAnalytics(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(revenue);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // APPOINTMENT ANALYTICS
    // GET: /api/v1/reports/analytics/appointments
    // ============================================================

    @GetMapping("/analytics/appointments")
    public ResponseEntity<?> getAppointmentAnalytics(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            AppointmentAnalyticsDTO analytics =
                    reportService.getAppointmentAnalytics(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(analytics);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // STAFF PERFORMANCE
    // GET: /api/v1/reports/analytics/staff
    // ============================================================

    @GetMapping("/analytics/staff")
    public ResponseEntity<?> getStaffPerformance(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            List<StaffPerformanceDTO> performance =
                    reportService.getStaffPerformance(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(performance);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // SERVICE PERFORMANCE
    // GET: /api/v1/reports/analytics/services
    // ============================================================

    @GetMapping("/analytics/services")
    public ResponseEntity<?> getServicePerformance(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        try {

            List<ServicePerformanceDTO> performance =
                    reportService.getServicePerformance(
                            fromDate,
                            toDate
                    );

            return ResponseEntity.ok(performance);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}