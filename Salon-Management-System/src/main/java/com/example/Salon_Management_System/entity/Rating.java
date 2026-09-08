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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false, unique = true)
    private Feedback feedback;

    @Column(nullable = false)
    private Integer ratingValue;

    @Column(nullable = false)
    private LocalDate ratingDate;

    @PrePersist
    public void onCreate() {
        if (ratingDate == null) {
            ratingDate = LocalDate.now();
        }
    }
}