package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import com.example.Salon_Management_System.enumiration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private Long invoiceId;

    private String invoiceNumber;

    private Long appointmentId;


    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String billingAddress;


    private LocalDate invoiceDate;
    private LocalDate dueDate;

    private InvoiceStatus status;


    private Double subtotal;
    private Double discountPercent;
    private Double discountAmount;

    private Double taxRate;
    private Double taxAmount;

    private Double totalAmount;

    private Double amountPaid;
    private Double balanceDue;


    private PaymentMethod paymentMethod;

    private String notes;


    private List<InvoiceItemDTO> items;


    private List<PaymentDTO> payments;
}