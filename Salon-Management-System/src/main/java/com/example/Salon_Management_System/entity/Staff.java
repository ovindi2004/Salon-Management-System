package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.StaffAvailability;
import com.example.Salon_Management_System.enumiration.StaffStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;


    // ============================================================
    // STAFF DETAILS
    // ============================================================

    private String staffCode;

    private String staffName;

    private String staffEmail;

    private String staffPhone;

    private LocalDate dateOfBirth;

    private String gender;

    private String address;

    private String position;

    private LocalDate hireDate;

    private Double salary;


    // ============================================================
    // STAFF STATUS
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StaffStatus status;


    // ============================================================
    // STAFF AVAILABILITY
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StaffAvailability availability;


    // ============================================================
    // STAFF → USER
    // One Staff → One User
    // ============================================================

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    // ============================================================
    // STAFF → WORKING HOURS
    // One Staff → Many Working Hours
    // ============================================================

    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffWorkingHour> workingHours =
            new ArrayList<>();


    // ============================================================
    // STAFF → LEAVES
    // One Staff → Many Leaves
    // ============================================================

    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffLeave> leaves =
            new ArrayList<>();


    // ============================================================
    // STAFF → APPOINTMENTS
    // One Staff → Many Appointments
    // ============================================================

    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Appointment> appointments =
            new ArrayList<>();
}