package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.DashboardDTO;

import java.time.LocalDate;

public interface DashboardService {

    DashboardDTO getDashboard(
            LocalDate fromDate,
            LocalDate toDate
    );
}