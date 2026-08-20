package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private  String  customerAddress;
    private LocalDate dateOfBirth;
    private String gender;
    @Column(length = 1000)
    private String customerNotes;
    private Integer totalVisits =0;
    private LocalDate lastVisitDate;
    private String customerStatus= "Active";

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

}
