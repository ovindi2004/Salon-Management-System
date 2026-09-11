package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.PaymentMethod;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;


    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;


    // ============================================================
    // PAYMENT METHOD
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;


    // ============================================================
    // PAYMENT STATUS
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus =
            PaymentStatus.PENDING;


    // ============================================================
    // PAYMENT DATE
    // ============================================================

    @Column(nullable = false)
    private LocalDate paymentDate;


    // ============================================================
    // TRANSACTION REFERENCE
    // ============================================================

    @Column(length = 100)
    private String transactionReference;


    // ============================================================
    // NOTES
    // ============================================================

    @Column(length = 1000)
    private String notes;
}