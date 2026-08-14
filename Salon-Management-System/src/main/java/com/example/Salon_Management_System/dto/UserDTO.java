package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private int userPhone;
    private UserRole role;
}
