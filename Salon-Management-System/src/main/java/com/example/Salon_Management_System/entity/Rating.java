package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;


    // ============================================================
    // RATING DETAILS
    // ============================================================

    @Column(
            nullable = false
    )
    private Integer ratingValue;


    @Column(nullable = false)
    private LocalDate ratingDate;


    // ============================================================
    // PRE PERSIST
    // ============================================================

    @PrePersist
    public void onCreate() {

        if (ratingDate == null) {
            ratingDate = LocalDate.now();
        }
    }
}