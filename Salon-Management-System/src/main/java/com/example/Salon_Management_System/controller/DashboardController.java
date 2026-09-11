package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.DashboardDTO;
import com.example.Salon_Management_System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
                "http://localhost:*",
                "http://127.0.0.1:*",
                "null"
        }
)
public class DashboardController {

    private final DashboardService dashboardService;


    // =========================================================
    // GET DASHBOARD
    // =========================================================
    //
    // Example:
    //
    // GET /api/v1/dashboard
    //
    // Default:
    // Current month → today
    //
    // =========================================================

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(

            @RequestParam(required = false)
            LocalDate fromDate,

            @RequestParam(required = false)
            LocalDate toDate

    ) {

        DashboardDTO dashboard =
                dashboardService.getDashboard(
                        fromDate,
                        toDate
                );

        return ResponseEntity.ok(dashboard);
    }


    // =========================================================
    // GET DASHBOARD BY DATE RANGE
    // =========================================================
    //
    // Example:
    //
    // GET /api/v1/dashboard/date-range
    //      ?fromDate=2026-09-01
    //      &toDate=2026-09-10
    //
    // =========================================================

    @GetMapping("/date-range")
    public ResponseEntity<DashboardDTO> getDashboardByDateRange(

            @RequestParam
            LocalDate fromDate,

            @RequestParam
            LocalDate toDate

    ) {

        DashboardDTO dashboard =
                dashboardService.getDashboard(
                        fromDate,
                        toDate
                );

        return ResponseEntity.ok(dashboard);
    }
}