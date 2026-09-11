package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;


    // ============================================================
    // CUSTOMER DETAILS
    // ============================================================

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String customerAddress;

    private LocalDate dateOfBirth;

    private String gender;


    @Column(length = 1000)
    private String customerNotes;


    // ============================================================
    // CUSTOMER VISIT INFORMATION
    // ============================================================

    private Integer totalVisits = 0;

    private LocalDate lastVisitDate;

    private String customerStatus = "Active";

    private LocalDate createdAt;


    // ============================================================
    // AUTO CREATED DATE
    // ============================================================

    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDate.now();
        }

        if (totalVisits == null) {
            totalVisits = 0;
        }

        if (customerStatus == null) {
            customerStatus = "Active";
        }
    }


    // ============================================================
    // USER RELATIONSHIP
    // One Customer → One User
    // ============================================================

    @OneToOne
    @JoinColumn(
            name = "userId",
            nullable = false,
            unique = true
    )
    @EqualsAndHashCode.Exclude
    private User user;


    // ============================================================
    // CUSTOMER → APPOINTMENTS
    // One Customer → Many Appointments
    // ============================================================

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointments =
            new ArrayList<>();


    // ============================================================
    // CUSTOMER → FEEDBACKS
    // One Customer → Many Feedbacks
    // ============================================================

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private List<Feedback> feedbacks =
            new ArrayList<>();
}