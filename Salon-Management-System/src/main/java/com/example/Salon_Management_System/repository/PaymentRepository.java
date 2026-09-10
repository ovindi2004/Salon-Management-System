package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.PaymentMethod;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Get all payments of an invoice
    List<Payment> findByInvoiceInvoiceId(Long invoiceId);

    // Get payments by status
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    // Get payments by payment method
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    // Get payments between two dates
    List<Payment> findByPaymentDateBetween(
            LocalDate from,
            LocalDate to
    );

    // Get payments by status and date range
    List<Payment> findByPaymentStatusAndPaymentDateBetween(
            PaymentStatus status,
            LocalDate from,
            LocalDate to
    );
}