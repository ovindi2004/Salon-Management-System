package com.example.Salon_Management_System.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor
public class AdminUserRequestDTO {
    private String fullName; private String username; private String email; private String password;
    private String phone; private String address; private String role; private String status;
}
