package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.enumiration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private Boolean passwordChanged;
    private String userPhone;
    private LocalDate userDob;
    private String userAddress;
    private String userGender;
    private UserStatus status;
    private UserRole role;
}
