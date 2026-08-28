package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.StaffAvailability;
import com.example.Salon_Management_System.enumiration.StaffStatus;
import com.example.Salon_Management_System.enumiration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO {
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
    private StaffStatus status;
    private StaffAvailability availability;
    private Long userId;
    private List<String> services;
}
