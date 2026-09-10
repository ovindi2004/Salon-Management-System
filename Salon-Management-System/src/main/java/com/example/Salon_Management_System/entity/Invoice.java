package com.example.Salon_Management_System.entity;

import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import com.example.Salon_Management_System.enumiration.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "invoice",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "invoice_number")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private String customerPhone;

    private String billingAddress;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private Double subtotal = 0.0;

    private Double discountPercent = 0.0;

    private Double discountAmount = 0.0;

    private Double taxRate = 0.0;

    private Double taxAmount = 0.0;

    private Double totalAmount = 0.0;

    private Double amountPaid = 0.0;

    private Double balanceDue = 0.0;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /*
     * ONE INVOICE -> MANY INVOICE ITEMS
     */


    @PrePersist
    protected void onCreate() {

        if (invoiceDate == null) {
            invoiceDate = LocalDate.now();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }

        if (discountPercent == null) {
            discountPercent = 0.0;
        }

        if (taxRate == null) {
            taxRate = 0.0;
        }

        if (amountPaid == null) {
            amountPaid = 0.0;
        }

        if (status == null) {
            status = InvoiceStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}