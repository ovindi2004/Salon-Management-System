package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.enumiration.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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

    private boolean passwordChanged;

    private String userPhone;

    private LocalDate userDob;

    private String userAddress;

    private String userGender;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // =========================
    // CUSTOMER RELATIONSHIP
    // =========================
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @EqualsAndHashCode.Exclude
    private Customer customer;

    // =========================
    // STAFF RELATIONSHIP
    // =========================
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @EqualsAndHashCode.Exclude
    private Staff staff;
}