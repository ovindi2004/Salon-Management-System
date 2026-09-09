package com.example.Salon_Management_System.dto;
import lombok.AllArgsConstructor; import lombok.Data; import lombok.NoArgsConstructor;
@Data @AllArgsConstructor @NoArgsConstructor
public class AdminUserResponseDTO {
    private Long adminUserId; private String fullName; private String username; private String email;
    private String role; private String status; private String phone; private String address; private String createdAt;
}
