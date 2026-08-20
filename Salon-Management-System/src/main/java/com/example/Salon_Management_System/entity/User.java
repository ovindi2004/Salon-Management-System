package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.enumiration.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private String userEmail;

    private String userPassword;

    private int userPhone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;


}
