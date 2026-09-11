package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {

    private long userId;

    private String userName;

    private String token;

    private UserRole userRole;
}