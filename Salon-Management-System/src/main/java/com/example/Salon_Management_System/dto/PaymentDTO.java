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


    private Long invoiceId;
    private String invoiceNumber;


    private Long appointmentId;


    private Long customerId;
    private String customerName;
    private String customerPhone;

    private Long serviceId;
    private String serviceName;


    private Long staffId;
    private String staffName;


    private LocalDate appointmentDate;
    private String appointmentTime;


    private BigDecimal servicePrice;


    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDate paymentDate;

    private String transactionReference;

    private String notes;
}