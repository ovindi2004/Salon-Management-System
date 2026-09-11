package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportTypeIgnoreCase(String reportType);

    List<Report> findByFromDateGreaterThanEqualAndToDateLessThanEqual(LocalDate fromDate, LocalDate toDate);

    List<Report> findByReportTypeIgnoreCaseAndFromDateGreaterThanEqualAndToDateLessThanEqual(String reportType, LocalDate fromDate, LocalDate toDate);

    List<Report> findAllByOrderByGeneratedAtDesc();
}