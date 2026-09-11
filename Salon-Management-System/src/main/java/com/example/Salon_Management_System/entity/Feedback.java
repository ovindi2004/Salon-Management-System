package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.FeedbackStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "feedback",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_feedback_appointment",
                        columnNames = "appointment_id"
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    // ============================================================
    // APPOINTMENT RELATIONSHIP
    // One Appointment can have only one Feedback
    // ============================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;


    // ============================================================
    // CUSTOMER RELATIONSHIP
    // One Customer can have many Feedbacks
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;


    // ============================================================
    // RATING RELATIONSHIP
    // One Feedback has one Rating
    // Feedback is the owning side
    // ============================================================

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "rating_id",
            nullable = false,
            unique = true
    )
    private Rating rating;


    // ============================================================
    // FEEDBACK DETAILS
    // ============================================================

    @Column(
            length = 500,
            nullable = false
    )
    private String comment;


    @Column(nullable = false)
    private LocalDate feedbackDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status = FeedbackStatus.PENDING;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    // ============================================================
    // PRE PERSIST
    // ============================================================

    @PrePersist
    public void onCreate() {

        if (feedbackDate == null) {
            feedbackDate = LocalDate.now();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = FeedbackStatus.PENDING;
        }
    }
}