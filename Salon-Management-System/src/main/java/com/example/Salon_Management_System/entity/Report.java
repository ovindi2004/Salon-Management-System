package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;



    @Column(nullable = false, length = 100)
    private String reportType;


    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;


    @Column(nullable = false)
    private LocalDateTime generatedAt;


    @Column(nullable = false, length = 100)
    private String generatedBy;


    @PrePersist
    public void onCreate() {

        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }
    }
}