package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long reportId;

    private String reportType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalDateTime generatedAt;

    private String generatedBy;
}