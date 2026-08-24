package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveResponseDTO {

    private Long customerId;

    private Long userId;

    private String customerName;

    private String email;

    private String temporaryPassword;
}