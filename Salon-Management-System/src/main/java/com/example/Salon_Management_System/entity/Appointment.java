package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;


    // ============================================================
    // CUSTOMER → APPOINTMENT
    // Many Appointments belong to One Customer
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;


    // ============================================================
    // SERVICE → APPOINTMENT
    // Many Appointments belong to One Service
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "service_id",
            nullable = false
    )
    private SalonService service;


    // ============================================================
    // STAFF → APPOINTMENT
    // Many Appointments belong to One Staff
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "staff_id",
            nullable = false
    )
    private Staff staff;


    // ============================================================
    // APPOINTMENT DETAILS
    // ============================================================

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;


    // ============================================================
    // APPOINTMENT STATUS
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private AppointmentStatus status =
            AppointmentStatus.SCHEDULED;


    // ============================================================
    // NOTES
    // ============================================================

    @Column(length = 1000)
    private String notes;


    // ============================================================
    // APPOINTMENT → INVOICE
    // One Appointment can have Zero or One Invoice
    // ============================================================

    @OneToOne(
            mappedBy = "appointment",
            fetch = FetchType.LAZY
    )
    private Invoice invoice;


    // ============================================================
    // APPOINTMENT → FEEDBACK
    // One Appointment can have Zero or One Feedback
    // ============================================================

    @OneToOne(
            mappedBy = "appointment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Feedback feedback;
}