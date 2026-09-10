package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    PaymentDTO createPayment(PaymentDTO dto);

    PaymentDTO getPaymentById(Long id);

    List<PaymentDTO> getAllPayments();

    PaymentDTO updatePayment(
            Long id,
            PaymentDTO dto
    );

    void deletePayment(Long id);

    PaymentDTO refundPayment(Long id);

    List<PaymentDTO> getPaymentsByDateRange(
            LocalDate from,
            LocalDate to
    );

    List<PaymentDTO> getPaymentsByInvoice(
            Long invoiceId
    );

    List<PaymentDTO> getPaymentsByStatus(
            String status
    );

    List<PaymentDTO> getPaymentsByMethod(
            String method
    );
}