package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long customerId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private Long userId;

    private String address;

    private LocalDate dateOfBirth;

    private String gender;

    private String notes;

    private Integer totalVisits;

    private LocalDate lastVisit;

    private String status;


    }
