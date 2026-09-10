package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    // Customer → Appointments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Service → Appointments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private SalonService service;

    // Staff → Appointments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(length = 1000)
    private String notes;

    // Appointment → Invoice
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Invoice invoice;

    // Appointment → Feedbacks
    @OneToMany(
            mappedBy = "appointment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Feedback> feedbacks = new ArrayList<>();
}