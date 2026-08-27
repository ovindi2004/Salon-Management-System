package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class Staff {
    private Long staffId;
    private String staffName;
    private String staffEmail;
    private String staffPhone;
    private LocalDate dateOfBirth;
    private String gender;
    private  String address;
    private LocalDate hireDate;
    private Double salary;

    @Enumerated(EnumType.STRING)
    private UserStatus status;


}
