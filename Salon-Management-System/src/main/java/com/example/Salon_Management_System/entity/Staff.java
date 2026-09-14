package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.StaffAvailability;
import com.example.Salon_Management_System.enumiration.StaffStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Column(length = 20)
    private StaffStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StaffAvailability availability;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;


    @OneToMany(
            mappedBy = "staff",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<StaffWorkingHour> workingHours = new ArrayList<>();


    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<StaffLeave> leaves = new ArrayList<>();


    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment> appointments =
            new ArrayList<>();
}