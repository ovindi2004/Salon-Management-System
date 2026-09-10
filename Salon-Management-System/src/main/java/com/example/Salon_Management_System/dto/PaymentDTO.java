package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.PaymentMethod;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    // Invoice
    private Long invoiceId;
    private String invoiceNumber;

    // Appointment
    private Long appointmentId;

    // Customer
    private Long customerId;
    private String customerName;
    private String customerPhone;

    // Service
    private Long serviceId;
    private String serviceName;

    // Staff
    private Long staffId;
    private String staffName;

    // Appointment details
    private LocalDate appointmentDate;
    private String appointmentTime;

    // Service price
    private BigDecimal servicePrice;

    // Payment
    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDate paymentDate;

    private String transactionReference;

    private String notes;
}