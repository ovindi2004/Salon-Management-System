package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // ============================================================
    // FIND REPORTS BY REPORT TYPE
    // ============================================================

    List<Report> findByReportTypeIgnoreCase(String reportType);


    // ============================================================
    // FIND REPORTS BY DATE RANGE
    // ============================================================

    List<Report> findByFromDateGreaterThanEqualAndToDateLessThanEqual(
            LocalDate fromDate,
            LocalDate toDate
    );


    // ============================================================
    // FIND REPORTS BY TYPE AND DATE RANGE
    // ============================================================

    List<Report> findByReportTypeIgnoreCaseAndFromDateGreaterThanEqualAndToDateLessThanEqual(
            String reportType,
            LocalDate fromDate,
            LocalDate toDate
    );


    // ============================================================
    // FIND LATEST REPORTS
    // ============================================================

    List<Report> findAllByOrderByGeneratedAtDesc();
}