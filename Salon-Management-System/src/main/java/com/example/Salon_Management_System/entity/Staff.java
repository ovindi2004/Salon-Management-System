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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
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

    @Enumerated(EnumType.STRING)
    private StaffStatus status;

    @Enumerated(EnumType.STRING)
    private StaffAvailability availability;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffWorkingHour> workingHours = new ArrayList<>();

    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffLeave> leaves = new ArrayList<>();
}