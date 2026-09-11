package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.DashboardDTO;
import com.example.Salon_Management_System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDashboard(@RequestParam(required = false) LocalDate fromDate,
                                       @RequestParam(required = false) LocalDate toDate) {
        DashboardDTO dashboard = dashboardService.getDashboard(fromDate, toDate);
        return new CommonResponse(0, dashboard, "Dashboard data loaded successfully");
    }

    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDashboardByDateRange(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        DashboardDTO dashboard = dashboardService.getDashboard(fromDate, toDate);

        return new CommonResponse(0, dashboard, "Dashboard data loaded successfully");
    }
}