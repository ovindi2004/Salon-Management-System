package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staff_id",nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "service_id",nullable = false)
    private Service service;


    private LocalDate appointmentDate;
    private LocalTime startTime;
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(columnDefinition = "TEXT")
    private  String notes;
}
